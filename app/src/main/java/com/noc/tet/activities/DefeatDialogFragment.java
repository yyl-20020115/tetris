package com.noc.tet.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.noc.tet.R;

public class DefeatDialogFragment extends DialogFragment {

    private CharSequence scoreString;
    private CharSequence timeString;
    private CharSequence apmString;
    private long score;
    private int level;
    private int apm;
    private String time;

    public DefeatDialogFragment() {
        super();
        scoreString = "unknown";
        timeString = "unknown";
        apmString = "unknown";
    }

    public void setData(long scoreArg, int level, int apm, String time) {
        scoreString = String.valueOf(scoreArg);
        timeString = this.time = time;
        apmString = String.valueOf(this.apm = apm);
        score = scoreArg;
        this.level = level;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.defeatDialogTitle);
        builder.setMessage(
                getResources().getString(R.string.scoreLabel) +
                        "\n    " + scoreString + "\n\n" +
                        getResources().getString(R.string.timeLabel) +
                        "\n    " + timeString + "\n\n" +
                        getResources().getString(R.string.apmLabel) +
                        "\n    " + apmString + "\n\n" +
                        getResources().getString(R.string.hint)
        );
        builder.setNeutralButton(R.string.defeatDialogReturn,
                (dialog, which) -> ((GameActivity) getActivity()).putScore(score, level, apm, time));
        return builder.create();
    }
}
