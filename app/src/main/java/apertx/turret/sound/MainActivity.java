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

public class MainActivity extends Activity {
	static final int ID_MENU_SETTINGS = 0;
	static final int ID_MENU_EXIT = 1;
	static final int ID_MENU_APP_IN_GP = 2;
	static final int ID_MENU_SOURCE_CODE = 3;
	static final int ID_MENU_STAT = 4;
	static final int ID_MENU_ABOUT = 5;

	static final int SOUND_MAX_STREAMS = 8;
	static final int SOUND_PRIORITY = 1;
	static final int SOUND_RES_ID = 0x7f040000;

	SharedPreferences settings;
	SoundPool sound;
	byte sound_done;
	int clicks;

	public void onCreate(Bundle b0) {
		super.onCreate(b0);
		settings = getSharedPreferences("data", MODE_PRIVATE);
		clicks = settings.getInt("clicks", 0);
		sound_done = 0;

		final String[] headers = getResources().getStringArray(R.array.quote_headers);
		final String[] items = getResources().getStringArray(R.array.quote_items);
		final byte[] quote_offset = {0,3,14,24,30,40,47,54,59,64,69,76,84,90,93,94,97,102,103};
		ArrayList<ArrayList<Map<String, String>>> labelList = new ArrayList<>();
		ArrayList<Map<String, String>> headerList;
		ArrayList<Map<String, String>> itemList;
		Map<String, String> map;

		headerList = new ArrayList<Map<String, String>>();
		for (byte j = 0; j < 18; j += 1) {
			map = new HashMap<String, String>();
			map.put("header", headers[j]);
			headerList.add(map);
		}
		byte i = 0;
		for (byte offset = 1; offset < 19; offset += 1) {
			itemList = new ArrayList<Map<String, String>>();
			for (; i < quote_offset[offset]; i += 1) {
				map = new HashMap<String, String>();
				map.put("item", items[i]);
				itemList.add(map);
			}
			labelList.add(itemList);
		}

		ExpandableListView elv = new ExpandableListView(this);
		elv.setDividerHeight(0);
		BaseExpandableListAdapter bela = new BaseExpandableListAdapter() {
			@Override public int getGroupCount() {
				return headers.length;
			}
			@Override public int getChildrenCount(int p0) {
				return quote_offset[p0 + 1] - quote_offset[p0];
			}
			@Override public Object getGroup(int p0) {
				return headers[p0];
			}
			@Override public Object getChild(int p0, int p1) {
				return items[quote_offset[p0] + p1];
			}
			@Override public long getGroupId(int p0) {
				return p0;
			}
			@Override public long getChildId(int p0, int p1) {
				return quote_offset[p0] + p1;
			}
			@Override public boolean hasStableIds() {
				return false;
			}
			@Override public View getGroupView(int p0, boolean p1, View p2, ViewGroup p3) {
				if (p2 == null) p2 = LayoutInflater.from(MainActivity.this).inflate(R.layout.bubble, null);
				TextView tv = p2.findViewById(R.id.bubble_text);
				tv.setText(headers[p0]);
				return p2;
			}
			@Override public View getChildView(int p0, int p1, boolean p2, View p3, ViewGroup p4) {
				if (p3 == null) p3 = LayoutInflater.from(MainActivity.this).inflate(R.layout.bubble, null);
				TextView tv = p3.findViewById(R.id.bubble_text);
				tv.setText(items[quote_offset[p0] + p1]);
				tv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f));
				return p3;
			}
			@Override public boolean isChildSelectable(int p0, int p1) {
				return true;
			}
		};
		elv.setAdapter(bela);

		sound = new SoundPool(SOUND_MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
		elv.setOnChildClickListener(new OnChildClickListener() {
				@Override public boolean onChildClick(ExpandableListView p0, View p1, int p2, int p3, long p4) {
					int sound_id = quote_offset[p2] + p3;
					if (sound_id < sound_done)
						sound.play(sound_id + 1, 1.0f, 1.0f, SOUND_PRIORITY, 0, 1.0f);
					else {
						MediaPlayer mp= MediaPlayer.create(MainActivity.this, SOUND_RES_ID + sound_id);
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
								@Override
								public void onCompletion(MediaPlayer p5) {
									p5.release();
								}
							});
						mp.start();
					}
					clicks++;
					return false;
				}
			});
		setContentView(elv);

		new Thread(new Runnable() {
				@Override public void run() {
					for (byte i = 0; i < 103; i++)
						sound.load(MainActivity.this, SOUND_RES_ID + i, SOUND_PRIORITY);
				}
			}).start();
		sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
				@Override
				public void onLoadComplete(SoundPool p0, int p1, int p2) {
					if (p2 == 0)
						sound_done += 1;
					else
						finish();
				}
			});
	}

	@Override public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, ID_MENU_SETTINGS, Menu.NONE, getString(R.string.settings));
		SubMenu sm = menu.addSubMenu(getString(R.string.help));
		sm.add(Menu.NONE, ID_MENU_APP_IN_GP, Menu.NONE, getString(R.string.app_in_gp));
		sm.add(Menu.NONE, ID_MENU_SOURCE_CODE, Menu.NONE, getString(R.string.source_code));
		sm.add(Menu.NONE, ID_MENU_STAT, Menu.NONE, getString(R.string.stat));
		sm.add(Menu.NONE, ID_MENU_ABOUT, Menu.NONE, getString(R.string.about));
		menu.add(Menu.NONE, ID_MENU_EXIT, Menu.NONE, getString(R.string.exit));
		return super.onCreateOptionsMenu(menu);
	}

	@Override public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case ID_MENU_SETTINGS:
				
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
					setTitle(getString(R.string.stat)).
					setMessage(new StringBuilder().append(getString(R.string.stat_message)).append(": ").append(clicks).toString()).
					setPositiveButton(R.string.ok, null).
					show();
				break;
			case ID_MENU_ABOUT:
				new AlertDialog.Builder(this).
					setTitle(getString(R.string.about)).
					setMessage(getString(R.string.about_message)).
					setPositiveButton(R.string.ok, null).
					show();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override protected void onSaveInstanceState(Bundle b0) {
		b0.putInt("clicks", clicks);
		super.onSaveInstanceState(b0);
	}

	@Override protected void onRestoreInstanceState(Bundle b0) {
		super.onRestoreInstanceState(b0);
		clicks = b0.getInt("clicks");
	}

	@Override protected void onDestroy() {
		super.onDestroy();
		sound.release();
		settings.edit().putInt("clicks", clicks).commit();
	}
}
