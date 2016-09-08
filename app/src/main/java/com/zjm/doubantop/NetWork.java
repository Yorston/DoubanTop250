package com.zjm.doubantop;

import android.view.View;
import android.widget.Toast;

import com.zjm.doubantop.Activity.MainActivity;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by B on 2016/7/28.
 */
public class NetWork {

    public static int NextStart = 1;

    public interface NetService{
        @GET("v2/movie/top250")
        Call<JsonBean> GetJson(@Query("start") String start, @Query("count") String count);
    }

    public void GetMsg(final int start, int count){

        MainActivity.v.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.douban.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetService netService = retrofit.create(NetService.class);
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
                    listener.UpdStart(NextStart = NextStart + 10);

                    EventBus.getDefault().post(bean);
                }

            }

            @Override
            public void onFailure(Call<JsonBean> call, Throwable t) {

            }
        });

    }

}
