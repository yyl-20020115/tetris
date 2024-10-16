package com.noc.tet.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.noc.tet.R;
import com.noc.tet.components.GameState;
import com.noc.tet.components.Sound;
import com.noc.tet.db.HighScoreOpenHelper;
import com.noc.tet.db.ScoreDataSource;

import java.util.Locale;
import java.util.TimeZone;


public class MainActivity extends ListActivity {

	public static final int SCORE_REQUEST = 0x0;
	
	/** This key is used to access the player name, which is returned as an Intent from the gameactivity upon completion (gameover).
	 *  The Package Prefix is mandatory for Intent data
	 */
	public static final String PLAYER_NAME_KEY = "com.noc.tet.activities.playername";
	
	/** This key is used to access the player name, which is returned as an Intent from the gameactivity upon completion (gameover).
	 *  The Package Prefix is mandatory for Intent data
	 */
	public static final String SCORE_KEY = "com.noc.tet.activities.score";
	
	public ScoreDataSource datasource;
	private SimpleCursorAdapter adapter;
	private AlertDialog.Builder startLevelDialog;
	private AlertDialog.Builder donateDialog;
	private int startLevel;
	private View dialogView;
	private SeekBar leveldialogBar;
	private TextView leveldialogtext;
	private Sound sound;
	private void setupLocale(Context context){
		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		Configuration conf = resources.getConfiguration();
		Locale sysLocale;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			sysLocale = resources.getConfiguration().getLocales().get(0);
		} else {
			sysLocale = resources.getConfiguration().locale;
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			if ("zh".equals(sysLocale.getLanguage())) { // 如果是中文
					conf.setLocale(Locale.SIMPLIFIED_CHINESE);
			} else { // 默认使用英语
				conf.setLocale(Locale.ENGLISH);
			}
		}

		resources.updateConfiguration(conf, dm);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setupLocale(this);
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		setContentView(R.layout.activity_main);
		PreferenceManager.setDefaultValues(this, R.xml.simple_preferences, true);
		PreferenceManager.setDefaultValues(this, R.xml.advanced_preferences, true);
		
		/* Create Music */
		sound = new Sound(this);
		sound.startMusic(Sound.MENU_MUSIC, 0);

