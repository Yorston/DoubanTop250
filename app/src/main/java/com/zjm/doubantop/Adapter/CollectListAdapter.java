package com.zjm.doubantop.Adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zjm.doubantop.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by B on 2016/9/8.
 */
public class CollectListAdapter extends BaseAdapter{

    private static CollectListAdapter collectListAdapter = new CollectListAdapter();
    private DisplayImageOptions option;
    private List<HashMap> list = new ArrayList<>();
    private CollectListAdapter(){
        option = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .showImageOnLoading(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }
    public static CollectListAdapter getAdapter(){
        return collectListAdapter;
    }

    public void setList(List<HashMap> list){
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertview, ViewGroup viewGroup) {

        Viewholder viewholder = null;
        LayoutInflater inflacter = LayoutInflater.from(viewGroup.getContext());
        if(convertview == null){
            viewholder = new Viewholder();
            convertview = inflacter.inflate(R.layout.listitem, null);
            viewholder.imageview = (ImageView) convertview.findViewById(R.id.imageView);
            viewholder.textview = (TextView) convertview.findViewById(R.id.textView);
            convertview.setTag(viewholder);
        }else {
            viewholder = (Viewholder) convertview.getTag();
        }

        viewholder.textview.setText((String)list.get(i).get("content"));
        //holder.imageview.setImageBitmap((Bitmap) list.get(i).get("BitMap"));
        viewholder.imageview.setTag(list.get(i).get("imageurl"));
        final Viewholder finalHolder = viewholder;

        ImageLoader.getInstance().loadImage((String)list.get(i).get("imageurl"), option, new SimpleImageLoadingListener(){
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                if(finalHolder.imageview.getTag() != null&&finalHolder.imageview.getTag().equals(imageUri)){
                    finalHolder.imageview.setImageBitmap(loadedImage);
                }
            }
        });
        return convertview;
    }

    public static class Viewholder{
        public TextView textview;
        public ImageView imageview;
    }

}
