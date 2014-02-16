package com.example.codedaykcrunningapp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
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
	private final String mpmKey = "mpm";
	private final String mphKey = "mph";
	private float mpmValue;
	private float mphValue;
	private final float minValue = 4.00f;
	private final float maxValue = 20.00f;

	public List<String[]> songsList = new ArrayList<String[]>();

	// Create Media Player
	MediaPlayer mediaPlayer = new MediaPlayer();

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

		// Get URI from the song list
		Uri myUri = Uri.parse(songsList.get(1)[2]);

		try {
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setDataSource(getApplicationContext(), myUri);
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (Exception e) {
			Log.w("Song Player", "Failed to play song");
		}

		mediaPlayer.start();

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageButton b = (ImageButton) v;

				if (!paused && !mediaPlayer.isPlaying()) {
					// Get URI from the song list
					Uri myUri = Uri.parse(songsList.get(1)[2]);

					try {
						mediaPlayer
								.setAudioStreamType(AudioManager.STREAM_MUSIC);
						mediaPlayer.setDataSource(getApplicationContext(),
								myUri);
						mediaPlayer.prepare();
						mediaPlayer.start();
					} catch (Exception e) {
						Log.w("Song Player", "Failed to play song");
					}

					mediaPlayer.start();
				} else if (!paused) {
					mediaPlayer.pause();
				} else {
					mediaPlayer.start();
				}

				if (!paused) {
					timer.cancel();
					timer.purge();
					h2.removeCallbacks(run);
					b.setImageResource(R.drawable.play);
				} else {
					offset = System.currentTimeMillis() - starttime;
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
		final TextView mpmValueTV = (TextView) findViewById(R.id.mpmValueTV);
		final TextView mphValueTV = (TextView) findViewById(R.id.mphTV);
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

				float prog = Float.parseFloat(String.valueOf(progress)) / 64;
				mpmValue = minValue + (maxValue - minValue) * prog;

				setMph(mpmValue);

				String hrStr, minStr;
				int hours, mins;
				hours = (int) mpmValue;
				hrStr = String.valueOf(hours);
				hrStr += ":";
				mpmValue = (mpmValue - hours) * 60;
				mins = (int) mpmValue;

				// Account for 0 as integer (add another 0)
				if (String.valueOf(mins).length() == 1)
					minStr = "0" + String.valueOf(mins);
				else
					minStr = String.valueOf(mins);

				mpmValueTV.setText(hrStr + minStr);
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
			}

			private void setMph(float val) {

				if (val == 0) {
					mphValueTV.setText("0.0");
					return;
				}
				mphValue = 1 / val * 60;
				mphValueTV.setText(new DecimalFormat("#.00").format(mphValue));

			}
		});
	}
}
