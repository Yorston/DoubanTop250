package com.zjm.doubantop.Activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zjm.doubantop.FileControler;
import com.zjm.doubantop.Gudie.HoleBean;
import com.zjm.doubantop.Gudie.NewbieGuideManager;
import com.zjm.doubantop.JsonBean;
import com.zjm.doubantop.Adapter.MainListAdapter;
import com.zjm.doubantop.ListSlideListener;
import com.zjm.doubantop.NetWorkConter.NetWork;
import com.zjm.doubantop.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemClickListener {

    private ListView listview;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private TextView textView;

    public static View v;
    private List<HashMap> list = new ArrayList();
    private NetWork netWork = NetWork.getNetWork();
    private MainListAdapter listadapter = MainListAdapter.getAdapter();
    private FileControler controler = FileControler.getFileControler();
    private ListSlideListener listener = ListSlideListener.getListener();
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
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTouchSlop = ViewConfiguration.get(MainActivity.this).getScaledTouchSlop();
        initView();
        if(savedInstanceState != null){
            list = (List<HashMap>) savedInstanceState.get("List");
        }
        EventBus.getDefault().register(this);
        listadapter.setList(list);
        //netWork.GetMsg(0,10);
        JsonBean bean = controler.GetFile();
        if(bean != null){
            EventBus.getDefault().post(bean);
            listener.UpdStart(11, false);
            NetWork.NextStart = 11;
            //ListSlideListener.old_start = 11;
        }else{
            netWork.GetMsg(0,10);
            //listener.UpdStart(241);
            //NetWork.NextStart = 241;
        }
        listview.setOnScrollListener(listener);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        byte[] bytearray;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(list);
            bytearray = byteArrayOutputStream.toByteArray();
            objectOutputStream.close();
            byteArrayOutputStream.close();
            outState.putByteArray("List", bytearray);

        } catch (IOException e) {
            e.printStackTrace();
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        list = (List<HashMap>) savedInstanceState.get("List");
    }

    public void initView(){
        listview = (ListView) findViewById(R.id.listview);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        textView = (TextView) findViewById(R.id.toolbattitle);

        View header = new View(this);
        header.setLayoutParams(new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(
                        R.dimen.abc_action_bar_default_height_material)));
        listview.addHeaderView(header);
        listview.setOnTouchListener(myTouchListener);
        listview.setAdapter(listadapter);
        listview.setOnItemClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        LayoutInflater inflater = LayoutInflater.from(this);
        v = inflater.inflate(R.layout.load, null);
        listview.addFooterView(v);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void GetBean(JsonBean jsonBean){
        list.addAll(jsonBean.getList());
        //List<HashMap> list = jsonBean.getList();
        listadapter.setList(list);
        listadapter.notifyDataSetChanged();
        v.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MovieDetailActivity.launch(MainActivity.this, (String)list.get(i - 1).get("MovieCover"), (String)list.get(i - 1).get("MovieTitle"), (String)list.get(i - 1).get("BaseInfo"), (String)list.get(i - 1).get("ImageUrl"));
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.my_collection:
                MyCollectionActivity.launch(MainActivity.this);
                break;
            case R.id.quit:
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(NewbieGuideManager.isNeverShowed(this, NewbieGuideManager.TYPE_COLLECT)) {
            new NewbieGuideManager(this, NewbieGuideManager.TYPE_COLLECT).addView(toolbar,
                    HoleBean.TYPE_RECTANGLE).show();
        }
    }
}
