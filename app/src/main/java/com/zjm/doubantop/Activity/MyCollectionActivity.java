package com.zjm.doubantop.Activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
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
    private int itemid = -2;
    private int mTouchSlop;
    private boolean mShow = true;
    private float mFirstY;
    private float mCurrentY;
    private ObjectAnimator mAnimator;
    private int direction;

    View.OnTouchListener myTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mFirstY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mCurrentY = event.getY();
                    if (mCurrentY - mFirstY > mTouchSlop) {
                        direction = 0;// down
                    } else if (mFirstY - mCurrentY > mTouchSlop) {
                        direction = 1;// up
                    }
                    if (direction == 1) {
                        if (mShow) {
                            toolbarAnim(1);//hide
                            mShow = !mShow;
                        }
                    } else if (direction == 0) {
                        if (!mShow) {
                            toolbarAnim(0);//show
                            mShow = !mShow;
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return false;
        }
    };

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

        View header = new View(this);
        header.setLayoutParams(new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(
                        R.dimen.abc_action_bar_default_height_material)));
        listView.addHeaderView(header);

        listView.setOnTouchListener(myTouchListener);

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
    }

    private void toolbarAnim(int flag) {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        if (flag == 0) {
            mAnimator = ObjectAnimator.ofFloat(toolbar,
                    "translationY", toolbar.getTranslationY(), 0);
        } else {
            mAnimator = ObjectAnimator.ofFloat(toolbar,
                    "translationY", toolbar.getTranslationY(),
                    -toolbar.getHeight());
        }
        mAnimator.start();
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
        if(collectionlist.size() < 5){
            listView.setOnTouchListener(null);
        }
        collectListAdapter.setList(collectionlist);
        collectListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MovieDetailActivity.launch(MyCollectionActivity.this, (String)collectionlist.get(i - 1).get("coverurl"), (String)collectionlist.get(i - 1).get("title"), (String)collectionlist.get(i - 1).get("content"), (String)collectionlist.get(i - 1).get("imageurl"));
        itemid = i - 1;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(itemid != -2&&!helper.checkExist((String)collectionlist.get(itemid).get("title"))){
            collectionlist.remove(itemid);
            collectListAdapter.notifyDataSetChanged();
        }

        getDataFromDB();
    }
}
