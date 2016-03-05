package com.example.administrator.myapplication;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ljk.R;

public class Alarmalert extends Activity {

	private MediaPlayer mp;
	private Vibrator v;
	Action action;
	AudioManager am;
	private ActivityManager am1;
	
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		Log.i("AlarmAlert","ljk");	
		am1 = ActivityManager.getInstance();
		am1.addActivity(this);
		
		v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		mp = new MediaPlayer();
        am = (AudioManager)getSystemService(AUDIO_SERVICE);
		
        Log.i("Alarm", Environment.getExternalStorageDirectory()+".");
		Intent intent = getIntent();
//		String rings = intent.getStringExtra(Environment.getExternalStorageDirectory()+"/a.mp3");
		String str_id = intent.getStringExtra("action_id");
		int id = Integer.parseInt(str_id);
		Log.i("Alarmalert",id+".");
		DBHelper helper = new DBHelper(Alarmalert.this);
		helper.openDatabase();
		action = helper.query(id);
		Log.i("AlarmalertAction_id1",action.num_id+"");	
		Log.i("AlarmalertAction_flag1",action.flag+"");	
		action.flag = Integer.valueOf(1);
		Log.i("AlarmalertAction_id2",action.num_id+"");	
		Log.i("AlarmalertAction_flag2",action.flag+"");	
		helper.modify(action);
		Log.i("actiontitle",action.actiontitle);

	try {
        mp.setDataSource(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));


			new AlertDialog.Builder(this)
			.setTitle(action.time)
			.setMessage(action.actiontitle)
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
					mp.stop();
					v.cancel();
					finish();
				}
				
			}).show();
			
			if(action.remidtype.equals("铃声")){
				mp.setAudioStreamType(AudioManager.STREAM_ALARM);
				mp.setLooping(true);
				mp.prepare();
				mp.start();}
			if(action.remidtype.equals("振动")){
				v.vibrate(new long[]{1000,3000,500,2000}, 2);
			}
			if(action.remidtype.equals("振动和铃声")){
				v.vibrate(new long[]{1000,3000,500,2000}, 2);
				mp.setAudioStreamType(AudioManager.STREAM_ALARM);
				mp.setLooping(true);
				mp.prepare();
				mp.start();
			}
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
			mp.stop();
		}
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "关于");
		menu.add(0, 2, 2, "退出");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			AlertDialog.Builder adb = new Builder(Alarmalert.this);
			adb.setTitle("关于我们");
			adb.setMessage("xxxx");
			adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(Alarmalert.this, "Thanks",
							Toast.LENGTH_SHORT).show();
				}
			});
			adb.show();
			break;
		case 2:
			AlertDialog.Builder adb1 = new Builder(Alarmalert.this);
			adb1.setTitle("消息");
			adb1.setMessage("真的要退出吗？");
			adb1.setPositiveButton("确定", new  DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					am1.exitAllProgress();
				}
			});
			adb1.setNegativeButton("取消", null);
			adb1.show();
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
}
