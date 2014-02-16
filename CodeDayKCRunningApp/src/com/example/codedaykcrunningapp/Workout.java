package com.example.codedaykcrunningapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Workout extends Activity {

	private SeekBar seekbar;
	private TextView minutes;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workout);
		seekbar = (SeekBar) findViewById(R.id.seekBar1);
		minutes = (TextView)findViewById(R.id.textView2);
		minutes.setText(String.valueOf(seekbar.getProgress() + " minute miles"));
		seekbar.setOnSeekBarChangeListener(
				new OnSeekBarChangeListener() {
					
					@Override
					public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
						int y = seekbar.getProgress();
						
						minutes.setText(String.valueOf(y) + " minute miles");
						
					}
					@Override
					public void onStartTrackingTouch(SeekBar arg0) {
						// TODO Auto-generated method stub
						
					}
					@Override
					public void onStopTrackingTouch(SeekBar arg0) {
						// TODO Auto-generated method stub
						//int x = seekbar.getProgress();
						
						//minutes.setText(String.valueOf(x));
					}
				});
	}
}
