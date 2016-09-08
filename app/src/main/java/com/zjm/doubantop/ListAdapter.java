package com.zjm.doubantop;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by B on 2016/7/28.
 */
public class ListAdapter extends BaseAdapter{

    private List<HashMap> list = new ArrayList<>();
    private DisplayImageOptions option;
    private static ListAdapter adapter = new ListAdapter();
    private ListAdapter(){
        option = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .showImageOnLoading(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }
    public static ListAdapter getAdapter(){
        return adapter;
    }

    /*public ListAdapter(List<HashMap> list){
        this.list = list;
        option = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }*/

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
        ViewHolder holder = null;
        LayoutInflater inflacter = LayoutInflater.from(viewGroup.getContext());
        if(convertview == null){
            holder = new ViewHolder();
            convertview = inflacter.inflate(R.layout.listitem, null);
            holder.imageview = (ImageView) convertview.findViewById(R.id.imageView);
            holder.textview = (TextView) convertview.findViewById(R.id.textView);
            convertview.setTag(holder);
        }else {
            holder = (ViewHolder) convertview.getTag();
        }


        holder.textview.setText((String)list.get(i).get("BaseInfo"));
        //holder.imageview.setImageBitmap((Bitmap) list.get(i).get("BitMap"));
        holder.imageview.setTag(list.get(i).get("ImageUrl"));
        final ViewHolder finalHolder = holder;

        //ImageLoader.getInstance().displayImage( (String)list.get(i).get("ImageUrl"), holder.imageview,option);

        ImageLoader.getInstance().loadImage((String)list.get(i).get("ImageUrl"), option, new SimpleImageLoadingListener(){
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

    public static class ViewHolder{
        public TextView textview;
        public ImageView imageview;
    }

}
