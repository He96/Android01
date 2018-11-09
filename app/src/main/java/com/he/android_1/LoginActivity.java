package com.he.android_1;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.he.android_1.model.Result;
import com.he.android_1.service.LoginService;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.userName)
    EditText userName;
    @BindView(R.id.passWord)
    EditText passWord;
    @BindView(R.id.loginBtn)
    Button loginBtn;
    @BindView(R.id.qq_btn)
    Button qqBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @OnClick(R.id.loginBtn)
    public void loginClicked() {
        checkLogin();
    }

    @OnClick(R.id.qq_btn)
    public void qqClicked(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    protected void checkLogin() {
        String name = userName.getText().toString();
        String pass = passWord.getText().toString();
        Log.e("request", name + ":" + pass);
        if (StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(pass)) {
            //
            request(name, pass);

        } else {
            Toast.makeText(this, "用户名或密码不能为空!", Toast.LENGTH_SHORT).show();
        }
    }

    public void request(String userName, String passWord) {
        //Toast.makeText(this,userName+":"+passWord,Toast.LENGTH_SHORT).show();
        JSONObject object = new JSONObject();

        try {
            object.put("userName", userName);
            object.put("passWord", passWord);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), object.toString());

        //创建retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.zswwlj.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //创建网络请求接口实例
        LoginService loginService = retrofit.create(LoginService.class);
        Call<Result> call = loginService.login(body);
        call.enqueue(new Callback<Result>() {
            //成功回调
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Log.e("结果", "JSON:" + response.body().toString());
                if (response.body().getCode() == 1) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            //失败回调
            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                System.out.println("请求失败！");
            }
        });
    }


}
