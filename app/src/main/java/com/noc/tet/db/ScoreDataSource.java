package com.noc.tet.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

public class ScoreDataSource {

    // Database fields
    private SQLiteDatabase database;
    private final HighScoreOpenHelper dbHelper;
    private final String[] allColumns = {
            HighScoreOpenHelper.COLUMN_ID,
            HighScoreOpenHelper.COLUMN_SCORE,
            HighScoreOpenHelper.COLUMN_PLAYER_NAME,
            HighScoreOpenHelper.COLUMN_LEVEL,
            HighScoreOpenHelper.COLUMN_APM,
            HighScoreOpenHelper.COLUMN_TIME
    };

    public ScoreDataSource(Context context) {
        dbHelper = new HighScoreOpenHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createScore(long score, int level, int apm, String time, String playerName) {
        ContentValues values = new ContentValues();
        values.put(HighScoreOpenHelper.COLUMN_SCORE, score);
        values.put(HighScoreOpenHelper.COLUMN_PLAYER_NAME, playerName);
        values.put(HighScoreOpenHelper.COLUMN_LEVEL, level);
        values.put(HighScoreOpenHelper.COLUMN_APM, apm);
        values.put(HighScoreOpenHelper.COLUMN_TIME, time);

        long insertId = database.insert(HighScoreOpenHelper.TABLE_HIGH_SCORES, null, values);
        Cursor cursor = database.query(HighScoreOpenHelper.TABLE_HIGH_SCORES,
                allColumns, HighScoreOpenHelper.COLUMN_ID + " = " + insertId, null,
                null, null, HighScoreOpenHelper.COLUMN_SCORE + " DESC");
        cursor.moveToFirst();
        //cursorToScore(cursor);
        cursor.close();
    }

    public int getCount() {
        String countQuery = "SELECT COUNT(*) FROM " + HighScoreOpenHelper.TABLE_HIGH_SCORES;
        Cursor cursor = this.database.rawQuery(countQuery, null);
        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        return count;
    }

    public void deleteScore(Score score) {
        long id = score.getId();
        //System.out.println("Comment deleted with id: " + id);
        database.delete(HighScoreOpenHelper.TABLE_HIGH_SCORES, HighScoreOpenHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteAllScores() {
        database.delete(HighScoreOpenHelper.TABLE_HIGH_SCORES, "", new String[0]);
    }

    @NonNull
    private Score cursorToScore(Cursor cursor) {
        Score score = new Score();
        score.setId(cursor.getLong(0));
        score.setScore(cursor.getLong(1));
        score.setName(cursor.getString(2));
        score.setLevel(cursor.getInt(3));
        score.setApm(cursor.getInt(4));
        score.setTime(cursor.getString(5));
        return score;
    }

    public Cursor getCursor() {
        return database.query(HighScoreOpenHelper.TABLE_HIGH_SCORES,
                allColumns, null, null, null, null, HighScoreOpenHelper.COLUMN_SCORE + " DESC");
    }
}
