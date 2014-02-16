package com.example.codedaykcrunningapp;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class StepService extends Service {
	
	private static final String TAG = "com.example.codedaykcrunningapp.StepService";
	
	private Sensor mSensor;
	private SensorManager mSensorManager;
	private StepDetector mStepDetector;
	
	/**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class StepBinder extends Binder {
        StepService getService() {
            return StepService.this;
        }
    }
    
    @Override
    public void onCreate() {
        Log.i(TAG, "[SERVICE] onCreate");
        super.onCreate();
        
        // Start detecting
        mStepDetector = new StepDetector();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        registerDetector();

        // Register our receiver for the ACTION_SCREEN_OFF action. This will make our receiver
        // code be called whenever the phone enters standby mode.
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, filter);
        
    }
    
    @Override
    public void onDestroy() {
    	Log.i(TAG, "[SERVICE] onDestroy");
    	
    	// Unregister receiver and detector
    	unregisterReceiver(mReceiver);
    	unregisterDetector();
    }
    
    private void registerDetector() {
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER );
        mSensorManager.registerListener(mStepDetector,
        								mSensor,
        								SensorManager.SENSOR_DELAY_FASTEST); // Fastest works best
    }

    // Called from onDestroy and when Screen turned off (unregister then re-register)
    private void unregisterDetector() {
        mSensorManager.unregisterListener(mStepDetector);
    }
	
	// BroadcastReceiver for handling ACTION_SCREEN_OFF.
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	
        	if(intent.getAction().equals(intent.ACTION_SCREEN_OFF)) {
        		// Unregisters the listener and registers it again.
                StepService.this.unregisterDetector();
                StepService.this.registerDetector();
                
        	}        	
        }
    };
    
    @Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
