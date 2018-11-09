package com.he.android_1;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.he.android_1.utils.TipsModal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WifiActivity extends AppCompatActivity {

    @BindView(R.id.getWifiList)
    Button getWifiList;

    WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        ButterKnife.bind(this);
        wifiCon();
    }

    @OnClick(R.id.getWifiList)
    public void buttonClick() {
        Toast.makeText(this,"点击了",Toast.LENGTH_SHORT).show();
        if (wifiManager != null) {
            List<ScanResult> resultList = wifiManager.getScanResults();
            while (resultList.size()<1){
                wifiManager.startScan();
                resultList = wifiManager.getScanResults();
            }
            for (ScanResult result : resultList) {
                Log.e("WiFi:", " 名称:" + result.SSID + " 信号:" + result.level);
            }
        }else{
            TipsModal.showModal("提示","wifi暂未开启",WifiActivity.this);
        }
    }

    private void wifiCon() {
        wifiManager = (WifiManager) getApplicationContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        boolean isOpen = wifiManager.setWifiEnabled(true);

        switch (wifiManager.getWifiState()) {
            //wifi正在关闭
            case WifiManager.WIFI_STATE_DISABLING:
                break;
            //wifi关闭
            case WifiManager.WIFI_STATE_DISABLED:
                break;
            //wifi正在开启
            case WifiManager.WIFI_STATE_ENABLING:
                break;
            //wifi开启
            case WifiManager.WIFI_STATE_ENABLED:
                Log.e("wifi开启:", "成功！");
                wifiManager.startScan();//开始扫描
                break;
            //wifi未知
            case WifiManager.WIFI_STATE_UNKNOWN:
                break;
        }
    }
}
