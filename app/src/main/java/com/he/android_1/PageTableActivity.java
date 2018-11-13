package com.he.android_1;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.he.android_1.utils.DBHelper;

import java.math.BigDecimal;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PageTableActivity extends AppCompatActivity {

    @BindView(R.id.jy_bt)
    ImageView jyBt;

    @BindView(R.id.text_progress)
    TextView textProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_table);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

    }

    @OnClick(R.id.jy_bt)
    public void click() {
        if (new DBHelper(PageTableActivity.this).getPageList(null).size() < 1) {
            Toast toast1 = Toast.makeText(this, "书籍正在准备中，请稍等。", Toast.LENGTH_SHORT);
            toast1.setText("书籍正在准备中，请稍等。");
            toast1.show();
        }
        Intent intent = new Intent(PageTableActivity.this, TxtActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        textProgress.setText(progress());
    }

    public String progress() {
        try {
            DBHelper dbHelper = new DBHelper(PageTableActivity.this);
            double progress = 0;
            Map<String, String> stringMap = dbHelper.getMap("title");
            if (stringMap.size() > 0 && stringMap.containsKey("title")) {
                BigDecimal current = new BigDecimal(stringMap.get("title"));
                int sum = dbHelper.getPageList(null).size();
                if (dbHelper.getPageList(null).size() <= 0) {
                    sum = 1;
                }
                BigDecimal count = new BigDecimal(sum);
                progress = current.divide(count, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            return "已阅读" + new BigDecimal(progress * 100).setScale(2, BigDecimal.ROUND_HALF_UP) + "%";
        } catch (Exception e) {
            e.printStackTrace();
            return "已阅读0.00%";
        }
    }
}
