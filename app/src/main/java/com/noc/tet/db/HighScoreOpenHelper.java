package com.noc.tet.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HighScoreOpenHelper extends SQLiteOpenHelper {

	public static final String TABLE_HIGH_SCORES = "highscores";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_SCORE = "score";
	public static final String COLUMN_PLAYER_NAME = "playername";

	public static final String COLUMN_LEVEL = "level";

	public static final String COLUMN_TIME = "time";

	public static final String COLUMN_APM="apm";

	private static final String DATABASE_NAME = "highscores.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
	      + TABLE_HIGH_SCORES + "(" + COLUMN_ID
	      + " integer primary key autoincrement, "
			+ COLUMN_SCORE + " integer, "
			+ COLUMN_PLAYER_NAME + " text, "
			+ COLUMN_LEVEL + " integer, "
			+ COLUMN_APM + " integer, "
			+ COLUMN_TIME + " text);"
			;
	  
    public HighScoreOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(HighScoreOpenHelper.class.getName(),
			"Upgrading database from version " + oldVersion + " to "
			+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIGH_SCORES);
		onCreate(db);
	}

}
