package apertx.turret.sound;

import android.app.*;
import android.content.*;
import android.os.*;
import android.preference.*;
import android.view.*;
import android.widget.*;
import android.widget.ExpandableListView.*;
import java.util.*;
import android.media.*;

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

	static final byte[] quote_offset = {0,3,14,24,30,40,47,54,59,64,69,76,84,90,93,94,97,102};

	SharedPreferences settings;
	SoundPool sound;
	byte sound_done;
	int clicks;

	public void onCreate(Bundle b0) {
		super.onCreate(b0);
		settings = getSharedPreferences("data", MODE_PRIVATE);
		CharSequence[] po = {
			"[Turret fire]",
			"[Turret fire]",
			"[Turret fire]"
		};
		CharSequence[] p1 = {
			"Wheee.",
			"Wheeeeeee.",
			"I'm afraid of heights.",
			"Noooo.",
			"See you soon.",
			"I'm scared.",
			"I'm scared.",
			"Hooray.",
			"Glorious freedom.",
			"I'm flying.",
			"Goodbye."
		};
		CharSequence[] p2 = {
			"Ow.",
			"Oww.",
			"Owwww.",
			"It burns.",
			"I'm on fire.",
			"I'm on fire owww.",
			"Please stop.",
			"You've made your point.",
			"OK you win.",
			"This is not good."
		};
		CharSequence[] p3 = {
			"Ooohhh.",
			"Owwww.",
			"Can't...breathe.",
			"Excuse me you're squishing me.",
			"Um, hello.",
			"Help! Being squished."
		};
		CharSequence[] p4 = {
			"Hello?",
			"Hello.",
			"Target acquired.",
			"Whyyyy.",
			"I did everything you askedddd.",
			"I don't understandddd.",
			"I'm fineeee",
			"Ahhhh.",
			"Wheee oh noooo.",
			"Whyyy."
		};
		CharSequence[] p5 = {
			"[Activation sound]",
			"[Alarm sound]",
			"[Alert sound]",
			"[Deploy sound]",
			"[Death sound]",
			"[Ping sound]",
			"[Retract sound]"
		};
		CharSequence[] p6 = {
			"Hi.",
			"Target acquired.",
			"Firing.",
			"Hello friend.",
			"Gotcha.",
			"There you are.",
			"I see you."
		};
		CharSequence[] p7 = {
			"Hellooo.",
			"Searching.",
			"Sentry mode activated.",
			"Is anyone there?",
			"Could you come over here?"
		};
		CharSequence[] p8 = {
			"Coming through.",
			"Excuse me.",
			"Sorry.",
			"My fault.",
			"Ohh."
		};
		CharSequence[] p9 = {
			"Hello.",
			"Deploying.",
			"Activated.",
			"There you are.",
			"Who's there?"
		};
		CharSequence[] p10 = {
			"Ahahaa.",
			"Critical error.",
			"Shutting down.",
			"I don't blame you.",
			"I don't hate you.",
			"Whyyyy.",
			"No hard feelings."
		};
		CharSequence[] p11 = {
			"Hey.",
			"Hey hey hey.",
			"Put me down.",
			"Who are you?",
			"Hey.",
			"Please put me down.",
			"Help.",
			"Uh oh."
		};
		CharSequence[] p12 = {
			"Goodbay.",
			"Sleep mode activated.",
			"Hibernating.",
			"Goodnight.",
			"Resting.",
			"Nap time."
		};
		CharSequence[] p13 = {
			"Are you still there?",
			"Target lost.",
			"Searching."
		};
		CharSequence[] p14 = {
			"aiaiaiaiaiai."
		};
		CharSequence[] p15 = {
			"Hey, it's me!",
			"Don't shoot!",
			"Stop shooting!"
		};
		CharSequence[] p16 = {
			"Aah.",
			"Self test error.",
			"Malfunctioning.",
			"Aaaah.",
			"Ow ow ow ow owww."
		};
		CharSequence[] p17 = {
			"But I need to protect the humans."
		};
		sound_done = 0;
		ArrayList<ArrayList<Map<String, CharSequence>>> labelList = new ArrayList<>();
		ArrayList<Map<String, CharSequence>> headerList = new ArrayList<>();
		ArrayList<Map<String, CharSequence>> itemList = new ArrayList<>();
		Map<String, CharSequence> map;

		for (CharSequence label: getResources().getStringArray(R.array.quote_headers)) {
			map = new HashMap<>();
			map.put("header", label);
			headerList.add(map);
		}
		for (CharSequence label: po) {
			map = new HashMap<>();
			map.put("label", label);
			itemList.add(map);
		}
		labelList.add(itemList);

		ExpandableListView elv = new ExpandableListView(this);
		SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(this, headerList, android.R.layout.simple_expandable_list_item_1, new String[] {"header"}, new int[] {android.R.id.text1}, labelList, android.R.layout.simple_list_item_1, new String[] {"label"}, new int[] {android.R.id.text1});
		elv.setAdapter(adapter);

		sound = new SoundPool(SOUND_MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
		elv.setOnChildClickListener(new OnChildClickListener() {
				@Override
				public boolean onChildClick(ExpandableListView p0, View p1, int p2, int p3, long p4) {
					int sound_id = quote_offset[p2] + p3;
					if (sound_id < sound_done)
						sound.play(sound_id + 1, 1, 1, 1, 0, SOUND_PRIORITY);
					else {
						MediaPlayer mp= MediaPlayer.create(MainActivity.this, SOUND_RES_ID + sound_id);
						mp.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener(){
								@Override
								public void onSeekComplete(MediaPlayer p5) {
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
				@Override
				public void run() {
					for (byte i = 0; i < 103; i++)
						sound.load(MainActivity.this, SOUND_RES_ID + i, 0);
				}
			}).start();
		sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
				@Override
				public void onLoadComplete(SoundPool p0, int p1, int p2) {
					if (p2 == 0)
						sound_done += 1;
				}
			});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, ID_MENU_SETTINGS, Menu.NONE, getString(R.string.settings));
		SubMenu sm = menu.addSubMenu(getString(R.string.help));
		sm.add(Menu.NONE, ID_MENU_APP_IN_GP, Menu.NONE, getString(R.string.app_in_gp));
		sm.add(Menu.NONE, ID_MENU_SOURCE_CODE, Menu.NONE, getString(R.string.source_code));
		sm.add(Menu.NONE, ID_MENU_STAT, Menu.NONE, getString(R.string.stat));
		sm.add(Menu.NONE, ID_MENU_ABOUT, Menu.NONE, getString(R.string.about));
		menu.add(Menu.NONE, ID_MENU_EXIT, Menu.NONE, getString(R.string.exit));
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
					setTitle(getString(R.string.stat).
					setMessage(new StringBuilder().append("").append(": ").append(clicks).toString()).
					setPositiveButton(R.string.ok, null).
					show();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onSaveInstanceState(Bundle b0) {
		b0.putInt("clicks", clicks);
		super.onSaveInstanceState(b0);
	}

	@Override
	protected void onRestoreInstanceState(Bundle b0) {
		super.onRestoreInstanceState(b0);
		clicks = b0.getInt("clicks");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		sound.release();
		settings.edit().putInt("clicks", clicks).commit();
	}
}
