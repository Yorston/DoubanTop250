package com.zjm.doubantop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by B on 2016/8/2.
 */
public class FileControler {

    private FileControler() {
    }

    private static FileControler fileControler = new FileControler();

    public static FileControler getFileControler() {
        return fileControler;
    }

    public void WriteFile(JsonBean bean) {
        File file = new File(MyApplication.getContext().getCacheDir(), "DoubanTopCache.data");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (file.canWrite()/*&&start == 0*/) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(bean);
                objectOutputStream.close();
                fileOutputStream.close();
                //System.out.println("文件写入成功!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public JsonBean GetFile() {
        JsonBean bean = null;
        File file = new File(MyApplication.getContext().getCacheDir(), "DoubanTopCache.data");
        if (file.exists() && file.canRead()/*&&start == 0*/) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                bean = (JsonBean) objectInputStream.readObject();
                //EventBus.getDefault().post(bean);
                objectInputStream.close();
                fileInputStream.close();
                //System.out.println("文件读取成功");
                return bean;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bean;
    }

}