		/* Database Management */
		Cursor mc;
	    datasource = new ScoreDataSource(this);
	    datasource.open();
	    mc = datasource.getCursor();
	    // Use the SimpleCursorAdapter to show the
	    // elements in a ListView
	    adapter = new SimpleCursorAdapter(
                this,
	        R.layout.blockinger_list_item,
	        mc,
	        new String[] {HighScoreOpenHelper.COLUMN_SCORE, HighScoreOpenHelper.COLUMN_PLAYER_NAME},
	        new int[] {R.id.text1, R.id.text2},
	        SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	    setListAdapter(adapter);
	    
	    /* Create Startlevel Dialog */
		startLevel = 0;
		startLevelDialog = new AlertDialog.Builder(this);
		startLevelDialog.setTitle(R.string.startLevelDialogTitle);
		startLevelDialog.setCancelable(false);
		startLevelDialog.setNegativeButton(R.string.startLevelDialogCancel, (dialog, which) -> dialog.dismiss());
		startLevelDialog.setPositiveButton(R.string.startLevelDialogStart, (dialog, which) -> MainActivity.this.start());
	    
		/* Create Donate Dialog */
	    donateDialog = new AlertDialog.Builder(this);
	    donateDialog.setTitle(R.string.pref_donate_title);
	    donateDialog.setMessage(R.string.pref_donate_summary);
	    donateDialog.setNegativeButton(R.string.startLevelDialogCancel, (dialog, which) -> dialog.dismiss());
	    donateDialog.setPositiveButton(R.string.donate_button, (dialog, which) -> {
            String url = getResources().getString(R.string.donation_url);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

		this.findViewById(R.id.clear).setEnabled(this.datasource.getCount()>0);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id==R.id.action_settings){
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		}else if(id == R.id.action_about){
			Intent intent1 = new Intent(this, AboutActivity.class);
			startActivity(intent1);
			return true;
		}else if(id == R.id.action_donate){
			donateDialog.show();
			return true;
		}else if(id == R.id.action_help){
			Intent intent2 = new Intent(this, HelpActivity.class);
			startActivity(intent2);
			return true;
		}else if(id == R.id.action_exit){
			GameState.destroy();
			MainActivity.this.finish();
			return true;
		}else{
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void start() {
		Intent intent = new Intent(this, GameActivity.class);
		Bundle b = new Bundle();
		b.putInt("mode", GameActivity.NEW_GAME); //Your id
		b.putInt("level", startLevel); //Your id
		b.putString("playername", ((TextView)findViewById(R.id.nicknameEditView)).getText().toString()); //Your id
		intent.putExtras(b); //Put your id to your next Intent
		startActivityForResult(intent,SCORE_REQUEST);
		this.findViewById(R.id.clear).setEnabled(true);

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode != SCORE_REQUEST)
			return;
		if(resultCode != RESULT_OK)
			return;

		String playerName = data.getStringExtra(PLAYER_NAME_KEY);
		long score = data.getLongExtra(SCORE_KEY,0);

	    datasource.open();
	    datasource.createScore(score, playerName);
	}

	public void onClickClear(View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.confirm);
		builder.setMessage(
				getResources().getString(R.string.confirm_to_clear)
		);
		builder.setPositiveButton(R.string.OK, (dialogInterface, i) ->
		{
			this.datasource.deleteAllScores();
			Cursor mc;
			mc = datasource.getCursor();
			// Use the SimpleCursorAdapter to show the
			// elements in a ListView
			this.adapter = new SimpleCursorAdapter(
					this,
					R.layout.blockinger_list_item,
					mc,
					new String[] {HighScoreOpenHelper.COLUMN_SCORE, HighScoreOpenHelper.COLUMN_PLAYER_NAME},
					new int[] {R.id.text1, R.id.text2},
					SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
			this.setListAdapter(adapter);
			this.findViewById(R.id.clear).setEnabled(false);
		});
		builder.setNegativeButton(R.string.Cancel,(dialogInterface, i) -> {

		});
		Dialog dialog = builder.create();

		dialog.show();
	}

    public void onClickStart(View view) {
		dialogView = getLayoutInflater().inflate(R.layout.seek_bar_dialog, null);
		leveldialogtext = dialogView.findViewById(R.id.leveldialogleveldisplay);
		leveldialogBar = dialogView.findViewById(R.id.levelseekbar);
		leveldialogBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				leveldialogtext.setText("" + arg1);
				startLevel = arg1;
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
			}
			
		});
		leveldialogBar.setProgress(startLevel);
		leveldialogtext.setText("" + startLevel);
		startLevelDialog.setView(dialogView);
		startLevelDialog.show();
    }

    public void onClickResume(View view) {
		Intent intent = new Intent(this, GameActivity.class);
		Bundle b = new Bundle();
		b.putInt("mode", GameActivity.RESUME_GAME); //Your id
		b.putString("playername", ((TextView)findViewById(R.id.nicknameEditView)).getText().toString()); //Your id
		intent.putExtras(b); //Put your id to your next Intent
		startActivityForResult(intent,SCORE_REQUEST);
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	sound.pause();
    	sound.setInactive(true);
    }

    @Override
    protected void onStop() {
    	super.onStop();
    	sound.pause();
    	sound.setInactive(true);
    	datasource.close();
    }

    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	sound.release();
    	sound = null;
    	datasource.close();
    }

    @Override
    protected void onResume() {
    	super.onResume();
    	sound.setInactive(false);
    	sound.resume();
    	datasource.open();
	    Cursor cursor = datasource.getCursor();
	    adapter.changeCursor(cursor);
	    
	    if(!GameState.isFinished()) {
	    	findViewById(R.id.resumeButton).setEnabled(true);
	    	((Button)findViewById(R.id.resumeButton)).setTextColor(getResources().getColor(R.color.square_error));
	    } else {
	    	findViewById(R.id.resumeButton).setEnabled(false);
	    	((Button)findViewById(R.id.resumeButton)).setTextColor(getResources().getColor(R.color.holo_grey));
	    }
    }

}
