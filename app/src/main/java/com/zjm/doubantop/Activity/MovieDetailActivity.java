package com.zjm.doubantop.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zjm.doubantop.DBHelper;
import com.zjm.doubantop.R;

/**
 * Created by B on 2016/9/6.
 */
public class MovieDetailActivity extends AppCompatActivity{

    private ImageView movieCover;
    private Toolbar toolbar;
    private TextView textView;
    private AppBarLayout appBarLayout;
    private DisplayImageOptions option;
    private DBHelper helper;
    private boolean isExist;
    private String coverUrl;
    private String movieName;
    private String content;
    private String movieUrl;

    public static void launch(Activity activity, String coverUrl, String movieName, String content, String movieUrl){
        Intent intent = new Intent(activity,MovieDetailActivity.class);
        intent.putExtra("CoverUrl", coverUrl);
        intent.putExtra("MovieName", movieName);
        intent.putExtra("Content", content);
        intent.putExtra("MovieUrl", movieUrl);
        activity.startActivityForResult(intent, 101);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moviedetail);
        option = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .showImageOnLoading(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        helper = new DBHelper(this);
        movieCover = (ImageView) findViewById(R.id.movie_cover);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        textView = (TextView) findViewById(R.id.movie_title);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
//        appBarLayout.addOnOffsetChangedListener(new AppbarListener());
        toolbar.setTitle("");
        movieUrl = getIntent().getStringExtra("MovieUrl");
        content = getIntent().getStringExtra("Content");
        coverUrl = getIntent().getStringExtra("CoverUrl");
        movieName = getIntent().getStringExtra("MovieName");
        isExist = helper.checkExist(movieName);
        textView.setText(getIntent().getStringExtra("MovieName"));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ico_back);
        ImageLoader.getInstance().loadImage(coverUrl, option, new SimpleImageLoadingListener(){
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                movieCover.setImageBitmap(loadedImage);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        if(isExist){
            menu.findItem(R.id.collect).setIcon(R.drawable.ico_collect);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.collect:
                if(isExist){
                    helper.delDate(movieName);
                    item.setIcon(R.drawable.ico_uncollect);
                    Toast.makeText(MovieDetailActivity.this, "Succeed", Toast.LENGTH_LONG).show();
                    isExist = false;
                }else{
                    helper.insertDate(content, movieUrl, movieName, coverUrl);
                    item.setIcon(R.drawable.ico_collect);
                    Toast.makeText(MovieDetailActivity.this, "Succeed", Toast.LENGTH_LONG).show();
                    isExist = true;
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    class AppbarListener implements AppBarLayout.OnOffsetChangedListener{
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        }
    }

}
