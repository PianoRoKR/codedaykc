package com.example.codedaykcrunningapp;

import java.util.ArrayList;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class StepDetector implements SensorEventListener
{
    private final static String TAG = "StepDetector";
    private float   mLimit = 10;
    private float   mLastValues = 0f;
    private float   mScale[] = new float[2];
    private float   mYOffset;

    private float   mLastDirections = 0f;
    private float   mLastExtremes[] = new float[2];
    private float   mLastDiff = 0f;
    private int     mLastMatch = -1;
    
    private ArrayList<StepListener> mStepListeners = new ArrayList<StepListener>();
    
    public StepDetector() {
        int h = 480; // TODO: remove this constant
        mYOffset = h * 0.5f;
        mScale[0] = - (h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
        mScale[1] = - (h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
    }
    
    public void setSensitivity(float sensitivity) {
        mLimit = sensitivity; // 1.97  2.96  4.44  6.66  10.00  15.00  22.50  33.75  50.62
    }
    
    public void addStepListener(StepListener sl) {
        mStepListeners.add(sl);
    }
    
    //public void onSensorChanged(int sensor, float[] values) {
    @SuppressWarnings("deprecation")
	public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor; 
        synchronized (this) {
            if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
            }
            else {
                if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    float vSum = 0;
                    for (int i=0 ; i<3 ; i++) {
                        final float v = mYOffset + event.values[i] * mScale[1];
                        vSum += v;
                    }
                    float v = vSum / 3;
                    
                    float direction = (v > mLastValues ? 1 : (v < mLastValues ? -1 : 0));
                    if (direction == - mLastDirections) {
                    	
                        // Direction changed
                        int extType = (direction > 0 ? 0 : 1); // minumum or maximum?
                        mLastExtremes[extType] = mLastValues;
                        float diff = Math.abs(mLastExtremes[extType] - mLastExtremes[1 - extType]);

                        if (diff > mLimit) {
                            
                            boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff * (2/3));
                            boolean isPreviousLargeEnough = mLastDiff > (diff/3);
                            boolean isNotContra = (mLastMatch != 1 - extType);
                            
                            // Log step and call listeners
                            if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
                                Log.i(TAG, "step");
                                for (StepListener stepListener : mStepListeners) {
                                    stepListener.onStep();
                                }
                                mLastMatch = extType;
                            }
                            else {
                                mLastMatch = -1;
                            }
                        }
                        // Set last difference
                        mLastDiff = diff;
                    }
                    mLastDirections = direction;
                    mLastValues = v;
                }
            }
        }
    }
    
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

}
