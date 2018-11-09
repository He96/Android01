package com.he.android_1.utils;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.he.android_1.R;
import com.he.android_1.model.PageInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/11/8 0008.
 */

public class PageTitleManger {
    //private static SqlConn sqlConn = SqlConn.getInstance();

    public static List<PageInfo> getTitleList(Context context) {
        List<PageInfo> titleList = new ArrayList<>();
        try {
            //getClass().getResourceAsStream("/assets/jy.txt");
            InputStream inputStream = context.getAssets().open("jy.txt");
            InputStreamReader reader = new InputStreamReader(inputStream, "utf8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String lineTxt = null;
            int offSet = 0;//章节所在行
            int count = 1;//章节数
            PageInfo pageInfo;
            StringBuffer info = new StringBuffer();
            boolean flag;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                pageInfo = new PageInfo();
                offSet++;
                if (lineTxt.equals("开头") || (lineTxt.contains("第") && lineTxt.contains("章 "))) {
                    pageInfo.setCount(count);
                    pageInfo.setOffSet(offSet);
                    pageInfo.setTitle(lineTxt.trim());
                    if (titleList.size() > 0) {
                        titleList.get(count - 2).setContext(info.toString());
                        pageInfo.setContext(info.toString());
                        info = new StringBuffer();
                    }
                    titleList.add(pageInfo);
                    count++;
                    flag = false;
                } else {
                    flag = true;
                }
                if (flag) {
                    lineTxt += "\r\n";
                    info.append(lineTxt);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return titleList;

    }


}
