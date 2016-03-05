package com.example.administrator.myapplication;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ljk.R;

public class DeuTypeList extends Activity {
	private ActivityManager am1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deu_action_typeshow);

		am1 = ActivityManager.getInstance();
		am1.addActivity(this);

		// 绑定Layout里面的ListView
		ListView list = (ListView) findViewById(R.id.deu_typeshow_listview);
		// 生成动态数组，加入数据
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("ItemImage", R.drawable.work);// 图像资源的ID
		map1.put("ItemText", "       学习 ");
		listItem.add(map1);

		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("ItemImage", R.drawable.study);// 图像资源的ID
		map2.put("ItemText", "       学生工作");
		listItem.add(map2);

		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("ItemImage", R.drawable.shetuan);// 图像资源的ID
		map3.put("ItemText", "       社团活动");
		listItem.add(map3);

		HashMap<String, Object> map4 = new HashMap<String, Object>();
		map4.put("ItemImage", R.drawable.tiyu);// 图像资源的ID
		map4.put("ItemText", "       体育锻炼 ");
		listItem.add(map4);

		HashMap<String, Object> map5 = new HashMap<String, Object>();
		map5.put("ItemImage", R.drawable.friend);// 图像资源的ID
		map5.put("ItemText", "       交友 ");
		listItem.add(map5);

		HashMap<String, Object> map6 = new HashMap<String, Object>();
		map6.put("ItemImage", R.drawable.other);// 图像资源的ID
		map6.put("ItemText", "       其他");
		listItem.add(map6);

		// 生成适配器的Item和动态数组对应的元素，这里用SimpleAdapter作为ListView的数据源
		// 如果条目布局比较复杂，可以继承BaseAdapter来定义自己的数据源。
		SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,// 数据源
				R.layout.deu_action_typeshow_listitem,// ListItem的XML实现
				// 动态数组与ImageItem对应的子项
				new String[] { "ItemImage", "ItemText" },
				// ImageItem的XML文件里面的一个ImageView,两个TextView ID
				new int[] { R.id.deu_typeshow_itemimage, R.id.deu_typeshow_itemtext });
		// 添加并且显示
		list.setAdapter(listItemAdapter);

		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {

				Bundle bundle = new Bundle();
				bundle.putInt("type_id", arg2);
				Intent intent = new Intent();
				intent.putExtras(bundle);
				intent.setClass(DeuTypeList.this,DeuList.class);
				DeuTypeList.this.startActivity(intent);

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
				AlertDialog.Builder adb = new Builder(DeuTypeList.this);
				adb.setTitle("关于我们");
				adb.setMessage("北京邮电大学/网络技术研究院/李剑昆制作");
				adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(DeuTypeList.this, "Thanks",
								Toast.LENGTH_SHORT).show();
					}
				});
				adb.show();
				break;
			case 2:
				AlertDialog.Builder adb1 = new Builder(DeuTypeList.this);
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