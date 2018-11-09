package com.he.android_1.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/11/8 0008.
 */

public class PageInfo implements Serializable {
    private int id;
    private String title;
    private int offSet;
    private int count;
    private String context;
    private double planned;

    public PageInfo() {
    }

    public PageInfo(int id, String title,String context, double planned) {
        this.id = id;
        this.title = title;
        this.context = context;
        this.planned = planned;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPlanned() {
        return planned;
    }

    public void setPlanned(double planned) {
        this.planned = planned;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOffSet() {
        return offSet;
    }

    public void setOffSet(int offSet) {
        this.offSet = offSet;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", offSet=" + offSet +
                ", count=" + count +
                ", context='" + context + '\'' +
                ", planned=" + planned +
                '}';
    }
}
