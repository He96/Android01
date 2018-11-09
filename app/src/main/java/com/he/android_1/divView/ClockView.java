package com.he.android_1.divView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/11/3 0003.
 */

public class ClockView extends View {
    private Context mContext;
    private Paint mPaint;

    public ClockView(Context context) {
        super(context);
        this.mContext = context;
        initPaint();
    }


    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initPaint();
    }

    //初始化画笔
    private void initPaint() {
        mPaint = new Paint();
        //抗锯齿
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);//空心圆
        mPaint.setStrokeWidth(0);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //具体内容
        //实心圆
        //mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(2);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 3, mPaint);
        mPaint.setStrokeWidth(10);
        canvas.drawPoint(getWidth() / 2, getHeight() / 2, mPaint);
        mPaint.setStrokeWidth(1);
        canvas.translate(getWidth() / 2, getHeight() / 2);
        for (int i = 0; i < 360; i++) {
            //秒
            int a = 15;
            //分钟
            if (i % 6 == 0) {
                a = 30;
            }
            //小时
            if (i % 30 == 0) {
                a = 40;
            }
            canvas.drawLine(getWidth() / 3 - a, 0, getWidth() / 3, 0, mPaint);
            canvas.rotate(1);
        }
        Paint tPain = new Paint();
        tPain.setTextSize(35);
        tPain.setAntiAlias(true);
        tPain.setStyle(Paint.Style.FILL);
        Rect textBound = new Rect();
        tPain.getTextBounds("12", 0, "12".length(), textBound);
        canvas.drawText("12", -textBound.width() / 2, -(getWidth() / 3 - 80), tPain);
        for (int i = 1; i < 12; i++) {
            drawNum(canvas, i * 30, i + "", tPain);
        }
        proint(canvas);

    }

    //时分秒 针
    private void proint(Canvas canvas) {
        canvas.save();
        //秒针
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.rotate(mSecondDegree);
        canvas.drawLine(0, 0, 0, -190, paint);
        canvas.restore();
        //分钟
        canvas.save();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        canvas.rotate(mMinDegree);
        canvas.drawLine(0, 0, 0, -140, paint);
        canvas.restore();
        canvas.save();
        //时针
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(7);
        canvas.rotate(mHourDegree);
        canvas.drawLine(0, 0, 0, -100, paint);
        canvas.restore();
    }

    private float mSecondDegree;//秒针的度数
    private float mMinDegree;//分针的度数
    private float mHourDegree;//时针的度数
    private boolean mIsNight;
    private Timer timer = new Timer();
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (mSecondDegree == 360) {
                mSecondDegree = 0;
            }
            if (mMinDegree == 360) {
                mMinDegree = 0;
            }
            if (mHourDegree == 360) {
                mHourDegree = 0;
            }
            mSecondDegree += 6;
            mMinDegree += 0.1f;
            mHourDegree += 1.0f / 240;
            postInvalidate();//重绘
        }
    };

    public void start() {
        timer.schedule(timerTask, 0, 1000);
    }

    public void setTime(int hour, int min, int second) {
        if (hour >= 24 || hour < 0 || min >= 60 || min < 0 || second >= 60 || second < 0) {
            Toast.makeText(getContext(), "时间不合法",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (hour >= 12) {//这里我们采用24小时制
            mIsNight = true;//添加一个变量，用于记录是否为下午。
            mHourDegree = (hour + min * 1.0f / 60f + second * 1.0f / 3600f - 12) * 30f;
        } else {
            mIsNight = false;
            mHourDegree = (hour + min * 1.0f / 60f + second * 1.0f / 3600f) * 30f;
        }
        mMinDegree = (min + second * 1.0f / 60f) * 6f;
        mSecondDegree = second * 6f;
        invalidate();
    }

    private void drawNum(Canvas canvas, int degree, String text, Paint paint) {
        Rect textBound = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBound);
        canvas.rotate(degree);
        canvas.translate(0, 80 - getWidth() / 3);
        canvas.rotate(-degree);
        canvas.drawText(text, -textBound.width() / 2, textBound.height() / 2, paint);
        canvas.rotate(degree);
        canvas.translate(0, getWidth() / 3 - 80);
        canvas.rotate(-degree);
    }
}
