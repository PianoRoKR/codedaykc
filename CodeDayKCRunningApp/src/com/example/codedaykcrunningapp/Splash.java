package com.example.codedaykcrunningapp;

import com.example.codedaykcrunningapp.LoadingListener.LoadingTaskListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class Splash extends Activity implements LoadingTaskListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}
	
	public void onTaskFinished() {
		Intent intent = new Intent(Splash.this, MainActivity.class);
		startActivity(intent);
		finish(); // Finish the activity making it impossible for user to return
;	}

}
