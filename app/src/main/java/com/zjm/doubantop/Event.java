package com.zjm.doubantop;

import java.util.List;

/**
 * Created by B on 2016/7/28.
 */
public class Event {

    private JsonBean[] jsonBeen;
    private List<JsonBean> list;

    public void setList(List<JsonBean> list){
        this.list = list;
    }

    public void setJsonBeen(JsonBean[] jsonBeen){
        this.jsonBeen = jsonBeen;
    }

    public List<JsonBean> getList(){
        return list;
    }

    public JsonBean[] getJsonBeen(){
        return jsonBeen;
    }

}
