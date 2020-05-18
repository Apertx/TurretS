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
	SoundPool sound;
	boolean[] done;

	public void onCreate(Bundle savedInstanceState) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		if (settings.getBoolean("light", true) == false)
			setTheme(android.R.style.Theme_Material);
		CharSequence[] header = {
			"Turret fire",
			"Turret launched into the air",
			"Turrets being zapped by tre Thermal Discouragement Beam",
			"Turret being smashed",
			"Turret factory",
			"Turret sound effects",
			"Turret found its target",
			"Turret searching",
			"Colliding Turrets",
			"Turrets deploying",
			"Turret deaths",
			"Turrets being picked up",
			"Turret deactivation",
			"Turret lost its target",
			"Turret being emancipated/fizzled",
			"Turrets being shot by other Turrets",
			"Turrets being tripped over",
			"Final chamber of the Co-op Course Mobility Gels"
		};
		CharSequence[] p0 = {
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
		final byte[] offset = {0, 3, 14, 24, 30, 40, 47, 54, 59, 64, 69, 76, 84, 90, 93, 94, 97, 102};
		done = new boolean[103];
		super.onCreate(savedInstanceState);
		ArrayList<ArrayList<Map<String, CharSequence>>> labelList = new ArrayList<>();
		ArrayList<Map<String, CharSequence>> headerList = new ArrayList<>();
		ArrayList<Map<String, CharSequence>> itemList = new ArrayList<>();
		Map<String, CharSequence> map;

		for (CharSequence label : header) {
			map = new HashMap<>();
			map.put("header", label);
			headerList.add(map);
		}
		for (CharSequence label : p0) {
			map = new HashMap<>();
			map.put("label", label);
			itemList.add(map);
		}
		labelList.add(itemList);
		itemList = new ArrayList<>();
		for (CharSequence label : p1) {
			map = new HashMap<>();
			map.put("label", label);
			itemList.add(map);
		}
		labelList.add(itemList);
		itemList = new ArrayList<>();
		for (CharSequence label : p2) {
			map = new HashMap<>();
			map.put("label", label);
			itemList.add(map);
		}
		labelList.add(itemList);
		itemList = new ArrayList<>();
		for (CharSequence label : p3) {
			map = new HashMap<>();
			map.put("label", label);
			itemList.add(map);
		}
		labelList.add(itemList);
		itemList = new ArrayList<>();
		for (CharSequence label : p4) {
			map = new HashMap<>();
			map.put("label", label);
			itemList.add(map);
		}
		labelList.add(itemList);
		itemList = new ArrayList<>();
		for (CharSequence label : p5) {
			map = new HashMap<>();
			map.put("label", label);
			itemList.add(map);
		}
		labelList.add(itemList);
		itemList = new ArrayList<>();
		for (CharSequence label : p6) {
			map = new HashMap<>();
			map.put("label", label);
			itemList.add(map);
		}
		labelList.add(itemList);
		itemList = new ArrayList<>();
		for (CharSequence label : p7) {
			map = new HashMap<>();
			map.put("label", label);
			itemList.add(map);
		}
		labelList.add(itemList);
		itemList = new ArrayList<>();
		for (CharSequence label : p8) {
			map = new HashMap<>();
			map.put("label", label);
			itemList.add(map);
		}
		labelList.add(itemList);
		itemList = new ArrayList<>();
		for (CharSequence label : p9) {
			map = new HashMap<>();
			map.put("label", label);
			itemList.add(map);
		}
		labelList.add(itemList);
		itemList = new ArrayList<>();
		for (CharSequence label : p10) {
			map = new HashMap<>();
			map.put("label", label);
			itemList.add(map);
		}
		labelList.add(itemList);
		itemList = new ArrayList<>();
		for (CharSequence label : p11) {
			map = new HashMap<>();
			map.put("label", label);
			itemList.add(map);
		}
		labelList.add(itemList);
		itemList = new ArrayList<>();
		for (CharSequence label : p12) {
			map = new HashMap<>();
			map.put("label", label);
			itemList.add(map);
		}
		labelList.add(itemList);
		itemList = new ArrayList<>();
		for (CharSequence label : p13) {
			map = new HashMap<>();
			map.put("label", label);
			itemList.add(map);
		}
		labelList.add(itemList);
		itemList = new ArrayList<>();
		for (CharSequence label : p14) {
			map = new HashMap<>();
			map.put("label", label);
			itemList.add(map);
		}
		labelList.add(itemList);
		itemList = new ArrayList<>();
		for (CharSequence label : p15) {
			map = new HashMap<>();
			map.put("label", label);
			itemList.add(map);
		}
		labelList.add(itemList);
		itemList = new ArrayList<>();
		for (CharSequence label : p16) {
			map = new HashMap<>();
			map.put("label", label);
			itemList.add(map);
		}
		labelList.add(itemList);
		itemList = new ArrayList<>();
		for (CharSequence label : p17) {
			map = new HashMap<>();
			map.put("label", label);
			itemList.add(map);
		}
		labelList.add(itemList);

		sound = new SoundPool(4, 3, 0);
		ExpandableListView elv = new ExpandableListView(this);
		SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(this,
																			  headerList, android.R.layout.simple_expandable_list_item_1, new String[] {"header"}, new int[] {android.R.id.text1},
																			  labelList, android.R.layout.simple_list_item_1, new String[] {"label"}, new int[] {android.R.id.text1});
		elv.setAdapter(adapter);
		elv.setOnChildClickListener(new OnChildClickListener() {
				@Override
				public boolean onChildClick(ExpandableListView p1, View p2, int p3, int p4, long p5) {
					int p6 = offset[p3] + p4;
					if (done[p6])
						sound.play(p6 + 1, 1, 1, 1, 0, 1);
					else {
						MediaPlayer mp= MediaPlayer.create(MainActivity.this, 0x7f040000 + p6);
						mp.start();
					}
					return false;
				}
			});
		setContentView(elv);
		new Thread(new Runnable() {
				@Override
				public void run() {
					for (byte i = 0; i < 103; i++)
						sound.load(MainActivity.this, 0x7f040000 + i, 0);
				}
			}).start();
		sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
				@Override
				public void onLoadComplete(SoundPool p1, int p2, int p3) {
					if (p3 == 0)
						done[p2 - 1] = true;
					else
						Toast.makeText(MainActivity.this, "Error while loading", 0).show();
				}
			});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, 10, Menu.NONE, "Settings");
		menu.add(Menu.NONE, 11, Menu.NONE, "Help");
		menu.add(Menu.NONE, 12, Menu.NONE, "Exit");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case 10:
				startActivity(new Intent(MainActivity.this, SettingsActivity.class));
				break;
			case 11:
				startActivity(new Intent(MainActivity.this, HelpActivity.class));
				break;
			case 12:
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		sound.release();
		sound = null;
	}
}
