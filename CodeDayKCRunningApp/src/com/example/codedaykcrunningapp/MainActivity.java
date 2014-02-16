package com.example.codedaykcrunningapp;

import java.text.DecimalFormat;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private final String mpmKey = "mpm";
	private final String mphKey = "mph";
	private float mpmValue;
	private float mphValue;
	private final float minValue = 4.00f;
	private final float maxValue = 20.00f;
	private SharedPreferences prefs;
	private SharedPreferences.Editor prefEditor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Load previous user data (needs work)
		prefs = this.getSharedPreferences("com.superjova.mainstart", Context.MODE_PRIVATE);
		prefEditor = prefs.edit();
		String savedMpm = prefs.getString(mpmKey, "9:00");
		
		// Seek bar
		SeekBar sb = (SeekBar) findViewById(R.id.mpmSB);
		//int lastProgress = Integer.parseInt(String.valueOf(findProgress(savedMpm)));
		//sb.setProgress(lastProgress);
		final TextView mpmValueTV = (TextView) findViewById(R.id.mpmValueTV);
		final TextView mphValueTV = (TextView) findViewById(R.id.mphTV);		
		
		sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {			
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				
				float prog = Float.parseFloat(String.valueOf(progress)) / 64;
				mpmValue = minValue + (maxValue - minValue) * prog;	
				
				setMph(mpmValue);
				
				String hrStr, minStr;
				int hours, mins;
				hours = (int)mpmValue;
				hrStr = String.valueOf(hours);
				hrStr += ":";
				mpmValue = (mpmValue - hours)*60;
			    mins = (int)mpmValue;
			    
			    // Account for 0 as integer (add another 0)
			    if (String.valueOf(mins).length() == 1)
			    	minStr = "0" + String.valueOf(mins);
			    else
			    	minStr = String.valueOf(mins);
			    
			    mpmValueTV.setText(hrStr + minStr); 
			    		
			}
			
			private void setMph(float val) {
				
				if(val==0)
				{
					mphValueTV.setText("0.0");
					return;
				}
				mphValue = (1 / val)*60;
				mphValueTV.setText(new DecimalFormat("#.00").format(mphValue));
				
			}
		});	
	}
	
	public float findProgress(String savedMpm) {
		
		int mins = Integer.parseInt(savedMpm.split(":")[0]) - 4;
		int sec = Integer.parseInt(savedMpm.split(":")[1]);
		return mins * 4 + sec;
	}	

	public void startWorkout(View view)
	{
		Intent i = new Intent(getApplicationContext(), Workout.class);
		startActivity(i);
	}
	
	

}
