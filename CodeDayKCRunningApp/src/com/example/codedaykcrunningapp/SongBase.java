package com.example.codedaykcrunningapp;

import java.sql.SQLException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SongBase {

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL(DATABASE_CREATE);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(SongBase.class.getName(), "Upgrading database from version "
					+ oldVersion + " to " + newVersion
					+ ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
			onCreate(db);
		}

	}

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "_title";
	public static final String COLUMN_ARTIST = "_artist";
	public static final String COLUMN_BPM = "_bpm";

	public static final String COLUMN_URI = "_uri";
	private static final String DATABASE_NAME = "SongBase";

	private static final int DATABASE_VERSION = 2;
	private final DatabaseHelper dbHelper;

	private SQLiteDatabase db;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ DATABASE_NAME + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_TITLE
			+ " text not null, " + COLUMN_ARTIST + " text not null, "
			+ COLUMN_BPM + " text not null, " + COLUMN_URI + " text not null);";

	public SongBase(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	// close the db
	public void close() {
		dbHelper.close();
	}

	// deletes a particular record
	public boolean deleteRecord(long rowId) {
		return db.delete(DATABASE_NAME, COLUMN_ID + "=" + rowId, null) > 0;
	}

	// Retrieves all records
	public Cursor getAllRecords(int bpm) {
		String high = String.valueOf(bpm + 5);
		String low = String.valueOf(bpm - 2);
		String[] selectionArgs = new String[] { low, high };
		String selection = COLUMN_URI + " BETWEEN ? AND ?";
		String[] proj = new String[] { COLUMN_URI };
		return GetBetween(proj, selection, selectionArgs);

	}

	public Cursor GetBetween(String[] projection, String selection,
			String[] args) {
		return db.query(DATABASE_NAME, projection, selection, args, null, null,
				null);
	}

	// Retrieves a particular record
	public Cursor getRecord(long rowId) {
		Cursor thisCursor = db.query(true, DATABASE_NAME,
				new String[] { COLUMN_ID, COLUMN_TITLE, COLUMN_ARTIST,
						COLUMN_BPM, COLUMN_URI }, null, null, null, null, null,
				null);
		if (thisCursor != null) {
			thisCursor.moveToFirst();
		}
		return thisCursor;
	}

	// insert a record
	public long insertRecord(String title, String artist, String bpm, String uri) {
		ContentValues initValues = new ContentValues();
		initValues.put(COLUMN_TITLE, title);
		initValues.put(COLUMN_ARTIST, artist);
		initValues.put(COLUMN_BPM, bpm);
		initValues.put(COLUMN_URI, uri);

		return db.insert(DATABASE_NAME, null, initValues);
	}

	// Open the db
	public SongBase open() throws SQLException {
		db = dbHelper.getWritableDatabase();
		return this;
	}

	// updates a record
	public boolean updateRecord(long rowId, String title, String artist,
			String bpm, String uri) {
		ContentValues initValues = new ContentValues();
		initValues.put(COLUMN_TITLE, title);
		initValues.put(COLUMN_ARTIST, artist);
		initValues.put(COLUMN_BPM, bpm);
		initValues.put(COLUMN_URI, uri);

		return db.update(DATABASE_NAME, initValues, COLUMN_ID + "=" + rowId,
				null) > 0;
	}

}
