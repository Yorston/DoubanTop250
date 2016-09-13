package com.zjm.doubantop.NetWorkConter;

import com.zjm.doubantop.JsonBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by B on 2016/9/13.
 */
public interface NetServiceCenter {

    @GET("v2/movie/top250")
    Call<JsonBean> GetJson(@Query("start") String start, @Query("count") String count);

}
