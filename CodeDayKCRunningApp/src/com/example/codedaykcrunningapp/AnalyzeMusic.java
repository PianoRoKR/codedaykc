package com.example.codedaykcrunningapp;

import java.util.ArrayList;
import java.util.List;
import com.example.codedaykcrunningapp.SongBase;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;

public class AnalyzeMusic extends Activity {
	
	// Song Library
	public List<String[]> songsList = new ArrayList<String[]>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_analyze_music);
		createMusicList();
		getBPM();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.analyze_music, menu);
		return true;
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
		for(int i = 0 ; i < songsList.size(); i++)
		{
			
			
		}
	}
}
