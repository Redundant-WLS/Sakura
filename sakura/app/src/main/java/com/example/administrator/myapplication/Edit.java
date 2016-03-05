package com.example.administrator.myapplication;

import java.util.Calendar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ljk.R;


public class Edit extends Activity {
	private TextView view01;
	private Spinner spinner01;
	private ArrayAdapter<?> adapter1;
	private TextView view02;
	private Spinner spinner02;
	private ArrayAdapter<?> adapter2;
	private Button date_btn;
	private Button time_btn;
	private Button save_btn;
	private Button can_btn;
	private Button det_btn;
	private String str_actiontype;
	private String str_remidtype;
	EditText et_title;
	EditText et_detail;
	private String str_date_old;
	private String str_time_old;
	private String str_date_new;
	private String str_time_new;
	DBHelper helper;
	public int id= 0 ;
	private boolean flag = false;
	private Calendar cal = Calendar.getInstance();
	AlarmManager am;
	Action action;
	private ActivityManager am1;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit);

		am1 = ActivityManager.getInstance();
		am1.addActivity(this);

		am = (AlarmManager)getSystemService(ALARM_SERVICE);

		//创建数据库帮助类
		helper = new DBHelper(Edit.this);
		//打开数据库
		helper.openDatabase();

		//获得意图
		Intent intent = getIntent();
		//从意图中得到需要的user对象
		action = (Action) intent.getSerializableExtra("action");

		id = action.num_id;
		Log.i("edit_action_num_id",id+"");
		loadActionData();

		setEditTextDisable();

		date_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				cal.setTimeInMillis(System.currentTimeMillis());
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH);
				int day = cal.get(Calendar.DAY_OF_MONTH);

				//跳出DatePickerDialog来设定时间
				new DatePickerDialog(Edit.this,
						new OnDateSetListener() {
							public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
								str_date_new = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
								Log.i("str_date_new",str_date_new);
								date_btn.setText(str_date_new);
							}
						},year,month,day).show();
			}
		});


		time_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 取得按下按钮的时间作为TimePickerDialog的默认值
				cal.setTimeInMillis(System.currentTimeMillis());
				//定义获取时间
				int mHour = cal.get(Calendar.HOUR_OF_DAY);
				int mMinute = cal.get(Calendar.MINUTE);
				//跳出TimePickerDialog来设定时间
				new TimePickerDialog(Edit.this,
						new TimePickerDialog.OnTimeSetListener() {
							public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
								str_time_new = hourOfDay+":"+minute;
								time_btn.setText(str_time_new);
								Log.i("str_time_new",str_time_new);
							}
						},mHour, mMinute, true).show();
			}
		});

		save_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(!flag)
				{	save_btn.setText("保存修改");
					setEditTextAble();
					setTitle("修改");
					flag = true;}
				else{
					//往数据库里面更新数据
					setTitle("详情");
					modify();

					if(!str_date_old.equals(str_date_new) || !str_time_old.equals(str_time_new)){
						Log.i("new alarm",str_date_new+str_time_new);

						// 广播通知
						Intent intent = new Intent();
						intent.setAction("action.cancleAlarm");
						sendBroadcast(intent);
//				            PendingIntent pend = PendingIntent.getBroadcast(Edit.this,
//				                    action.num_id, new Intent(Edit.this,CallAlarm.class), 0);
//				            am.cancel(pend);
						Log.i("cancel_alarm",action.num_id+"");

						baocunAction() ;
					}

//						Intent intent1 = new Intent();
//						intent1.putExtra("cancle_action_id",action.num_id+"");
//				        intent1.setAction("action.CancleAlarm");
//				        sendBroadcast(intent1);

					Intent intent = new Intent();
					intent.putExtra("cancle_action_id",action.num_id+"");
					intent.setAction("action.refreshAction");
					sendBroadcast(intent);

					setEditTextDisable();
					setColorToGray();
					save_btn.setText("修改");
					flag = false;
				}
			}
		});

		can_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		det_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				helper.delete(action.num_id);

				// 广播通知
				Intent intent1 = new Intent();
				intent1.setAction("action.cancleAlarm");
				sendBroadcast(intent1);
