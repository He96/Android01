package com.he.android_1;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.he.android_1.model.PageInfo;
import com.he.android_1.utils.DBHelper;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TxtActivity extends AppCompatActivity {
    //    private static SqlConn sqlConn = SqlConn.getInstance();
    @BindView(R.id.listView)
    ListView lv;
    @BindView(R.id.order_by_title)
    ImageView orderByTitle;
    private List<PageInfo> mList;
    boolean flag = true;
    int titleId = 0;
    private DBHelper dbHelper = new DBHelper(TxtActivity.this);
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txt);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        initUI();
        mList = dbHelper.getPageList(null);
        initAdapter();
    }

    @OnClick(R.id.order_by_title)
    public void toOrderBy() {
        Log.e("排序", flag + "");
        String args = "id desc";
        if (!flag) {
            args = "id asc";
        }
        mList = dbHelper.getPageList(args);
        flag = !flag;
        initAdapter();
    }

    private void initUI() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int item, long id) {
                Log.e("click item:", item + " id:" + id);
                Intent intent = new Intent(TxtActivity.this, ContentActivity.class);
                intent.putExtra("pageInfo", mList.get(item));
                dbHelper.addMap("title", String.valueOf(item));
                titleId = item;
                startActivityForResult(intent, 202);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("he", "requestCode:" + requestCode);
        Log.e("TxtTime", System.currentTimeMillis() + "");
        if (requestCode == 202 && resultCode == RESULT_OK) {
            double planned = data.getDoubleExtra("planned", 0);
            Bundle bundle = data.getExtras();
            double plan = bundle.getDouble("planned");
            Log.e("txt planned", planned + " plan:" + plan);
            mList.get(titleId).setPlanned(planned);
            newsAdapter.notifyDataSetChanged();
        } else {
            Log.e("data", "data " + data);
        }
    }

    private void initAdapter() {
        newsAdapter = new NewsAdapter();
        lv.setAdapter(newsAdapter);
    }

    public class NewsAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //没有缓存
            Holder holder;
            if (convertView == null) {
                holder = new Holder();
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_layout, null);
                holder.textView = convertView.findViewById(R.id.text_title);
                holder.textView1 = convertView.findViewById(R.id.text_flag);
                convertView.setTag(holder);
            } else {
                //存在缓存
                holder = (Holder) convertView.getTag();
            }

            PageInfo pageInfo = mList.get(position);
            holder.textView.setText(pageInfo.getTitle());
            if (pageInfo.getPlanned() > 0.01) {
                holder.textView1.setText("已读:" + pageInfo.getPlanned() + "%");
            }
            return convertView;
        }

        class Holder {
            TextView textView;
            TextView textView1;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Map<String, String> maps = dbHelper.getMap("title");
        if (maps.size() > 0 && maps.containsKey("title")) {
            //滑动到指定位置
            lv.setSelection(Integer.valueOf(maps.get("title").trim()));
        }
    }

}
