package com.example.codedaykcrunningapp;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Track;
import com.echonest.api.v4.TrackAnalysis;
import com.example.codedaykcrunningapp.SongBase;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class AnalyzeMusic extends Activity {
	
     Handler myHandler = new Handler() {

	    @Override
	    public void handleMessage(Message msg) {
	        switch (msg.what) {
	        case 0:
	            finish();
	            break;
	        default:
	            break;
	        }
	    }
	};
	
	// Song Library
	public List<String[]> songsList = new ArrayList<String[]>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_analyze_music);
		MusicAnalysis thread = new MusicAnalysis();
		thread.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.analyze_music, menu);
		return true;
	}
	
	private class MusicAnalysis extends AsyncTask<Void, String, Void> {
		ProgressDialog pDialog;
		private final String API_KEY = "6HI4WT1SDG7B6TFYG";
		EchoNestAPI mEn = new EchoNestAPI(API_KEY);
		
		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(AnalyzeMusic.this);
			pDialog.setTitle("Loading your music!");
			pDialog.setMessage("Getting music library...");
			pDialog.setMax(100);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			createMusicList();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			publishProgress("Loaded music! Determining BPM...");
			getBPM();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			publishProgress("Finished!");
			return null;
		}
		
		protected void onPostExecute(Void result){
			super.onPostExecute(result);
			pDialog.dismiss();
			myHandler.sendEmptyMessage(0);
		}
		
		protected void onProgressUpdate(String... message)
		{
			pDialog.setMessage(message[0]);
		}
		
		// Finds locally stored music and saves the title, artist, and path to list
		private void createMusicList()
		{
			Uri db = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
			String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0 AND " + MediaStore.Audio.Media.DURATION + " > 60000";
			int count;
			
			String[] projection = new String[] { 
					MediaStore.Audio.Media.TITLE,
		    		MediaStore.Audio.Media.ARTIST,
		    		MediaStore.Audio.Media.DATA
		    		
		    	};
			
			Cursor cursor = getContentResolver().query(db, projection, selection, null, null);
			count = cursor.getCount();
			if(count > 0 )
			{
				for(int i = 0; i < count; i++)
				{
					cursor.moveToPosition(i);
					String[] musicData = {cursor.getString(0), cursor.getString(1), cursor.getString(2)};
					songsList.add(musicData);
				}
			}
			else { Log.w("musicApp", "No Music");}
			cursor.close();
		}
		
		// Gets the BPM from songs in list and saves them to the Database
		private void getBPM()
		{
			Track track = null;
			TrackAnalysis trackanalysis = null;
			double bpm;
			String songURI;
			for(int i = 0 ; i < songsList.size(); i++)
			{
				track = null;
				songURI = songsList.get(i)[2];
				File f = new File(songURI);
				publishProgress("Checking: " + songURI);
				try {
					publishProgress("Uploading..." + songsList.get(i)[0]);
					track = mEn.uploadTrack(f, false);
				} catch (EchoNestException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(track == null)
					continue;
				try {
					publishProgress(track.getTitle());
				} catch (EchoNestException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					bpm = track.getTempo();
					publishProgress(String.valueOf(bpm));
				} catch (EchoNestException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
		
	}
}
