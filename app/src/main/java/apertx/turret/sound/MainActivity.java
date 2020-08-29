package apertx.turret.sound;

import android.app.*;
import android.content.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.ExpandableListView.*;
import java.util.*;
import android.view.animation.*;

public class MainActivity extends Activity {
	static final int ID_MENU_SETTINGS = 10;
	static final int ID_MENU_EXIT = 11;
	static final int ID_MENU_APP_IN_GP = 12;
	static final int ID_MENU_SOURCE_CODE = 13;
	static final int ID_MENU_STAT = 14;
	static final int ID_MENU_ABOUT = 15;

	static final int SOUND_MAX_STREAMS = 8;
	static final int SOUND_PRIORITY = 1;
	static final int SOUND_RES_ID = 0x7f040000;

	ExpandableListView elv;
	SharedPreferences settings;

	SoundPool sound;
	byte sound_done;
	int clicks;
	boolean[] sets;

	public void onCreate(Bundle b0) {
		super.onCreate(b0);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		settings = getSharedPreferences("data", MODE_PRIVATE);
		clicks = settings.getInt("clicks", 0);
		sound = new SoundPool(SOUND_MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
		sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
				@Override public void onLoadComplete(SoundPool p0, int p1, int p2) {
					if (p2 == 0) sound_done += 1;
					else finish();
				}
			});

		final int last_quote = getResources().getInteger(R.integer.last_quote);
		sets = new boolean[1];
		sets[0] = settings.getBoolean("animate", false);
		sound_done = 0;

		elv = new ExpandableListView(this);
		elv.setDividerHeight(0);
		elv.setBackgroundResource(R.color.bubble_primary);
		elv.setAdapter(new Bela());
		setContentView(elv);

		new Thread(new Runnable() {
				@Override public void run() {
					for (int i = 0; i < last_quote; i++)
						sound.load(MainActivity.this, SOUND_RES_ID + i, SOUND_PRIORITY);
				}
			}).start();
	}

	class Bela extends BaseExpandableListAdapter {
		Animation anim;
		String[] headers;
		String[] items;
		int[] quote_offset;
		Bela() {
			anim = new AlphaAnimation(0.5f, 1.0f);
			anim.setDuration(200);
			headers = getResources().getStringArray(R.array.quote_headers);
			items = getResources().getStringArray(R.array.quote_items);
			quote_offset = getResources().getIntArray(R.array.quote_offset);
		}
		@Override public int getGroupCount() {return headers.length;}
		@Override public int getChildrenCount(int p0) {return quote_offset[p0 + 1] - quote_offset[p0];}
		@Override public Object getGroup(int p0) {return headers[p0];}
		@Override public Object getChild(int p0, int p1) {return items[quote_offset[p0] + p1];}
		@Override public long getGroupId(int p0) {return p0;}
		@Override public long getChildId(int p0, int p1) {return quote_offset[p0] + p1;}
		@Override public boolean hasStableIds() {return false;}
		@Override public boolean isChildSelectable(int p0, int p1) {return false;}
		@Override public View getGroupView(int p0, boolean p1, View p2, ViewGroup p3) {
			if (p2 == null) p2 = LayoutInflater.from(MainActivity.this).inflate(R.layout.header, null);
			((TextView)p2.findViewById(R.id.header_text)).setText(headers[p0]);
			return p2;
		}
		@Override public View getChildView(int p0, int p1, boolean p2, View p3, ViewGroup p4) {
			TextView tv;
			final int sound_id = quote_offset[p0] + p1;
			if (p3 == null) p3 = LayoutInflater.from(MainActivity.this).inflate(R.layout.bubble, null);
			tv = p3.findViewById(R.id.bubble_text);
			tv.setText(items[quote_offset[p0] + p1]);
			tv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View p5) {
						if (getIntent().getAction() == RingtoneManager.ACTION_RINGTONE_PICKER) {
							setResult(RESULT_OK, new Intent().putExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI, Uri.parse(new StringBuilder().append("android.resource://apertx.turret.sound/").append(SOUND_RES_ID + sound_id).toString())));
							finish();
						} else {
							if (sound_id < sound_done)
								sound.play(sound_id + 1, 1.0f, 1.0f, SOUND_PRIORITY, 0, 1.0f);
							else {
								MediaPlayer mp = MediaPlayer.create(MainActivity.this, SOUND_RES_ID + sound_id);
								mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
										@Override public void onCompletion(MediaPlayer p5) {
											p5.release();
										}
									});
								mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
										@Override public void onPrepared(MediaPlayer p5) {
											p5.start();
										}
									});
							}
							clicks += 1;
						}
					}
				});
			if (sets[0]) ((LinearLayout)p3.findViewById(R.id.bubble_layout)).startAnimation(anim);
			return p3;
		}
	}

	@Override public boolean onCreateOptionsMenu(Menu p0) {
		p0.add(Menu.NONE, ID_MENU_SETTINGS, Menu.NONE, getString(R.string.settings));
		SubMenu sm = p0.addSubMenu(getString(R.string.help));
		sm.add(Menu.NONE, ID_MENU_APP_IN_GP, Menu.NONE, R.string.app_in_gp);
		sm.add(Menu.NONE, ID_MENU_SOURCE_CODE, Menu.NONE, R.string.source_code);
		sm.add(Menu.NONE, ID_MENU_STAT, Menu.NONE, R.string.stat);
		sm.add(Menu.NONE, ID_MENU_ABOUT, Menu.NONE, R.string.about);
		p0.add(Menu.NONE, ID_MENU_EXIT, Menu.NONE, R.string.exit);
		return super.onCreateOptionsMenu(p0);
	}

	@Override public boolean onOptionsItemSelected(MenuItem p0) {
		switch (p0.getItemId()) {
			case ID_MENU_SETTINGS:
				new AlertDialog.Builder(this).
					setTitle(R.string.settings).
					setMultiChoiceItems(R.array.sets, sets, new AlertDialog.OnMultiChoiceClickListener() {
						@Override public void onClick(DialogInterface p1, int p2, boolean p3) {
							sets[p2] = p3;
						}
					}).
					show();
				break;
			case ID_MENU_EXIT:
				finish();
				break;
			case ID_MENU_APP_IN_GP:
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=apertx.turret.sound")));
				break;
			case ID_MENU_SOURCE_CODE:
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Apertx/TurretS")));
				break;
			case ID_MENU_STAT:
				new AlertDialog.Builder(this).
					setTitle(R.string.stat).
					setMessage(new StringBuilder().append(getString(R.string.stat_message)).append(": ").append(clicks).toString()).
					setPositiveButton(R.string.ok, null).
					show();
				break;
			case ID_MENU_ABOUT:
				new AlertDialog.Builder(this).
					setTitle(R.string.about).
					setMessage(R.string.about_message).
					setPositiveButton(R.string.ok, null).
					show();
				break;
		}
		return super.onOptionsItemSelected(p0);
	}

	@Override protected void onSaveInstanceState(Bundle b0) {
		b0.putInt("clicks", clicks);
		b0.putBooleanArray("sets", sets);
		super.onSaveInstanceState(b0);
	}

	@Override protected void onRestoreInstanceState(Bundle b0) {
		super.onRestoreInstanceState(b0);
		clicks = b0.getInt("clicks");
		sets = b0.getBooleanArray("sets");
	}

	@Override protected void onDestroy() {
		super.onDestroy();
		sound.release();
		settings.edit().
			putInt("clicks", clicks).
			putBoolean("animate", sets[0]).
			commit();
	}
}
