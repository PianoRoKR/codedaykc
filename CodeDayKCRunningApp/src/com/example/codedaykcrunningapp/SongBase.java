package com.example.codedaykcrunningapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SongBase extends SQLiteOpenHelper {
	
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "_title";
	public static final String COLUMN_ARTIST = "_artist";
	public static final String COLUMN_BPM = "_bpm";
	public static final String COLUMN_URI = "_uri";
	
	private static final String DATABASE_NAME = "SongBase";
	private static final int DATABASE_VERSION = 1;
	
	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
		      + DATABASE_NAME + "(" + COLUMN_ID
		      + " integer primary key autoincrement, " + COLUMN_TITLE
		      + " text not null, " + COLUMN_ARTIST 
		      + " text not null, " + COLUMN_BPM
		      + " text not null, " + COLUMN_URI
		      + " text not null);";

	public SongBase(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		
		
		
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		
		database.execSQL(DATABASE_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SongBase.class.getName(),
		        "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		    db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
		    onCreate(db);
	}

}
