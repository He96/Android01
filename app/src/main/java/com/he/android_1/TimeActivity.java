package com.he.android_1;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

import com.he.android_1.divView.ClockView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimeActivity extends AppCompatActivity {
    @BindView(R.id.showTime)
    TextView showTime;
    private ClockView clockView;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        clockView = (ClockView) findViewById(R.id.time_view);
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR);//小时
        int minute = cal.get(Calendar.MINUTE);//分
        int second = cal.get(Calendar.SECOND);//秒
        handler = new Handler(){
            public void handleMessage(Message msg){
                showTime.setText((String)msg.obj);
            }
        };
        Threads thread = new Threads();
        thread.start();
        clockView.setTime(hour, minute, second);
        clockView.start();
    }

    class Threads extends Thread{
        @Override
        public void run(){
            try{
                while (true){
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss E");
                    String time = dateFormat.format(new Date());
                    handler.sendMessage(handler.obtainMessage(100,time));
                    Thread.sleep(1000);
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

}

