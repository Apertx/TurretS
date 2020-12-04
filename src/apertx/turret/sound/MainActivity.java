package apertx.turret.sound;

import android.app.*;
import android.content.*;
import android.database.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.widget.*;
import apertx.turret.sound.*;
import java.util.*;

public class MainActivity extends Activity{
 final int ID_MENU_FAVS = 10;
 final int ID_MENU_SETTINGS = 11;
 final int ID_MENU_EXIT = 12;
 final int ID_MENU_APP_IN_GP = 13;
 final int ID_MENU_SOURCE_CODE = 14;
 final int ID_MENU_STAT = 15;
 final int ID_MENU_ABOUT = 16;

 final int SOUND_MAX_STREAMS = 8;
 final int SOUND_PRIORITY = 1;
 final int SOUND_RES_ID = 0x7f040000;

 ExpandableListView elv;
 ListView list;
 SharedPreferences settings;

 SoundPool sound;
 byte sound_done;
 boolean fav_mode;
 boolean pick_mode;
 int clicks;
 String favs;
 boolean[] sets;

 public void onCreate(Bundle b0){
  super.onCreate(b0);
  setVolumeControlStream(AudioManager.STREAM_MUSIC);
  settings=getSharedPreferences("data",MODE_PRIVATE);
  clicks=settings.getInt("clicks",0);
  sound=new SoundPool(SOUND_MAX_STREAMS,AudioManager.STREAM_MUSIC,0);

  final int last_quote = getResources().getInteger(R.integer.last_quote);
  sets=new boolean[1];
  sets[0]=settings.getBoolean("animate",false);
  favs=settings.getString("favs",getResources().getString(R.string.fav_default));
  sound_done=0;
  fav_mode=false;
  pick_mode=getIntent().getAction().equals(RingtoneManager.ACTION_RINGTONE_PICKER);

  list=new ListView(this);
  list.setDividerHeight(0);
  list.setBackgroundResource(R.color.bubble_primary);
  elv=new ExpandableListView(this);
  elv.setDividerHeight(0);
  elv.setBackgroundResource(R.color.bubble_primary);
  elv.setAdapter(new Bela());
  setContentView(elv);

  new Thread(new Runnable() {
    @Override public void run(){
     while(sound_done!=last_quote){
      sound.load(MainActivity.this,SOUND_RES_ID+sound_done,SOUND_PRIORITY);
      sound_done+=1;
     }
    }
   }).start();
 }

