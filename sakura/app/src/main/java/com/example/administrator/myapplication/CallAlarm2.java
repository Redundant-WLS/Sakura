package com.example.administrator.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CallAlarm2 extends BroadcastReceiver {
	String id ;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Log.i("intent.getStringExtra(action_id)",intent.getStringExtra("action_id")+".");
		Log.i("intent.getStringExtra(action_id1)",intent.getStringExtra("action_id1")+".");
		Log.i("intent.getStringExtra(action_id2)",intent.getStringExtra("action_id2")+".");
		id = intent.getStringExtra("action_id2");
		Log.i("id===================",id+".");
		Intent i = new Intent(context,Alarmalert2.class);
		i.putExtra("class_id", id);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}}
