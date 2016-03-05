package com.example.administrator.myapplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.ljk.R;


public class Schedual extends Activity {
	private Spinner schedual_spinner;
	private ArrayAdapter<?> adapter1;
	private Button btn_addclass;
	private Integer classweek = Integer.valueOf(0) ;
	private Integer classweek_id;
	private DBHelper helper ;
	private Lesson lesson;
	private AlarmManager am;
	private ActivityManager am1;
	private Calendar cal = Calendar.getInstance();
	private Calendar cal1 = Calendar.getInstance();
	private int num_class_id_shedual;
	private int int_classday_id_nowpppp;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		am1 = ActivityManager.getInstance();
		am1.addActivity(this);
		int_classday_id_nowpppp = cal.get(Calendar.DAY_OF_WEEK);
		Log.i("Schedual_int_classday_id_nowpppp",int_classday_id_nowpppp+".////////");
		setContentView(R.layout.schedual);
		Log.i("schedual_context",this+"");

		schedual_spinner = (Spinner) findViewById(R.id.schedual_spinner);
		adapter1 = ArrayAdapter.createFromResource(this, R.array.week,
				android.R.layout.simple_spinner_item);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		schedual_spinner.setAdapter(adapter1);
		schedual_spinner.setOnItemSelectedListener(new Spinner01XMLSelectedListener());
		schedual_spinner.setVisibility(View.VISIBLE);

		am = (AlarmManager) getSystemService(ALARM_SERVICE);
		showClass();

