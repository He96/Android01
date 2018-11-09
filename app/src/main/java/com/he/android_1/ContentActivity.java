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

import com.he.android_1.model.PageInfo;
import com.he.android_1.utils.DBHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContentActivity extends AppCompatActivity {

    @BindView(R.id.titleTop)
    TextView titleTop;
    @BindView(R.id.titleContent)
    TextView titleContent;
    @BindView(R.id.text_set)
    ImageView textSet;

    int size = 16;
    @BindView(R.id.text_set_s)
    ImageView textSetS;

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
        PageInfo info = (PageInfo) intent.getSerializableExtra("pageInfo");
        Log.e("info:", info.getTitle()+" id:"+info.getId());
        titleTop.setText(info.getTitle());
        titleContent.setText("  " + info.getContext());
        DBHelper dbHelper = new DBHelper(ContentActivity.this);
        info.setPlanned(info.getPlanned()+20);
        dbHelper.update(info);
    }

    @OnClick(R.id.text_set)
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

    @OnClick(R.id.text_set_s)
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
}
