package com.example.administrator.myapplication;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ljk.R;

public class Main extends Activity {
	private ActivityManager am1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_listview);

		am1 = ActivityManager.getInstance();
		am1.addActivity(this);

		// 绑定Layout里面的ListView
		ListView list = (ListView) findViewById(R.id.ListView01);
		// 生成动态数组，加入数据
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("ItemImage", R.drawable.classtable);// 图像资源的ID
		map1.put("ItemTitle", "未处理事件 ");
		listItem.add(map1);

		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("ItemImage", R.drawable.action_list);// 图像资源的ID
		map2.put("ItemTitle", "课程表 ");
		listItem.add(map2);

		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("ItemImage", R.drawable.add_action);// 图像资源的ID
		map3.put("ItemTitle", "添加事件 ");
		listItem.add(map3);

		HashMap<String, Object> map4 = new HashMap<String, Object>();
		map4.put("ItemImage", R.drawable.done_action);// 图像资源的ID
		map4.put("ItemTitle", " 历史记录 ");
		listItem.add(map4);

		HashMap<String, Object> map5 = new HashMap<String, Object>();
		map5.put("ItemImage", R.drawable.calendar);// 图像资源的ID
		map5.put("ItemTitle", "       日历 ");
		listItem.add(map5);

		HashMap<String, Object> map6 = new HashMap<String, Object>();
		map6.put("ItemImage", R.drawable.setting);// 图像资源的ID
		map6.put("ItemTitle", "       设置 ");
		listItem.add(map6);

		SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,
				R.layout.main_listview_item,
				new String[] { "ItemImage", "ItemTitle" },
				new int[] { R.id.ItemImage, R.id.ItemTitle });
		list.setAdapter(listItemAdapter);

		// 添加点击
		list.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("deprecation")
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {

				Intent intent = new Intent();

				if(arg2==0){
					intent.setClass(Main.this,DeuTypeList.class);
					Main.this.startActivity(intent);
				}
				else if(arg2==1){
					intent.setClass(Main.this,Schedual.class);
					Main.this.startActivity(intent);
				}
				else if(arg2==2){
					intent.setClass(Main.this,Add.class);
					Main.this.startActivity(intent);
				}
				else if(arg2==3){
					intent.setClass(Main.this,History.class);
					Main.this.startActivity(intent);
				}
				else if(arg2==4){

					try {
						Intent i = new Intent();
						ComponentName cn = null;
						if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
							cn = new ComponentName("com.android.calendar",
									"com.android.calendar.LaunchActivity");
						} else {
							cn = new ComponentName("com.google.android.calendar",
									"com.android.calendar.LaunchActivity");}
						i.setComponent(cn);
						startActivity(i);
					} catch (ActivityNotFoundException e) {
						// TODO: handle exception
						Log.e("ActivityNotFoundException", e.toString());
					}

				}
				else if(arg2==5){
					intent.setClass(Main.this,Setting.class);
					Main.this.startActivity(intent);
				}
			}
		});

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
				AlertDialog.Builder adb = new Builder(Main.this);
				adb.setTitle("关于我们");
				adb.setMessage("xxxxxx");
				adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(Main.this, "Thanks",
								Toast.LENGTH_SHORT).show();
					}
				});
				adb.show();
				break;
			case 2:
				AlertDialog.Builder adb1 = new Builder(Main.this);
				adb1.setTitle("消息");
				adb1.setMessage("真的要退出吗？");
				adb1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
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

	@SuppressWarnings("deprecation")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK )
		{
			// 创建退出对话框
			AlertDialog isExit = new AlertDialog.Builder(this).create();
			// 设置对话框标题
			isExit.setTitle("消息");
			// 设置对话框消息
			isExit.setMessage("确定要退出吗");
			// 添加选择按钮并注册监听
			isExit.setButton("确定", listener);
			isExit.setButton2("取消", listener);
			// 显示对话框
			isExit.show();

		}

		return false;

	}
	/**监听对话框里面的button点击事件*/
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
	{
		public void onClick(DialogInterface dialog, int which)
		{
			switch (which)
			{
				case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
					am1.exitAllProgress();
//	                finish();
					break;
				case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
					break;
				default:
					break;
			}
		}
	};


}