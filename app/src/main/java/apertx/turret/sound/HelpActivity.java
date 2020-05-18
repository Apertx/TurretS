package apertx.turret.sound;
import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.preference.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;

public class HelpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("light", true) == false)
			setTheme(android.R.style.Theme_Material);
		CharSequence[] credit = {"App in Play market", "Source code", "About Turret soundboard", "Translation support"};
		super.onCreate(savedInstanceState);
		ListView list = new ListView(this);
		list.setAdapter(new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1, credit));
		list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
					switch (p3) {
						case 0:
							startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=apertx.turret.sound")));
							break;
						case 1:
							startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Apertx/TurretS")));
							break;
						case 2:
							new AlertDialog.Builder(HelpActivity.this).setTitle("About Turret soundboard").
								setMessage("This application is designed for fans of the game Portal 2. If you have suggestions for improving the application, please write in the reviews, I will try to add new features to the application").
								setPositiveButton("OK", null).show();
								break;
						case 3:
							Toast.makeText(HelpActivity.this, "Coming soon...", 0).show();
							break;
					}
				}
			});
		setContentView(list);
	}
}
