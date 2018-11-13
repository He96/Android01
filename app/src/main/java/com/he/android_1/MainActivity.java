package com.he.android_1;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.he.android_1.model.PageInfo;
import com.he.android_1.utils.DBHelper;
import com.he.android_1.utils.PageTitleManger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.weather)
    TextView weather;
    @BindView(R.id.clockTime)
    TextView clockTime;
    @BindView(R.id.calculator)
    TextView calculator;
    @BindView(R.id.wifiView)
    TextView wifiView;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.II_point_container)
    LinearLayout IIPointContainer;
    @BindView(R.id.txtInfo)
    TextView txtInfo;


    private int[] imageResIds;
    private ArrayList<ImageView> imageViewArrayList;
    private String[] contentDescs;
    private int previousSelectedPosition = 0;
    boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        initViews();
        initData();
        initAdapter();


        new Thread() {
            @Override
            public void run() {
                isRunning = true;
                while (isRunning) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //往下跳一位
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
                        }
                    });
                }
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    private void initViews() {
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int newPosition = position % imageViewArrayList.size();
                tvDesc.setText(contentDescs[newPosition]);
                IIPointContainer.getChildAt(previousSelectedPosition).setEnabled(false);
                IIPointContainer.getChildAt(newPosition).setEnabled(true);
                previousSelectedPosition = newPosition;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {
        imageResIds = new int[]{
                R.mipmap.ic_bg01, R.mipmap.ic_bg02, R.mipmap.ic_bg04, R.mipmap.ic_bg05
        };
        contentDescs = new String[]{
                "While there is life",
                "there is hope!",
                "welcome",
                "--mathew"
        };
        imageViewArrayList = new ArrayList<>();
        ImageView imageView;
        View pointView;
        LinearLayout.LayoutParams layoutParams;
        for (int i = 0; i < imageResIds.length; i++) {
            imageView = new ImageView(this);
            imageView.setBackgroundResource(imageResIds[i]);
            imageViewArrayList.add(imageView);
            //加小白点，指示器
            pointView = new View(this);
            pointView.setBackgroundResource(R.drawable.selector_bg_point);
            layoutParams = new LinearLayout.LayoutParams(5, 5);
            if (i != 0) {
                layoutParams.leftMargin = 10;
            }
            pointView.setEnabled(false);
            IIPointContainer.addView(pointView, layoutParams);
        }
    }

    private void initAdapter() {
        IIPointContainer.getChildAt(0).setEnabled(true);
        tvDesc.setText(contentDescs[0]);
        previousSelectedPosition = 0;
        //设置适配器
        viewpager.setAdapter(new MyAdapter());
        int pos = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % imageViewArrayList.size());
        viewpager.setCurrentItem(5000000);
    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int newPosition = position % imageViewArrayList.size();
            ImageView imageView = imageViewArrayList.get(newPosition);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    //时钟
    @OnClick(R.id.clockTime)
    public void toClock() {
        Intent intent = new Intent(MainActivity.this, TimeActivity.class);
        startActivity(intent);
    }

    //天气
    @OnClick(R.id.weather)
    public void toWeather() {
        Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
        startActivity(intent);
    }

    //计算器
    @OnClick(R.id.calculator)
    public void toCalculator() {
        Intent intent = new Intent(MainActivity.this, CalculatorActivity.class);
        startActivity(intent);
    }

    //wifi
    @OnClick(R.id.wifiView)
    public void toWifi() {
        Intent intent = new Intent(MainActivity.this, WifiActivity.class);
        startActivity(intent);
    }

    //小说
    @OnClick(R.id.txtInfo)
    public void toTxt() {
        Intent intent = new Intent(MainActivity.this, PageTableActivity.class);
        startActivity(intent);
    }
}
