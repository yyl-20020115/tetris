package com.noc.tet.db;

import androidx.annotation.NonNull;

public class Score {
    private long id;
    private long score;
    private String playerName;

    private int level;
    private int apm;
    private String time;

    public Score() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getScore() {
        return score;
    }

    public String getScoreString() {
        return String.valueOf(score);
    }

    public void setScore(long comment) {
        this.score = comment;
    }

    public String getName() {
        return playerName;
    }

    public void setName(String comment) {
        this.playerName = comment;
    }

    // Will be used by the ArrayAdapter in the ListView
    @NonNull
    @Override
    public String toString() {
        return score + "@" + playerName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getApm() {
        return apm;
    }

    public void setApm(int apm) {
        this.apm = apm;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
