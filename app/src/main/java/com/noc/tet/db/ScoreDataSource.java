package com.noc.tet.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ScoreDataSource {

	// Database fields
	  private SQLiteDatabase database;
	  private final HighScoreOpenHelper dbHelper;
	  private final String[] allColumns = { HighScoreOpenHelper.COLUMN_ID,
			  HighScoreOpenHelper.COLUMN_SCORE,
			  HighScoreOpenHelper.COLUMN_PLAYER_NAME};

	  public ScoreDataSource(Context context) {
	    dbHelper = new HighScoreOpenHelper(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  public Score createScore(long score, String playerName) {
	    ContentValues values = new ContentValues();
	    values.put(HighScoreOpenHelper.COLUMN_SCORE, score);
	    values.put(HighScoreOpenHelper.COLUMN_PLAYER_NAME, playerName);
	    long insertId = database.insert(HighScoreOpenHelper.TABLE_HIGH_SCORES, null, values);
	    Cursor cursor = database.query(HighScoreOpenHelper.TABLE_HIGH_SCORES,
	        allColumns, HighScoreOpenHelper.COLUMN_ID + " = " + insertId, null,
	        null, null, HighScoreOpenHelper.COLUMN_SCORE + " DESC");
	    cursor.moveToFirst();
	    Score newScore = cursorToScore(cursor);
	    cursor.close();
	    return newScore;
	  }

	  public void deleteScore(Score score) {
	    long id = score.getId();
	    //System.out.println("Comment deleted with id: " + id);
	    database.delete(HighScoreOpenHelper.TABLE_HIGH_SCORES, HighScoreOpenHelper.COLUMN_ID
	        + " = " + id, null);
	  }

	  private Score cursorToScore(Cursor cursor) {
		  Score score = new Score();
		  score.setId(cursor.getLong(0));
		  score.setScore(cursor.getLong(1));
		  score.setName(cursor.getString(2));
	    return score;
	  }

	public Cursor getCursor() {
		return database.query(HighScoreOpenHelper.TABLE_HIGH_SCORES,
		        allColumns, null, null, null, null, HighScoreOpenHelper.COLUMN_SCORE + " DESC");
	}
	  
}
