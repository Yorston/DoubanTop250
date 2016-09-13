package com.zjm.doubantop.NetWorkConter;

import android.view.View;
import android.widget.Toast;

import com.zjm.doubantop.Activity.MainActivity;
import com.zjm.doubantop.FileControler;
import com.zjm.doubantop.JsonBean;
import com.zjm.doubantop.ListSlideListener;
import com.zjm.doubantop.MyApplication;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by B on 2016/7/28.
 */
public class NetWork {

    public static int NextStart = 1;
    private final static String baseurl = "https://api.douban.com/";
    private NetServiceCenter netService;

    private NetWork(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        netService = retrofit.create(NetServiceCenter.class);
    }

    private static NetWork netWork = new NetWork();

    public static NetWork getNetWork(){
        return netWork;
    }

    public void GetMsg(final int start, int count){
        MainActivity.v.setVisibility(View.VISIBLE);
        Call<JsonBean> call = netService.GetJson(start+"", count+"");
        call.enqueue(new Callback<JsonBean>() {
            @Override
            public void onResponse(Call<JsonBean> call, Response<JsonBean> response) {

                JsonBean bean = response.body();
                ListSlideListener listener = ListSlideListener.getListener();
                FileControler fileControler = FileControler.getFileControler();
                if(start == 0){
                   fileControler.WriteFile(bean);
                }

                if(bean.getSubjects().size() == 0){
                    MainActivity.v.setVisibility(View.GONE);
                    Toast.makeText(MyApplication.getContext(),"已经是最后一页",Toast.LENGTH_LONG).show();
                }else{
                    listener.UpdStart(NextStart = NextStart + 10, false);

                    EventBus.getDefault().post(bean);
                }

            }

            @Override
            public void onFailure(Call<JsonBean> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "fail", Toast.LENGTH_SHORT).show();
                ListSlideListener listener = ListSlideListener.getListener();
                listener.UpdStart(NextStart, true);
            }
        });

    }

}
