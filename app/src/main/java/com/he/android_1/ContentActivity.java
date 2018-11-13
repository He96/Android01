package com.he.android_1;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.he.android_1.divView.DivScrollChangedListenerView;
import com.he.android_1.model.PageInfo;
import com.he.android_1.utils.DBHelper;

import java.math.BigDecimal;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContentActivity extends AppCompatActivity {

    @BindView(R.id.titleTop)
    TextView titleTop;
    @BindView(R.id.titleContent)
    TextView titleContent;
    //@BindView(R.id.text_set)
    ImageView textSet;
    int size = 16;
    //@BindView(R.id.text_set_s)
    ImageView textSetS;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    volatile int position = 0;
    volatile int scrollY = -1;
    volatile int now, sum;
    PageInfo info;
    private Handler handler = new Handler();
    DBHelper dbHelper = new DBHelper(ContentActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        ButterKnife.bind(this);
        getContent();

    }

    private void getContent() {
        Intent intent = getIntent();
        info = (PageInfo) intent.getSerializableExtra("pageInfo");
        Log.e("info:", info.getTitle() + " id:" + info.getId());
        titleTop.setText(info.getTitle());
        titleContent.setText("  " + info.getContext());
        //跳转指定进度
        Map<String, String> map = dbHelper.getMap(String.valueOf(info.getId()));
        if (map.size() > 0) {
            String value = map.get(String.valueOf(info.getId()));
            double sum = Double.valueOf(value.split(":")[0]);
            double now = Double.valueOf(value.split(":")[1]);
            Log.e("进度:", "总值:" + sum + " 当前:" + now);
            position = (int) now;
        }
    }

    // @OnClick(R.id.text_set)
    public void onSet() {
        if (size == 16) {
            Toast toast = Toast.makeText(this, "放大字体", Toast.LENGTH_SHORT);
            toast.setText("放大字体");
            toast.show();
            size++;
        }

        if ((size - 16) > 5) {
            Toast toast1 = Toast.makeText(this, "你瞎啊!放这么大。", Toast.LENGTH_SHORT);
            toast1.setText("你瞎啊!放这么大。");
            toast1.show();
        } else {
            size++;
        }

        titleContent.setTextSize(size);
    }

    // @OnClick(R.id.text_set_s)
    public void onSets() {
        if (size == 16) {
            Toast toast = Toast.makeText(this, "缩小字体", Toast.LENGTH_SHORT);
            toast.setText("缩小字体");
            toast.show();
            size--;
        }
        if ((16 - size) > 3) {
            Toast toast = Toast.makeText(this, "再小你眼睛就看瞎了!", Toast.LENGTH_SHORT);
            toast.setText("再小你眼睛就看瞎了!");
            toast.show();
        } else {
            size--;
        }
        titleContent.setTextSize(size);
    }

    //滑动到指定位置
    public Runnable scrollTo = new Runnable() {
        @Override
        public void run() {
            scrollView.scrollTo(0, position);
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(scrollTo, 200);
    }

    public void runnableGetScrollY() {
        Log.e("testScrollY:", scrollView.getScrollY() + "");
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                sum = titleContent.getMeasuredHeight();//总高度
                int current = scrollView.getScrollY();//移动距离
                int scrH = scrollView.getMeasuredHeight();//控件高度
                now = current + scrH;
                Log.e("滑动监听", "开始滑动！ now:" + now + "  scrollY:" + scrollY);
                if((sum - now)>1000){
                    titleTop.setVisibility(View.VISIBLE);
                }else{
                    titleTop.setVisibility(View.GONE);
                }
            }
        });

    }

    private boolean current(int a, int b) {
        boolean flag = false;
        if (a == b) {
            flag = true;
        }
        if ((a - b) > 0 && (a - b) < 10) {
            flag = true;
        }
        if ((a - b) < 0 && (b - a) < 10) {
            flag = true;
        }
        return flag;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            getScroll();
            finish();
        }
        return super.onKeyDown(keyCode, event);

    }

    public void getScroll() {
        sum = titleContent.getMeasuredHeight();//总高度
        int current = scrollView.getScrollY();//移动距离
        int scrH = scrollView.getMeasuredHeight();//控件高度
        now = current + scrH;
        Log.e("滑动监听", "开始滑动！ now:" + now + "  scrollY:" + scrollY);

        Log.e("返回", "" + now);
        if (sum > 0) {
            //添加章节信息
            dbHelper.addMap(info.getId() + "", sum + ":" + now);
            //更新进度
            double newPlanned = new BigDecimal((double) now / (double) sum * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            if (current(now, sum)) {
                newPlanned = 100;
            }
            Log.e(" 进度:", newPlanned + " now:" + now + " sum:" + sum);
            Log.e("contentTime",System.currentTimeMillis()+"");
            info.setPlanned(newPlanned);
            dbHelper.update(info);
            Intent intent = getIntent();
            intent.putExtra("planned",newPlanned);
            setResult(RESULT_OK,intent);
            startActivityForResult(intent,202);
        }
    }
}
