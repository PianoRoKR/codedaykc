package com.example.codedaykcrunningapp;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Workout extends Activity {
	class firstTask extends TimerTask {

		@Override
		public void run() {
			h.sendEmptyMessage(0);
		}
	}

	class secondTask extends TimerTask {

		@Override
		public void run() {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					long millis = System.currentTimeMillis() - starttime;
					int seconds = (int) (millis / 1000);
					int minutes = seconds / 60;
					seconds = seconds % 60;

					time.setText(String.format("%d:%02d", minutes, seconds));
				}
			});
		}
	};

	TextView time;

	final Handler h = new Handler(new Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			long millis = System.currentTimeMillis() - starttime + offset;
			int seconds = (int) (millis / 1000);
			int minutes = seconds / 60;
			seconds = seconds % 60;

			time.setText(String.format("%d:%02d", minutes, seconds));
			return false;
		}
	});

	private SeekBar seekbar;
	private TextView minutes;
	private long starttime = 0;
	private long offset = 0;

	Handler h2 = new Handler();

	Runnable run = new Runnable() {

		@Override
		public void run() {
			long millis = System.currentTimeMillis() - starttime + offset;
			int seconds = (int) (millis / 1000);
			int minutes = seconds / 60;
			seconds = seconds % 60;

			time.setText(String.format("%d:%02d", minutes, seconds));

			h2.postDelayed(this, 500);
		}
	};;

	Timer timer = new Timer();

	private boolean paused = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workout);

		time = (TextView) findViewById(R.id._time);

		ImageButton b = (ImageButton) findViewById(R.id.imageButton1);
		starttime = System.currentTimeMillis();
		timer = new Timer();
		timer.schedule(new firstTask(), 0, 500);
		timer.schedule(new secondTask(), 0, 500);
		h2.postDelayed(run, 0);
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageButton b = (ImageButton) v;
				if (!paused) {
					timer.cancel();
					timer.purge();
					h2.removeCallbacks(run);
					offset = System.currentTimeMillis() - starttime;
					b.setImageResource(R.drawable.play);
				} else {
					starttime = System.currentTimeMillis();
					timer = new Timer();
					timer.schedule(new firstTask(), 0, 500);
					timer.schedule(new secondTask(), 0, 500);
					h2.postDelayed(run, 0);
					b.setImageResource(R.drawable.pause);
				}
				paused = !paused;
			}
		});

		seekbar = (SeekBar) findViewById(R.id.seekBar1);
		minutes = (TextView) findViewById(R.id.textView2);
		minutes.setText(String.valueOf(seekbar.getProgress() + " minute miles"));
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progressValue,
					boolean fromUser) {
				int y = seekbar.getProgress();

				minutes.setText(String.valueOf(y) + " minute miles");

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
			}
		});
	}
}
