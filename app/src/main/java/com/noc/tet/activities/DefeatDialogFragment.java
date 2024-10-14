package com.noc.tet.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.noc.tet.R;

public class DefeatDialogFragment extends DialogFragment {

	private CharSequence scoreString;
	private CharSequence timeString;
	private CharSequence apmString;
	private long score;
	
	public DefeatDialogFragment() {
		super();
		scoreString = "unknown";
		timeString = "unknown";
		apmString = "unknown";
	}
	
	public void setData(long scoreArg, String time, int apm) {
		scoreString = String.valueOf(scoreArg);
		timeString = time;
		apmString = String.valueOf(apm);
		score = scoreArg;
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
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			builder.setNeutralButton(R.string.defeatDialogReturn, (dialog, which) -> ((GameActivity)getActivity()).putScore(score));
		}
		return builder.create();
	}
}
