package com.zjm.doubantop.Activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zjm.doubantop.Adapter.CollectListAdapter;
import com.zjm.doubantop.DBHelper;
import com.zjm.doubantop.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by B on 2016/9/6.
 */
public class MyCollectionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private Toolbar toolbar;
    private ListView listView;
    private Cursor cursor;
    private DBHelper helper;
    private List<HashMap> collectionlist = new ArrayList<>();
    private CollectListAdapter collectListAdapter = CollectListAdapter.getAdapter();
    public static void launch(Activity activity){
        Intent intent = new Intent(activity, MyCollectionActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycollection);
        helper = new DBHelper(MyCollectionActivity.this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.listview);
        listView.setOnItemClickListener(this);
        listView.setAdapter(collectListAdapter);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ico_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getDataFromDB();
        collectListAdapter.setList(collectionlist);
        collectListAdapter.notifyDataSetChanged();

    }

    public void getDataFromDB(){
        cursor = helper.RetAllCollect();
        collectionlist.clear();
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String imageurl = cursor.getString(cursor.getColumnIndex("imageurl"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String coverurl = cursor.getString(cursor.getColumnIndex("coverurl"));
                HashMap map = new HashMap();
                map.put("content", content);
                map.put("imageurl", imageurl);
                map.put("title", title);
                map.put("coverurl", coverurl);
                collectionlist.add(map);
                cursor.moveToNext();
            }
        }
        cursor.close();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MovieDetailActivity.launch(MyCollectionActivity.this, (String)collectionlist.get(i).get("coverurl"), (String)collectionlist.get(i).get("title"), (String)collectionlist.get(i).get("content"), (String)collectionlist.get(i).get("imageurl"));
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }
}
