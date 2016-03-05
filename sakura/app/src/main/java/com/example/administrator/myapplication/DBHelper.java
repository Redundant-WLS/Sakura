package com.example.administrator.myapplication;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHelper {
	
	public static final String DB_DBNAME="action_ljk";
	
	public static final String DB_TABLENAME="user";
	
	public static final String DB_LESSON_TABLENAME="lesson";
	
	public static final int VERSION = 1;
	
	public static SQLiteDatabase dbInstance; 
	
	public static SQLiteDatabase dbInstance1; 
	
	private MyDBHelper myDBHelper;
	
	private Context context;
	
	public DBHelper(Context context) {
		this.context = context;
	}
	
	public void openDatabase() {
		if(dbInstance == null) {
			myDBHelper = new MyDBHelper(context,DB_DBNAME,VERSION);			
			dbInstance = myDBHelper.getReadableDatabase();
		}
	}

	public long insert(Action action) {
		ContentValues values = new ContentValues();
		values.put("type_id", action.type_id);
		values.put("flag",action.flag);
		values.put("actiontitle", action.actiontitle);
		values.put("actiondetail", action.actiondetail);
		values.put("actiontype", action.actiontype);
		values.put("date", action.date);
		values.put("time", action.time);
		values.put("remidtype", action.remidtype);
		return dbInstance.insert(DB_TABLENAME, null, values);
	}
	
	public long insertLesson(Lesson lesson) {
		ContentValues values = new ContentValues();
		values.put("classname" , lesson.classname);
		values.put("classteacher" , lesson.classteacher);
		values.put("classroom" , lesson.classroom);
		values.put("classstarttime" , lesson.classstarttime);
		values.put("classendtime" , lesson.classendtime);
		values.put("startendtime" , lesson.classstarttime+"--"+lesson.classendtime);		
		values.put("classday" , lesson.classday);
		values.put("classday_id" , lesson.classday_id);
		values.put("classremidtype" , "��");	
		Log.i("insertLesson",lesson.classname);
		return dbInstance.insert(DB_LESSON_TABLENAME, null, values);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	
	public ArrayList getAllAction() {
		ArrayList list = new ArrayList();
		

		Cursor cursor = dbInstance.query("user",new String[]{"num_id","type_id","flag","actiontitle"
				                         ,"actiondetail","actiontype","date","time","remidtype"}
		                                 ,null,null,null,null,null);
		while(cursor.moveToNext()) {
			HashMap<String, Comparable> item = new HashMap<String, Comparable>();
			item.put("num_id", cursor.getInt(cursor.getColumnIndex("num_id")));
			item.put("type_id", cursor.getInt(cursor.getColumnIndex("type_id")));
			item.put("flag", cursor.getInt(cursor.getColumnIndex("flag")));
			item.put("actiontitle", cursor.getString(cursor.getColumnIndex("actiontitle")));
			item.put("actiondetail", cursor.getString(cursor.getColumnIndex("actiondetail")));
			item.put("actiontype", cursor.getString(cursor.getColumnIndex("actiontype")));
			item.put("date", cursor.getString(cursor.getColumnIndex("date")));
			item.put("time", cursor.getString(cursor.getColumnIndex("time")));
			item.put("remidtype", cursor.getString(cursor.getColumnIndex("remidtype")));
			list.add(item);
		}
		return list;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	
	public ArrayList getAllLesson() {
		ArrayList list = new ArrayList();
		Log.i("DBHelpergetAllLesson1","start.");
		Cursor cursor = dbInstance.query("lesson",new String[]{"num_class_id","classname"
				                         ,"classteacher","classroom","classstarttime"
				                         ,"classendtime","startendtime","classday","classday_id"
				                         ,"classremidtype"},null,null,null,null,null);
		Log.i("DBHelpergetAllLesson2",cursor+".");
		while(cursor.moveToNext()) {
			Log.i("DBHelpergetAllLesson3","3.");
			HashMap<String, Comparable> item = new HashMap<String, Comparable>();
			item.put("num_class_id", cursor.getInt(cursor.getColumnIndex("num_class_id")));
			item.put("classname", cursor.getString(cursor.getColumnIndex("classname")));			
			item.put("classteacher", cursor.getString(cursor.getColumnIndex("classteacher")));
			item.put("classroom", cursor.getString(cursor.getColumnIndex("classroom")));
			item.put("classstarttime", cursor.getString(cursor.getColumnIndex("classstarttime")));
			item.put("classendtime", cursor.getString(cursor.getColumnIndex("classendtime")));
			item.put("startendtime", cursor.getString(cursor.getColumnIndex("startendtime")));			
			item.put("classday", cursor.getString(cursor.getColumnIndex("classday")));
			item.put("classday_id", cursor.getInt(cursor.getColumnIndex("classday_id")));
			item.put("classremidtype", cursor.getString(cursor.getColumnIndex("classremidtype")));
			list.add(item);}
		return list;
	}
	
	public void delete(Integer integer) {
		dbInstance.delete(DB_TABLENAME, "num_id=?", new String[]{integer.toString()});
	}
	
	public void deleteLesson(Integer integer) {
		dbInstance.delete(DB_LESSON_TABLENAME, "num_class_id=?", new String[]{integer.toString()});
	}
	public void modify(Action action) {
		Log.i("num_id1",action.num_id+"");
		ContentValues values1 = new ContentValues();
		values1.put("num_id",action.num_id);
		values1.put("flag",action.flag);
		values1.put("type_id",action.type_id);
		values1.put("actiontitle", action.actiontitle);
		values1.put("actiondetail", action.actiondetail);
		values1.put("actiontype", action.actiontype);
		values1.put("date", action.date);
		values1.put("time", action.time);
		values1.put("remidtype", action.remidtype);
		Log.i("modify","modify");
		Log.i("num_id2",action.num_id+"");
		dbInstance.update(DB_TABLENAME, values1, "num_id=?",
				          new String[]{String.valueOf((action.num_id))});
	}
	
	public void modifyLesson(Lesson lesson) {
		ContentValues values1 = new ContentValues();
		values1.put("num_class_id",lesson.num_class_id);
		values1.put("classname",lesson.classname);
		values1.put("classteacher",lesson.classteacher);
		values1.put("classroom", lesson.classroom);
		values1.put("classstarttime", lesson.classstarttime);
		values1.put("classendtime", lesson.classendtime);
		values1.put("startendtime", lesson.classstarttime+"--"+lesson.classendtime);
		values1.put("classday", lesson.classday);
		values1.put("classday_id", lesson.classday_id);
		values1.put("classremidtype", lesson.classremidtype);		
		dbInstance.update(DB_LESSON_TABLENAME, values1, "num_class_id=?", 
				          new String[]{String.valueOf((lesson.num_class_id))});
	}
	
	public Action query(int id){
	Action action = new Action();
	Log.i("query_id",id+"");
	Cursor cursor = dbInstance.query("user", null, "num_id=?", new String[]{id+""}, null, null, null);
	while(cursor.moveToNext()){
		action.num_id = Integer.valueOf(id) ;
		action.type_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("type_id")));
		action.flag= Integer.parseInt(cursor.getString(cursor.getColumnIndex("flag")));
		action.actiontitle = cursor.getString(cursor.getColumnIndex("actiontitle"));
		action.actiondetail = cursor.getString(cursor.getColumnIndex("actiondetail"));
		action.actiontype = cursor.getString(cursor.getColumnIndex("actiontype"));
		action.date = cursor.getString(cursor.getColumnIndex("date"));
		action.time = cursor.getString(cursor.getColumnIndex("time"));
		action.remidtype = cursor.getString(cursor.getColumnIndex("remidtype"));
	}
	return action;
	}
	
	public Lesson queryLesson(int id){
		Lesson lesson = new Lesson();
		Cursor cursor = dbInstance.query("lesson", null,
				                         "num_class_id=?", new String[]{id+""}, null, null, null);
		while(cursor.moveToNext()){
		  lesson.num_class_id = Integer.valueOf(id) ;
		  lesson.classname = cursor.getString(cursor.getColumnIndex("classname"));
		  lesson.classteacher = cursor.getString(cursor.getColumnIndex("classteacher"));
		  lesson.classroom = cursor.getString(cursor.getColumnIndex("classroom"));
		  lesson.classstarttime = cursor.getString(cursor.getColumnIndex("classstarttime"));
		  lesson.classendtime = cursor.getString(cursor.getColumnIndex("classendtime"));
		  lesson.startendtime = cursor.getString(cursor.getColumnIndex("startendtime"));
		  lesson.classday = cursor.getString(cursor.getColumnIndex("classday"));
		  lesson.classday_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("classday_id")));
		  lesson.classremidtype = cursor.getString(cursor.getColumnIndex("classremidtype"));
		}
		return lesson;
		}
	
	public int lastId(){  
		
	Cursor cursor = dbInstance.rawQuery("select last_insert_rowid() from user",null);          
	int id = 0;    
	if(cursor.moveToFirst())  
	   id = cursor.getInt(0);  
	return id; 
	}
	
public class MyDBHelper extends SQLiteOpenHelper {
	
	private static final int VERSION = 1;

	public MyDBHelper(Context context, String name, CursorFactory factory,int version) {
		super(context, name, factory, version);
	}
	
	public MyDBHelper(Context context,String name){
		this(context,name,VERSION);
	}
	
	public MyDBHelper(Context context,String name,int version){
		this(context, name,null,version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	  Log.i("ljk","create a Database");
	  db.execSQL("create table user (num_id integer primary key autoincrement, type_id integer " +
				   ",flag integer,actiontitle text,actiondetail text,actiontype text," +
				   "date text,time text,remidtype text)");
      db.execSQL("create table lesson (num_class_id integer primary key autoincrement" +
      		       ",classname text,classteacher text,classroom text,classstarttime text" +
      		       ",classendtime text,startendtime text,classday text,classday_id integer" +
      		       ",classremidtype text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "drop table if exists " + DB_TABLENAME;
		db.execSQL(sql);
		String sql1 = "drop table if exists " + DB_LESSON_TABLENAME;
		db.execSQL(sql1);
		myDBHelper.onCreate(db);
	}

}

}