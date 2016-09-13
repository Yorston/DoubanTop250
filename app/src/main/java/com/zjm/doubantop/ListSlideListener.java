package com.zjm.doubantop;

import android.widget.AbsListView;

import com.zjm.doubantop.NetWorkConter.NetWork;

/**
 * Created by B on 2016/8/1.
 */
public class ListSlideListener implements AbsListView.OnScrollListener{

    private NetWork netWork = NetWork.getNetWork();
    private int start;
    public static int old_start = 1;
    private int count = 10;

    private ListSlideListener(){}
    private static ListSlideListener listSlideListener = new ListSlideListener();
    public static ListSlideListener getListener(){
        return  listSlideListener;
    }

    public void UpdStart(int start, boolean reset_oldstart){
        System.out.println("setStart  " + start);
        if(reset_oldstart){
            old_start = old_start - 10;
        }
        this.start = start;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

        if ((i2 - 1 == i + i1)&&(i + i1 != 0)) {
            //System.out.println("start  " + start + "old_start  " + old_start);
            if (start != old_start) {
                old_start = start;
                //start = start + 10;
                netWork.GetMsg(start, count);
                System.out.println("加载下一页...");
            }
        }
    }
}
