package com.he.android_1.divView;

import java.io.UnsupportedEncodingException;

/**
 * 页数计算
 * Created by mathew on 2018/11/10 0010.
 */

public class ProcessText {
    private int pages;//总页数
    private final int size = 900;//每页字数
    private int count;//总数
    private long currentpage;//当前页面

    public ProcessText(String title,int currentpage){
        try {
            count = title.getBytes("UTF-8").length;
            pages = count/size;
            this.currentpage = currentpage;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //定位字节位置 根据页数定位文本的位置
    private void seek(int page){

    }

    private String read(){
        byte[] chs = new byte[size+30];
        return null;
    }
}