//					Intent intent1 = new Intent();
//					intent1.putExtra("cancle_action_id",action.num_id+"");
//			        intent1.setAction("action.CancleAlarm");
//			        sendBroadcast(intent1);

				Intent intent = new Intent();
				intent.putExtra("cancle_action_id",action.num_id+"");
				intent.setAction("action.refreshAction");
				sendBroadcast(intent);



//		            PendingIntent pend = PendingIntent.getBroadcast(Edit.this,
//		                    action.num_id, new Intent(Edit.this,CallAlarm.class), 0);
//		            am.cancel(pend);
//		            Log.i("delet_cancel_alarm",action.num_id+"");

				setEditTextDisable();
				setColorToGray();
				Log.i("num_id",action.num_id+"");
				finish();
			}
		});

	}



	public void loadActionData() {

		Log.i("num_id_intent1",action.num_id+"");
		save_btn = (Button) findViewById(R.id.save);
		can_btn = (Button) findViewById(R.id.cancel);
		det_btn = (Button) findViewById(R.id.delete);
		date_btn = (Button) findViewById(R.id.date_btn);
		time_btn = (Button) findViewById(R.id.time_btn);
		et_title = (EditText)findViewById(R.id.title);
		et_detail = (EditText)findViewById(R.id.detail);
		save_btn.setText("修改");
		date_btn.setText(action.date);
		time_btn.setText(action.time);
		str_date_old = action.date;
		str_time_old = action.time;
		Log.i("str_date_old",str_date_old);
		Log.i("str_time_old",str_time_old);
		str_date_new = action.date;
		str_time_new = action.time;
		et_title.setText(action.actiontitle);
		et_detail.setText(action.actiondetail);

		spinner01 = (Spinner) findViewById(R.id.spinner01);
		view01 = (TextView) findViewById(R.id.spinnerText01);
		//将可选内容与ArrayAdapter连接起来
		adapter1 = ArrayAdapter.createFromResource(this, R.array.planets, android.R.layout.simple_spinner_item);
		//设置下拉列表的风格
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//将adapter1 添加到spinner中
		spinner01.setAdapter(adapter1);
		//添加事件Spinner事件监听
		spinner01.setOnItemSelectedListener(new Spinner01XMLSelectedListener());
		//设置默认值
		if(action.actiontype.equals("学习")){	spinner01.setSelection(0);}
		if(action.actiontype.equals("学生工作")){	spinner01.setSelection(1);}
		if(action.actiontype.equals("社团活动")){	spinner01.setSelection(2);}
		if(action.actiontype.equals("体育锻炼")){	spinner01.setSelection(3);}
		if(action.actiontype.equals("交友")){	spinner01.setSelection(4);}
		if(action.actiontype.equals("其他")){	spinner01.setSelection(5);}
		spinner01.setVisibility(View.VISIBLE);

		spinner02 = (Spinner) findViewById(R.id.spinner02);
		view02 = (TextView) findViewById(R.id.spinnerText02);
		//将可选内容与ArrayAdapter连接起来
		adapter2 = ArrayAdapter.createFromResource(this, R.array.model, android.R.layout.simple_spinner_item);
		//设置下拉列表的风格
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//将adapter2 添加到spinner中
		spinner02.setAdapter(adapter2);
		//添加事件Spinner事件监听
		spinner02.setOnItemSelectedListener(new Spinner02XMLSelectedListener());
		if(action.remidtype.equals("振动")){	spinner02.setSelection(0);}
		if(action.remidtype.equals("铃声")){	spinner02.setSelection(1);}
		if(action.remidtype.equals("振动和铃声")){	spinner02.setSelection(2);}
		//设置默认值
		spinner02.setVisibility(View.VISIBLE);
	}

	//使用XML形式操作
	class Spinner01XMLSelectedListener implements OnItemSelectedListener{
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
			view01.setText("请选择提醒方式:"+adapter1.getItem(arg2));
			str_actiontype = (String) adapter1.getItem(arg2);
		}
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	//使用XML形式操作
	class Spinner02XMLSelectedListener implements OnItemSelectedListener{
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
			view02.setText("你添加的事件类型："+adapter2.getItem(arg2));
			str_remidtype = (String) adapter2.getItem(arg2);
		}
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}


	private void setEditTextDisable() {
		et_title.setEnabled(false);
		et_detail.setEnabled(false);
		date_btn.setEnabled(false);
		time_btn.setEnabled(false);
		spinner01.setEnabled(false);
		spinner02.setEnabled(false);
		setColorToGray();

	}

	/**
	 *  设置显示的字体颜色为白色
	 */
	private void setColorToGray() {
		et_title.setTextColor(Color.GRAY);
		et_detail.setTextColor(Color.GRAY);
		date_btn.setTextColor(Color.GRAY);
		time_btn.setTextColor(Color.GRAY);
		view01.setTextColor(Color.GRAY);
		view02.setTextColor(Color.GRAY);
	}

	/**
	 * 设置EditText为可用状态
	 */
	private void setEditTextAble() {
		et_title.setEnabled(true);
		et_detail.setEnabled(true);
		date_btn.setEnabled(true);
		time_btn.setEnabled(true);
		spinner01.setEnabled(true);
		spinner02.setEnabled(true);
		setColorToBlack();
	}

	/**
	 *  设置显示的字体颜色为黑色
	 */
	private void setColorToBlack() {
		et_title.setTextColor(Color.BLACK);
		et_detail.setTextColor(Color.BLACK);
		date_btn.setTextColor(Color.BLACK);
		time_btn.setTextColor(Color.BLACK);
		view01.setTextColor(Color.BLACK);
		view02.setTextColor(Color.BLACK);
	}

	/**
	 * 获得最新数据，创建DBHelper对象，更新数据库
	 */
	private void modify() {
		Log.i("num_id_intent2",action.num_id+"");
		Action  action1 = new Action();
		action1.num_id = action.num_id;
		action1.flag =  Integer.valueOf(0);
		action1.flag = action.flag;
		action1.actiontitle = et_title.getText().toString();
		action1.actiondetail = et_detail.getText().toString();
		action1.actiontype = str_actiontype;
		action1.date = date_btn.getText().toString();
		action1.time = time_btn.getText().toString();
		action1.remidtype = str_remidtype;

		if(action1.actiontype.equals("学习")){
			action1.type_id = new Integer(0);
		}

		if(action1.actiontype.equals("学生工作")){
			action1.type_id = new Integer(1);
		}

		if(action1.actiontype.equals("社团活动")){
			action1.type_id = new Integer (2);
		}

		if(action1.actiontype.equals("体育锻炼")){
			action1.type_id = new Integer(3);
		}

		if(action1.actiontype.equals("交友")){
			action1.type_id =new Integer(4);
		}

		if(action1.actiontype.equals("其他")){
			action1.type_id =new Integer(5);
		}

		String str_title_null = et_title.getText().toString();

		if (str_title_null == null || str_title_null.equals("")){

			Toast.makeText(Edit.this, "主题不可以为空", Toast.LENGTH_SHORT).show();}
		else{

			Log.i("action_num_id_modify",action1.num_id+"");
			helper.modify(action1);

		}
	}

	public String format(int i){
		String s = ""+i;
		if(s.length()==1) s = "0"+s;
		return s;
	}

	public void baocunAction() {

		Log.i("Edit_num_id_last",helper.lastId()+".");
		cal.setTimeInMillis(System.currentTimeMillis());
		if(!str_time_new.equals("")&&!str_date_new.equals("")){
			String[] t1 = str_date_new.split("-");
			String[] t2 = str_time_new.split(":");
			Log.i("date",t1[0]+t1[1]+t1[2]);
			cal.set(Integer.parseInt(t1[0]), (Integer.parseInt(t1[1])-1),Integer.parseInt(t1[2]));
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t2[0]));
			cal.set(Calendar.MINUTE, Integer.parseInt(t2[1]));
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			Intent intent = new Intent(Edit.this,CallAlarm1.class);
			String str_id = id+"";
			intent.putExtra("action_id1", str_id);
			Log.i("action_id1", str_id);
			Log.i("success","success");
			intent.setAction("editAction");
			PendingIntent pi2 = PendingIntent.getBroadcast(Edit.this, id, intent, 0);
			am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi2);
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
				AlertDialog.Builder adb = new Builder(Edit.this);
				adb.setTitle("关于我们");
				adb.setMessage("xxxxxxx");
				adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(Edit.this, "Thanks",
								Toast.LENGTH_SHORT).show();
					}
				});
				adb.show();
				break;
			case 2:
				AlertDialog.Builder adb1 = new Builder(Edit.this);
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