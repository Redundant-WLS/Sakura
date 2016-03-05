package com.example.administrator.myapplication;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import com.ljk.R;

public class Add extends Activity {
	   private TextView view01;
	   private Spinner spinner01;
	   private ArrayAdapter<?> adapter1;
	   private TextView view02;
	   private Spinner spinner02;
	   private ArrayAdapter<?> adapter2;
	   public Button date_btn;
	   public Button time_btn;	
	   public Button save_btn;
	   public Button can_btn;
	   public String str_actiontype;
	   public String str_remidtype;
	   private String str_date;
	   private String str_time;
	   private AlarmManager am;
	   private ActivityManager am1;
	   EditText et_title;
	   EditText et_detail;
	   DBHelper helper;
	   public int id= 0 ;
	   private Calendar cal = Calendar.getInstance();
	   Calendar c = Calendar.getInstance();
	   Calendar c1 = Calendar.getInstance();
	   Calendar c2 = Calendar.getInstance();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addnew);
		
		am1 = ActivityManager.getInstance();
		am1.addActivity(this);
		
	      IntentFilter intentFilter = new IntentFilter();  
	      intentFilter.addAction("action.cancleAlarm");  
	      registerReceiver(cancleAlarmBroadcastReceiver, intentFilter); 
	    
		am = (AlarmManager) getSystemService(ALARM_SERVICE);
	     helper = new DBHelper(Add.this);
		helper.openDatabase();
		
		loadAction();
		
	    date_btn.setOnClickListener(new OnClickListener() {		
				public void onClick(View v) {
					cal.setTimeInMillis(System.currentTimeMillis());
					int year = cal.get(Calendar.YEAR);
					int month = cal.get(Calendar.MONTH);
					int day = cal.get(Calendar.DAY_OF_MONTH);
					new DatePickerDialog(Add.this,new DatePickerDialog.OnDateSetListener(){
						public void onDateSet(DatePicker view, int year, int monthOfYear,
								int dayOfMonth) {
							str_date = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
							date_btn.setText(str_date);}
					},year,month,day).show();
				}
			});
		   		   

	       time_btn.setOnClickListener(new OnClickListener() {		
				public void onClick(View v) {
					cal.setTimeInMillis(System.currentTimeMillis());
					int hour = cal.get(Calendar.HOUR_OF_DAY);
					int minute = cal.get(Calendar.MINUTE);
					new TimePickerDialog(Add.this,new TimePickerDialog.OnTimeSetListener(){
						public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
							str_time = hourOfDay+":"+minute;
							time_btn.setText(str_time);}						
					},hour,minute,true).show();}
			});
	       
	       save_btn.setOnClickListener(new OnClickListener() {		
				public void onClick(View v) {
					Action  action = new Action();	
					action.flag = Integer.valueOf(0);
					action.actiontitle = et_title.getText().toString();
					action.actiondetail = et_detail.getText().toString();
					action.actiontype = str_actiontype;
					action.date = date_btn.getText().toString();
					action.time = time_btn.getText().toString();
					action.remidtype = str_remidtype;
					
					if(action.actiontype.equals("学习")){
						action.type_id = Integer.valueOf(0);
					}	
					
					if(action.actiontype.equals("学生工作")){
						action.type_id = Integer.valueOf(1);
					}
					
					if(action.actiontype.equals("社团活动")){
						action.type_id = Integer.valueOf(2);
					}
					
					if(action.actiontype.equals("体育锻炼")){
						action.type_id = Integer.valueOf(3);
					}
					
					if(action.actiontype.equals("交友")){
						action.type_id =Integer.valueOf(4);
					}
					
					if(action.actiontype.equals("其他")){
						action.type_id =Integer.valueOf(5);
					}
					
                    String str_title_null = et_title.getText().toString();
			
		          	if (str_title_null == null || str_title_null.equals("")){
				
	            		Toast.makeText(Add.this, "主题不可以为空", Toast.LENGTH_SHORT).show();}
		          	else{


					long result = helper.insert(action);
					

					if(result == -1 ) {
						Toast.makeText(Add.this, "添加失败", Toast.LENGTH_LONG).show();
					}else{
					setTitle("用户添加成功!");}

					baocunAction();
					finish();
		          	}
				}
			});
	       
	       can_btn.setOnClickListener(new OnClickListener() {		
				public void onClick(View v) {
						finish();
				}
			});
	    	       
	}


	    class Spinner01XMLSelectedListener implements OnItemSelectedListener{
	        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
	            view01.setText("请选择添加的事件类型:"+adapter1.getItem(arg2));
	            str_actiontype = (String) adapter1.getItem(arg2);
	        }
	        public void onNothingSelected(AdapterView<?> arg0) {
	        }
	    }
	    

	    class Spinner02XMLSelectedListener implements OnItemSelectedListener{
	        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
	            view02.setText("请选择提醒方式"+adapter2.getItem(arg2));
	            str_remidtype = (String) adapter2.getItem(arg2);
	        }
	        public void onNothingSelected(AdapterView<?> arg0) {
	        }
	    }
	    

	    private BroadcastReceiver cancleAlarmBroadcastReceiver = new BroadcastReceiver() {  
	    
	        @Override  
	        public void onReceive(Context context, Intent intent) {  
	            String action = intent.getAction();  
	            if (action.equals("action.cancleAlarm"))  
	            {  
	                Log.i("add_receiver","cancleAlarm"); 
	            }  
	        }  
	    };  
		  
	    @Override  
	    protected void onDestroy() {  
	        super.onDestroy();  
	        unregisterReceiver(cancleAlarmBroadcastReceiver);  
	    }  
	    
	    public void loadAction(){
	    	
			save_btn = (Button) findViewById(R.id.save);
			can_btn = (Button) findViewById(R.id.cancel);
			date_btn = (Button) findViewById(R.id.date_btn);
			time_btn = (Button) findViewById(R.id.time_btn);	
			et_title = (EditText)findViewById(R.id.title);
			et_detail = (EditText)findViewById(R.id.detail);
			date_btn.setText(c1.get(Calendar.YEAR)+"-" +(c1.get(Calendar.MONTH)+1)+ "-" +c1.get(Calendar.DAY_OF_MONTH));
			time_btn.setText(c1.get(Calendar.HOUR_OF_DAY)+":" +c1.get(Calendar.MINUTE));
			Log.i("date_btn",c1.get(Calendar.YEAR)+"-" +(c1.get(Calendar.MONTH)+1)+ "-" +c1.get(Calendar.DAY_OF_MONTH));
			Log.i("time_btn",c1.get(Calendar.HOUR_OF_DAY)+":" +c1.get(Calendar.MINUTE));

			str_date = c1.get(Calendar.YEAR)+"-" +(c1.get(Calendar.MONTH)+1)+ "-" +c1.get(Calendar.DAY_OF_MONTH);
			str_time = c1.get(Calendar.HOUR_OF_DAY)+":" +c1.get(Calendar.MINUTE);
			spinner01 = (Spinner) findViewById(R.id.spinner01);
	        view01 = (TextView) findViewById(R.id.spinnerText01);

	     	adapter1 = ArrayAdapter.createFromResource(this, R.array.planets, android.R.layout.simple_spinner_item);

			adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			spinner01.setAdapter(adapter1);

		    spinner01.setOnItemSelectedListener(new Spinner01XMLSelectedListener());

			spinner01.setVisibility(View.VISIBLE);
			
			spinner02 = (Spinner) findViewById(R.id.spinner02);
	        view02 = (TextView) findViewById(R.id.spinnerText02);

	     	adapter2 = ArrayAdapter.createFromResource(this, R.array.model, android.R.layout.simple_spinner_item);

			adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			spinner02.setAdapter(adapter2);

		    spinner02.setOnItemSelectedListener(new Spinner02XMLSelectedListener());

			spinner02.setVisibility(View.VISIBLE);
	    	
	    }
	    
		public void baocunAction() {

			    id = helper.lastId();
				cal.setTimeInMillis(System.currentTimeMillis());
				if(!str_time.equals("")&&!str_date.equals("")){
					String[] t1 = str_date.split("-");
					String[] t2 = str_time.split(":");
					cal.set(Integer.parseInt(t1[0]), (Integer.parseInt(t1[1])-1),Integer.parseInt(t1[2]));
					cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t2[0]));
					cal.set(Calendar.MINUTE, Integer.parseInt(t2[1]));
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);

					Intent intent = new Intent(Add.this,CallAlarm.class);	
					String str_id = id+"";
					intent.putExtra("action_id", str_id);
					PendingIntent pi = PendingIntent.getBroadcast(Add.this, id, intent, 0);
					am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
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
				AlertDialog.Builder adb = new Builder(Add.this);
				adb.setTitle("关于我们");
				adb.setMessage("xxxxxxx");
				adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(Add.this, "Thanks",
								Toast.LENGTH_SHORT).show();
					}
				});
				adb.show();
				break;
			case 2:
				AlertDialog.Builder adb1 = new Builder(Add.this);
				adb1.setTitle("消息");
				adb1.setMessage("真的要退出吗");
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