		btn_addclass = (Button) findViewById(R.id.addclass);
		btn_addclass.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MyDialog dialog = new MyDialog(Schedual.this,"添加课程：",new MyDialog.OnDialogListener() {
					public void back(String name) {
						showClass();
						Log.i("Schedual","back");
					}
				});
				dialog.show();
			}
		});
	}

	private void  showClass(){

		ListView lv = (ListView)findViewById(R.id.schedual_deuList); //创建ListView对象
		//创建数据库帮助类
		helper = new DBHelper(Schedual.this);
		helper.openDatabase();
		ArrayList list = helper.getAllLesson();//拿到所有课程的list
		ArrayList list1 = new ArrayList();

		for(int i=0;i<list.size();i++){

			HashMap item = (HashMap) list.get(i);
			classweek_id=(Integer) item.get("classday_id");
			Log.i("Schedual","classweek_id"+classweek_id);
			Log.i("Schedual","classweek"+classweek);
			if(classweek_id.compareTo(classweek) ==0){
				list1.add(item);
				Log.i("Schedual_startendtime",""+item.get("classday_id"));}
		}

		Log.i("Schedual_list",list.size()+".");

		SimpleAdapter adapter = new SimpleAdapter(this,
				list1,
				R.layout.schedual_list,
				new String[]{"classname","classroom","startendtime","classteacher"},
				new int[]{R.id.schedual_classname_listview,R.id.schedual_classroom_listview,
						R.id.schedual_classtime_listview,R.id.schedual_classteacher_listview});

		lv.setAdapter(adapter);

		lv.setOnItemLongClickListener(new ListView.OnItemLongClickListener(){
			public boolean onItemLongClick(AdapterView<?> arg0, View v,final int position, long arg3) {
				HashMap item = (HashMap)arg0.getItemAtPosition(position);
				num_class_id_shedual = Integer.parseInt(String.valueOf(item.get("num_class_id")));
				lesson = new Lesson();
				lesson.num_class_id = Integer.parseInt(String.valueOf(item.get("num_class_id")));
				lesson.classname = String.valueOf(String.valueOf(item.get("classname")));
				lesson.classteacher = String.valueOf(String.valueOf(item.get("classteacher")));
				lesson.classroom = String.valueOf(item.get("classroom"));
				lesson.classstarttime = String.valueOf(item.get("classstarttime"));
				lesson.classendtime = String.valueOf(item.get("classendtime"));
				lesson.classday = String.valueOf(item.get("classday"));
				lesson.classday_id = Integer.parseInt(String.valueOf(item.get("classday_id")));

				android.content.DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which) {
						switch(which){
							case 0:
								edit(num_class_id_shedual);
								break;
							case 1:
								delete(num_class_id_shedual);
								break;
							case 2:
								setClock(num_class_id_shedual);
								break;
						}
					}
				};

				new AlertDialog.Builder(Schedual.this)
						.setTitle(R.string.operate)
						.setItems(R.array.oper_items, listener)
						.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
							public void onClick(DialogInterface dialog, int which) {
							}
						}).show();

				return false;
			}
		});
	}

	//使用XML形式操作
	class Spinner01XMLSelectedListener implements OnItemSelectedListener{
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
			classweek = Integer.valueOf(arg2) ;
			Log.i("Schedual","classweek"+classweek);
			showClass();
		}
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	public void edit(int position){
		EditMyDialog dialog = new EditMyDialog(Schedual.this,lesson,new EditMyDialog.OnDialogListener() {
			public void back(String name) {
				showClass();
			}
		});
		dialog.show();

	}

	public void delete(final int position){
		new AlertDialog.Builder(this)
				.setTitle(R.string.warn)
				.setMessage(R.string.confirm_delete)
				.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which) {
						Integer id = Integer.valueOf(position);
						helper.deleteLesson(id);
						Toast.makeText(Schedual.this, R.string.delete_success, Toast.LENGTH_LONG).show();
						showClass();
					}

				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	public void setClock(int position){

		Lesson lesson1 = helper.queryLesson(position);
		String str_setTime = lesson1.classstarttime;
		Log.i("str_setTime",str_setTime+"/");
		String str_classday = lesson1.classday;
		Log.i("str_classDay",str_classday+"/");
		int int_classday_id = lesson1.classday_id;
		int int_classday_id_now  = int_classday_id_nowpppp-2;
		Log.i("Schedual----int_classday_id+..+int_classday_id_now",int_classday_id+".."+int_classday_id_now);
		Log.i("Schedual_date",cal1.get(Calendar.YEAR)+"-"+(cal1.get(Calendar.MONTH)+1)+"-"+cal1.get(Calendar.DAY_OF_MONTH));

		String[] t1 = str_setTime.split(":");
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t1[0]));
		cal.set(Calendar.MINUTE, Integer.parseInt(t1[1]));
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		if(int_classday_id > int_classday_id_now){
			int class_cha = int_classday_id - int_classday_id_now ;
			if(class_cha == 7){
				if (cal1.getTimeInMillis() + 1000 * 10 <= cal.getTimeInMillis()){
					String[] t2 = str_setTime.split(":");
					cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t2[0]));
					cal.set(Calendar.MINUTE, Integer.parseInt(t2[1]));
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);

					Intent intent = new Intent(Schedual.this,CallAlarm2.class);
					String str_id = position+"";
					intent.putExtra("action_id2", str_id);
					PendingIntent pi = PendingIntent.getBroadcast(Schedual.this, position, intent, 0);
					am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
				}else{
					String[] t2 = str_setTime.split(":");
					cal.set(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH)+1,cal1.get(Calendar.DAY_OF_MONTH)+6);
					cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t2[0]));
					cal.set(Calendar.MINUTE, Integer.parseInt(t2[1]));
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);

					Intent intent = new Intent(Schedual.this,CallAlarm2.class);
					String str_id = position+"";
					intent.putExtra("action_id2", str_id);
					PendingIntent pi = PendingIntent.getBroadcast(Schedual.this, position, intent, 0);
					am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
				}
			}else{
				String[] t2 = str_setTime.split(":");
				cal.set(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH)+1,cal1.get(Calendar.DAY_OF_MONTH)+class_cha-1);
				cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t2[0]));
				cal.set(Calendar.MINUTE, Integer.parseInt(t2[1]));
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);

				Intent intent = new Intent(Schedual.this,CallAlarm2.class);
				String str_id = position+"";
				intent.putExtra("action_id2", str_id);
				PendingIntent pi = PendingIntent.getBroadcast(Schedual.this, position, intent, 0);
				am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
			}
		}

		if(int_classday_id < int_classday_id_now){
			int class_cha = int_classday_id - int_classday_id_now ;

			String[] t2 = str_setTime.split(":");
			cal.set(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH)+1,cal1.get(Calendar.DAY_OF_MONTH)+(7-class_cha)-1);
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t2[0]));
			cal.set(Calendar.MINUTE, Integer.parseInt(t2[1]));
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			Intent intent = new Intent(Schedual.this,CallAlarm2.class);
			String str_id = position+"";
			intent.putExtra("action_id2", str_id);
			PendingIntent pi = PendingIntent.getBroadcast(Schedual.this, position, intent, 0);
			am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
		}

		if(int_classday_id == int_classday_id_now){
			if (cal1.getTimeInMillis() + 1000 * 10 <= cal.getTimeInMillis()){
				String[] t2 = str_setTime.split(":");
//			cal.set(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH)+1,cal1.get(Calendar.DAY_OF_MONTH));
				cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t2[0]));
				cal.set(Calendar.MINUTE, Integer.parseInt(t2[1]));
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);

				Intent intent = new Intent(Schedual.this,CallAlarm2.class);
				String str_id = position+"";
				intent.putExtra("action_id2", str_id);
				PendingIntent pi = PendingIntent.getBroadcast(Schedual.this, position, intent, 0);
				am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
			}else{
				String[] t2 = str_setTime.split(":");
				cal.set(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH)+1,cal1.get(Calendar.DAY_OF_MONTH)+6);
				cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t2[0]));
				cal.set(Calendar.MINUTE, Integer.parseInt(t2[1]));
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);

				Intent intent = new Intent(Schedual.this,CallAlarm2.class);
				String str_id = position+"";
				intent.putExtra("action_id2", str_id);
				PendingIntent pi = PendingIntent.getBroadcast(Schedual.this, position, intent, 0);
				am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
			}
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
				AlertDialog.Builder adb = new Builder(Schedual.this);
				adb.setTitle("关于我们");
				adb.setMessage("xxxxxx" );
				adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(Schedual.this, "Thanks",
								Toast.LENGTH_SHORT).show();
					}
				});
				adb.show();
				break;
			case 2:
				AlertDialog.Builder adb1 = new Builder(Schedual.this);
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