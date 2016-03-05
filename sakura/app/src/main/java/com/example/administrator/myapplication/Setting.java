package com.example.administrator.myapplication;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ljk.R;


public class Setting extends Activity {
	private ActivityManager am1;
	private TextView view01;
	private Spinner spinner01;
	private ArrayAdapter<?> adapter1;
	private TextView view02;
	private Spinner spinner02;
	private ArrayAdapter<?> adapter2;
	DBHelper helper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);

		am1 = ActivityManager.getInstance();
		am1.addActivity(this);

		//创建数据库帮助类
		helper = new DBHelper(Setting.this);
		//打开数据库
		helper.openDatabase();

		spinner01 = (Spinner) findViewById(R.id.setting_spinner01);
		view01 = (TextView) findViewById(R.id.setting_spinnerText01);
		//将可选内容与ArrayAdapter连接起来
		adapter1 = ArrayAdapter.createFromResource(this, R.array.model, android.R.layout.simple_spinner_item);
		//设置下拉列表的风格
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//将adapter1 添加到spinner中
		spinner01.setAdapter(adapter1);
		//添加事件Spinner事件监听
		spinner01.setOnItemSelectedListener(new Spinner01XMLSelectedListener());
		//设置默认值
		spinner01.setVisibility(View.VISIBLE);

		spinner02 = (Spinner) findViewById(R.id.setting_spinner02);
		view02 = (TextView) findViewById(R.id.setting_spinnerText02);
		//将可选内容与ArrayAdapter连接起来
		adapter2 = ArrayAdapter.createFromResource(this, R.array.model, android.R.layout.simple_spinner_item);
		//设置下拉列表的风格
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//将adapter2 添加到spinner中
		spinner02.setAdapter(adapter2);
		//添加事件Spinner事件监听
		spinner02.setOnItemSelectedListener(new Spinner02XMLSelectedListener());
		//设置默认值
		spinner02.setVisibility(View.VISIBLE);

		// 绑定Layout里面的ListView
		ListView list = (ListView) findViewById(R.id.setting_listview);
		// 生成动态数组，加入数据
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("ItemText", "清除所有已完成的事件");
		listItem.add(map1);

		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("ItemText", "清除所有事件");
		listItem.add(map2);

		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("ItemText", "清除所有课程");
		listItem.add(map3);

		// 生成适配器的Item和动态数组对应的元素，这里用SimpleAdapter作为ListView的数据源
		// 如果条目布局比较复杂，可以继承BaseAdapter来定义自己的数据源。
		SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,// 数据源
				R.layout.setting_item,// ListItem的XML实现
				// 动态数组与ImageItem对应的子项
				new String[] {"ItemText" },
				// ImageItem的XML文件里面的一个ImageView,两个TextView ID
				new int[] {R.id.setting_listview_itemtext });
		// 添加并且显示
		list.setAdapter(listItemAdapter);

		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				switch (arg2 )
				{  case 0:

					//清除所有已完成的事件
					ArrayList list = helper.getAllAction();//拿到所有用户的list

					Log.i("size",list.size()+"");

					for(int i=0;i<list.size();i++){

						HashMap item = (HashMap) list.get(i);
						Integer flag_i= (Integer) item.get("flag");
						Integer num_id = (Integer) item.get("num_id");

						if(flag_i.compareTo(1) ==0){
							helper.delete(num_id);
						}
					}

					break;

					case 1:
						//清除所有的事件
						ArrayList list1 = helper.getAllAction();//拿到所有用户的list

						Log.i("size",list1.size()+"");

						for(int i=0;i<list1.size();i++){

							HashMap item = (HashMap) list1.get(i);
							Integer num_id = (Integer) item.get("num_id");
							helper.delete(num_id);
						}
						break;

					case 2:
						//清除所有的课程
						ArrayList list2 = helper.getAllLesson();//拿到所有用户的list

						Log.i("size",list2.size()+"");

						for(int i=0;i<list2.size();i++){

							HashMap item = (HashMap) list2.get(i);
							Integer num_class_id = (Integer) item.get("num_class_id");
							helper.deleteLesson(num_class_id);
						}
						break;
				}

			}
		});
	}

	//使用XML形式操作
	class Spinner01XMLSelectedListener implements OnItemSelectedListener{
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
			view01.setText("请选择事件的提醒方式:"+adapter1.getItem(arg2));
			ArrayList list = helper.getAllAction();//拿到所有用户的list

			for(int i=0;i<list.size();i++){
				HashMap item = (HashMap) list.get(i);
				Action action = new Action();
				action.num_id = Integer.parseInt(String.valueOf(item.get("num_id")));
				action.type_id = Integer.parseInt(String.valueOf(item.get("type_id")));
				action.flag = Integer.parseInt(String.valueOf(item.get("flag")));
				action.actiontitle = String.valueOf(item.get("actiontitle"));
				action.actiondetail = String.valueOf(item.get("actiondetail"));
				action.actiontype = String.valueOf(item.get("actiontype"));
				action.date = String.valueOf(item.get("date"));
				action.time = String.valueOf(item.get("time"));
				if(arg2==0){
					action.remidtype = "振动";}
				if(arg2==1){
					action.remidtype = "铃声";}
				if(arg2==2){
					action.remidtype = "振动和铃声";}
				helper.modify(action);
			}

		}
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	//使用XML形式操作
	class Spinner02XMLSelectedListener implements OnItemSelectedListener{
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
			view02.setText("请选择课程的提醒方式:"+adapter2.getItem(arg2));

			ArrayList list = helper.getAllLesson();//拿到所有用户的list

			for(int i=0;i<list.size();i++){
				HashMap item = (HashMap) list.get(i);
				Lesson lesson = new Lesson();
				lesson.num_class_id = Integer.parseInt(String.valueOf(item.get("num_class_id")));
				lesson.classname = String.valueOf(String.valueOf(item.get("classname")));
				lesson.classteacher = String.valueOf(String.valueOf(item.get("classteacher")));
				lesson.classroom = String.valueOf(item.get("classroom"));
				lesson.classstarttime = String.valueOf(item.get("classstarttime"));
				lesson.classendtime = String.valueOf(item.get("classendtime"));
				lesson.startendtime = String.valueOf(item.get("startendtime"));
				lesson.classday = String.valueOf(item.get("classday"));
				lesson.classday_id = Integer.parseInt(String.valueOf(item.get("classday_id")));
				if(arg2==0){
					lesson.classremidtype = "振动";}
				if(arg2==1){
					lesson.classremidtype = "铃声";}
				if(arg2==2){
					lesson.classremidtype = "振动和铃声";}
				helper.modifyLesson(lesson);
			}
		}
		public void onNothingSelected(AdapterView<?> arg0) {
		}
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
				AlertDialog.Builder adb = new Builder(Setting.this);
				adb.setTitle("关于我们");
				adb.setMessage("xxxxxx");
				adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(Setting.this, "Thanks",
								Toast.LENGTH_SHORT).show();
					}
				});
				adb.show();
				break;
			case 2:
				AlertDialog.Builder adb1 = new Builder(Setting.this);
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

}