 class Bela extends BaseExpandableListAdapter{
  Animation anim;
  String[] headers;
  String[] items;
  int[] quote_offset;
  Bela(){
   anim=new AlphaAnimation(0.5f,1.0f);
   anim.setDuration(200);
   headers=getResources().getStringArray(R.array.quote_headers);
   items=getResources().getStringArray(R.array.quote_items);
   quote_offset=getResources().getIntArray(R.array.quote_offset);
  }
  @Override public int getGroupCount(){return headers.length;}
  @Override public int getChildrenCount(int p0){return quote_offset[p0+1]-quote_offset[p0];}
  @Override public Object getGroup(int p0){return headers[p0];}
  @Override public Object getChild(int p0,int p1){return items[quote_offset[p0]+p1];}
  @Override public long getGroupId(int p0){return p0;}
  @Override public long getChildId(int p0,int p1){return quote_offset[p0]+p1;}
  @Override public boolean hasStableIds(){return false;}
  @Override public boolean isChildSelectable(int p0,int p1){return false;}
  @Override public View getGroupView(int p0,boolean p1,View p2,ViewGroup p3){
   if(p2==null) p2=LayoutInflater.from(MainActivity.this).inflate(R.layout.header,null);
   ((TextView)p2.findViewById(R.id.header_text)).setText(headers[p0]);
   return p2;
  }
  @Override public View getChildView(int p0,int p1,boolean p2,View p3,ViewGroup p4){
   TextView tv;
   final int sound_id = quote_offset[p0]+p1;
   if(p3==null) p3=LayoutInflater.from(MainActivity.this).inflate(R.layout.bubble,null);
   tv=p3.findViewById(R.id.bubble_text);
   tv.setText(items[sound_id]);
   tv.setOnClickListener(new OnClickListener() {
     @Override public void onClick(View p5){
      if(pick_mode){
       setResult(RESULT_OK,new Intent().putExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI,Uri.parse(new StringBuilder().append("android.resource://apertx.turret.sound/").append(SOUND_RES_ID+sound_id).toString())));
       finish();
      }else{
       if(sound_id<sound_done)
        sound.play(sound_id+1,1.0f,1.0f,SOUND_PRIORITY,0,1.0f);
       else{
        MediaPlayer mp = MediaPlayer.create(MainActivity.this,SOUND_RES_ID+sound_id);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
          @Override public void onCompletion(MediaPlayer p5){
           p5.release();
          }
         });
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
          @Override public void onPrepared(MediaPlayer p5){
           p5.start();
          }
         });
       }
       clicks+=1;
      }
     }
    });
   final ImageView iv = p3.findViewById(R.id.bubble_fav);
   if(favs.charAt(sound_id)=='a') iv.setImageResource(R.drawable.ic_unfavorite);
   else iv.setImageResource(R.drawable.ic_favorite);
   iv.setOnClickListener(new OnClickListener() {
     @Override public void onClick(View p5){
      char[] chf = favs.toCharArray();
      if(chf[sound_id]=='a'){
       iv.setImageResource(R.drawable.ic_favorite);
       chf[sound_id]='b';
      }else{
       iv.setImageResource(R.drawable.ic_unfavorite);
       chf[sound_id]='a';
      }
      favs=String.valueOf(chf);
     }
    });
   if(pick_mode==false){
    ImageView niv = p3.findViewById(R.id.bubble_notif);
    niv.setImageResource(R.drawable.ic_notification);
    niv.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View p5){
       if(Integer.parseInt(Build.VERSION.SDK)>=23){
        if(Settings.System.canWrite(MainActivity.this))
         new AlertDialog.Builder(MainActivity.this).
          setTitle(R.string.set_sound).
          setItems(R.array.notif,new DialogInterface.OnClickListener() {
           @Override public void onClick(DialogInterface p5,int p6){
            RingtoneManager.setActualDefaultRingtoneUri(MainActivity.this,RingtoneManager.TYPE_RINGTONE<<p6,Uri.parse(new StringBuilder().append("android.resource://apertx.turret.sound/").append(SOUND_RES_ID+sound_id).toString()));
           }
          }).
          show();
        else
         new AlertDialog.Builder(MainActivity.this).
          setMessage(R.string.notif_warn).
          setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface p5,int p6){
            startActivity(new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS));
           }
          }).
          show();
       }else	
        new AlertDialog.Builder(MainActivity.this).
         setTitle(R.string.set_sound).
         setItems(R.array.notif,new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface p5,int p6){
           RingtoneManager.setActualDefaultRingtoneUri(MainActivity.this,RingtoneManager.TYPE_RINGTONE<<p6,Uri.parse(new StringBuilder().append("android.resource://apertx.turret.sound/").append(SOUND_RES_ID+sound_id).toString()));
          }
         }).
         show();
      }
     });
   }
   if(sets[0]) ((LinearLayout)p3.findViewById(R.id.bubble_layout)).startAnimation(anim);
   return p3;
  }
 }

 class Laa implements ListAdapter{
  Animation anim;
  String[] items;
  List<Integer> id_items;
  Laa(){
   anim=new AlphaAnimation(0.5f,1.0f);
   anim.setDuration(200);
   items=getResources().getStringArray(R.array.quote_items);
   id_items=new ArrayList<Integer>();
   for(int i = 0; i<items.length; i+=1)
    if(favs.charAt(i)=='b')
     id_items.add(i);
  }
  @Override public void registerDataSetObserver(DataSetObserver p1){}
  @Override public void unregisterDataSetObserver(DataSetObserver p1){}
  @Override public int getCount(){return id_items.size();}
  @Override public Object getItem(int p0){return null;}
  @Override public long getItemId(int p0){return p0;}
  @Override public boolean hasStableIds(){return false;}
  @Override public View getView(int p0,View p1,ViewGroup p2){
   TextView tv;
   final int sound_id = p0;
   if(p1==null) p1=LayoutInflater.from(MainActivity.this).inflate(R.layout.bubble,null);
   tv=p1.findViewById(R.id.bubble_text);
   tv.setText(items[id_items.get(sound_id)]);
   tv.setOnClickListener(new OnClickListener() {
     @Override public void onClick(View p5){
      if(pick_mode){
       setResult(RESULT_OK,new Intent().putExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI,Uri.parse(new StringBuilder().append("android.resource://apertx.turret.sound/").append(SOUND_RES_ID+sound_id).toString())));
       finish();
      }else{
       if(sound_id<sound_done)
        sound.play(id_items.get(sound_id)+1,1.0f,1.0f,SOUND_PRIORITY,0,1.0f);
       else{
        MediaPlayer mp = MediaPlayer.create(MainActivity.this,SOUND_RES_ID+sound_id);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
          @Override public void onCompletion(MediaPlayer p5){
           p5.release();
          }
         });
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
          @Override public void onPrepared(MediaPlayer p5){
           p5.start();
          }
         });
       }
       clicks+=1;
      }
     }
    });
   final ImageView iv = p1.findViewById(R.id.bubble_fav);
   iv.setImageResource(R.drawable.ic_favorite);
   iv.setOnClickListener(new OnClickListener() {
     @Override public void onClick(View p5){
      char[] chf = favs.toCharArray();
      chf[(int)id_items.get(sound_id)]='a';
      favs=String.valueOf(chf);
      id_items.remove(sound_id);
      list.setAdapter(new Laa());
     }
    });
   if(pick_mode==false){
    ImageView niv = p1.findViewById(R.id.bubble_notif);
    niv.setImageResource(R.drawable.ic_notification);
    niv.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View p5){
       if(Integer.parseInt(Build.VERSION.SDK)>=23){
        if(Settings.System.canWrite(MainActivity.this))
         new AlertDialog.Builder(MainActivity.this).
          setTitle(R.string.set_sound).
          setItems(R.array.notif,new DialogInterface.OnClickListener() {
           @Override public void onClick(DialogInterface p5,int p6){
            RingtoneManager.setActualDefaultRingtoneUri(MainActivity.this,RingtoneManager.TYPE_RINGTONE<<p6,Uri.parse(new StringBuilder().append("android.resource://apertx.turret.sound/").append(SOUND_RES_ID+sound_id).toString()));
           }
          }).
          show();
        else
         new AlertDialog.Builder(MainActivity.this).
          setMessage(R.string.notif_warn).
          setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
           @Override public void onClick(DialogInterface p5,int p6){
            startActivity(new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS));
           }
          }).
          show();
       }else	
        new AlertDialog.Builder(MainActivity.this).
         setTitle(R.string.set_sound).
         setItems(R.array.notif,new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface p5,int p6){
           RingtoneManager.setActualDefaultRingtoneUri(MainActivity.this,RingtoneManager.TYPE_RINGTONE<<p6,Uri.parse(new StringBuilder().append("android.resource://apertx.turret.sound/").append(SOUND_RES_ID+sound_id).toString()));
          }
         }).
         show();
      }
     });
   }
   if(sets[0]) ((LinearLayout)p1.findViewById(R.id.bubble_layout)).startAnimation(anim);
   return p1;
  }
  @Override public int getItemViewType(int p0){return IGNORE_ITEM_VIEW_TYPE;}
  @Override public int getViewTypeCount(){return 1;}
  @Override public boolean isEmpty(){return id_items.isEmpty();}
  @Override public boolean areAllItemsEnabled(){return false;}
  @Override public boolean isEnabled(int p1){return false;}
 }

 @Override public boolean onCreateOptionsMenu(Menu p0){
  p0.add(Menu.NONE,ID_MENU_FAVS,Menu.NONE,getString(R.string.fav));
  p0.add(Menu.NONE,ID_MENU_SETTINGS,Menu.NONE,getString(R.string.settings));
  SubMenu sm = p0.addSubMenu(getString(R.string.help));
  sm.add(Menu.NONE,ID_MENU_APP_IN_GP,Menu.NONE,R.string.app_in_gp);
  sm.add(Menu.NONE,ID_MENU_SOURCE_CODE,Menu.NONE,R.string.source_code);
  sm.add(Menu.NONE,ID_MENU_STAT,Menu.NONE,R.string.stat);
  sm.add(Menu.NONE,ID_MENU_ABOUT,Menu.NONE,R.string.about);
  p0.add(Menu.NONE,ID_MENU_EXIT,Menu.NONE,R.string.exit);
  return super.onCreateOptionsMenu(p0);
 }

 @Override public boolean onOptionsItemSelected(MenuItem p0){
  switch(p0.getItemId()){
   case ID_MENU_FAVS:
    if(fav_mode==false){
     fav_mode=true;
     list.setAdapter(new Laa());
     setContentView(list);
    }
    break;
   case ID_MENU_SETTINGS:
    new AlertDialog.Builder(this).
     setTitle(R.string.settings).
     setMultiChoiceItems(R.array.sets,sets,new AlertDialog.OnMultiChoiceClickListener() {
      @Override public void onClick(DialogInterface p1,int p2,boolean p3){
       sets[p2]=p3;
      }
     }).
     show();
    break;
   case ID_MENU_EXIT:
    finish();
    break;
   case ID_MENU_APP_IN_GP:
    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id=apertx.turret.sound")));
    break;
   case ID_MENU_SOURCE_CODE:
    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://github.com/Apertx/TurretS")));
    break;
   case ID_MENU_STAT:
    new AlertDialog.Builder(this).
     setTitle(R.string.stat).
     setMessage(new StringBuilder().append(getString(R.string.stat_message)).append(": ").append(clicks).toString()).
     setPositiveButton(R.string.ok,null).
     show();
    break;
   case ID_MENU_ABOUT:
    new AlertDialog.Builder(this).
     setTitle(R.string.about).
     setMessage(R.string.about_message).
     setPositiveButton(R.string.ok,null).
     show();
    break;
  }
  return super.onOptionsItemSelected(p0);
 }

 @Override protected void onSaveInstanceState(Bundle b0){
  b0.putInt("clicks",clicks);
  b0.putBooleanArray("sets",sets);
  super.onSaveInstanceState(b0);
 }

 @Override protected void onRestoreInstanceState(Bundle b0){
  super.onRestoreInstanceState(b0);
  clicks=b0.getInt("clicks");
  sets=b0.getBooleanArray("sets");
 }

 @Override public void onBackPressed(){
  if(fav_mode){
   fav_mode=false;
   setContentView(elv);
  }else super.onBackPressed();
 }

 @Override protected void onDestroy(){
  super.onDestroy();
  sound.release();
  settings.edit().
   putInt("clicks",clicks).
   putBoolean("animate",sets[0]).
   putString("favs",favs).
   commit();
 }
}
