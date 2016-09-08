package com.zjm.doubantop;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by B on 2016/7/28.
 */
public class MyApplication extends Application{

    private static Context context;

    @Override
    public void onCreate() {
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }


}
