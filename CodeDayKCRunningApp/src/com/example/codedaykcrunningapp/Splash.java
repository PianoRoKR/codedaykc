package com.example.codedaykcrunningapp;

import com.example.codedaykcrunningapp.LoadingListener.LoadingTaskListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.widget.ProgressBar;

public class Splash extends Activity implements LoadingTaskListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SharedPreferences prefs = getSharedPreferences("prefName", MODE_PRIVATE);
		if(!prefs.getBoolean("isFirstRun", false)) {
		    // This mean App Launch First Time
		    prefs = getSharedPreferences("prefName", MODE_PRIVATE);
		    SharedPreferences.Editor edit= prefs.edit();

		    edit.putBoolean("isFirstRun", true);
		    edit.commit();
		    // Open the B Activity
		} else {
		    // This mean App Launch Second or third ... time and start the A Activity
		}
		
		setContentView(R.layout.activity_splash);
		
		ProgressBar loadBar = (ProgressBar) findViewById(R.id.loadingBar);
		
		new LoadingListener(loadBar, this).execute("www.google.co.uk");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}
	
	@Override
	public void onTaskFinished() {
		Intent intent = new Intent(Splash.this, MainActivity.class);
		startActivity(intent);
		finish(); // Finish the activity making it impossible for user to return
;	}

}
