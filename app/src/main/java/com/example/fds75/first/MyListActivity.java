package com.example.fds75.first;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MyListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    List<String> data = new ArrayList<String>();
    private String TAG = "MyLIst";
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        ListView listView = (ListView) findViewById(R.id.mylist);
        //String data[] = {"111","222"};

        //init data初始化数据
        for (int i=0;i<10;i++){
            data.add("item"+i);
        }

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);//当前对象，布局，数据
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.nodata));//当为空时显示
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> listv, View view, int position, long id) {
        Log.i(TAG, "onItemClick: positioon"+position);
        Log.i(TAG, "onItemClick: parent"+listv);//将类中原来的parent改为listv，方便引入参数
        adapter.remove(listv.getItemAtPosition(position));//移除数据
        adapter.notifyDataSetChanged();//通知数据集改变，将会刷新

    }
}
