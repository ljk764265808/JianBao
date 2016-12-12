package us.mifeng.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import us.mifeng.app.MInterface;
import us.mifeng.base.Login;
import us.mifeng.utils.MOkHttp;
import us.mifeng.utils.ShareUtils;
import us.mifeng.utils.ShowUtils;

import static us.mifeng.utils.ShareUtils.getData;

/**
 * Created by yst on 2016/11/24.
 */
/*
* 登陆页面
* */
public class LoginActivity extends Activity {
    public static final String TAG = "LoginActivity";
    @InjectView(R.id.ed_id)
    EditText edId;
    @InjectView(R.id.ed_password)
    EditText edPassword;
    @InjectView(R.id.box)
    CheckBox box;
    @InjectView(R.id.btn_login)
    Button btnLogin;
    @InjectView(R.id.tv_noid)
    TextView tvNoid;
    @InjectView(R.id.tv_look)
    TextView tvLook;
    private String username;
    private String password;
    private String status;
    private HashMap<String, String> map;
    private Login login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        isKeepPassword();
        initLister();
    }

    private void initLister() {
        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                initView();
                if (isChecked == true) {
                    ShareUtils.setData(LoginActivity.this, "judgeText", true);
                    ShareUtils.setData(LoginActivity.this, "username", username);
                    ShareUtils.setData(LoginActivity.this, "password", password);
                } else {
                    ShareUtils.setData(LoginActivity.this, "judgeText", false);
                    ShareUtils.setData(LoginActivity.this, "username", "");
                    ShareUtils.setData(LoginActivity.this, "password", "                   ");

                }
            }
        });
    }

    private void isKeepPassword() {
        //从配置文件中取用户名密码的键值对
        //若第一运行，则取出的键值对为所设置的默认值
        Boolean judgeText = (Boolean) getData(LoginActivity.this, "judgeText", false);// 选中状态
        String strUserName = (String) getData(LoginActivity.this, "username", "0");// 用户名
        String strPassword = (String) ShareUtils.getData(LoginActivity.this, "password", "0");// 密码*/


        if (judgeText == true) {
            box.setChecked(true);
            edId.setText(strUserName);
            edPassword.setText(strPassword);
        } else {
            box.setChecked(false);
            edId.setText("");
            edPassword.setText("");
        }
    }

    private void postData() {

        String path = MInterface.zhuji + MInterface.denglu;
        Log.e(TAG, "run: " + path);
        map = new HashMap<>();
        //把用户输入的文本携带到工具类进行联网操作，通过联网中判断是否为正确的token值
        map.put("username", username);
        map.put("password", password);
        MOkHttp.post(LoginActivity.this, path, map, Login.class);
        MOkHttp.setGetEntityCallBack(new MOkHttp.GetEntityCallBack() {
            @Override
            public void getEntity(Object obj) {
                login = (Login) obj;
                if (login != null) {
                    status = login.getStatus();
                     /*通过用户输入的用户名密码获取token值* */
                    juegeStatus();
                    String token = login.getToken();
                    Log.e("TAG", "getEntity: token值：" + token);
                    //存放token值
                    if (token != null) {
                        ShareUtils.setData(LoginActivity.this, "token", token);
                    }
                }
            }
        });
    }


    private void juegeStatus() {

        //再次判断请求回来的code码是否为200，正确的话跳转，并清空这个集合，这样的话避免重复以前的数据
        if (status.equals("200")) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            //map.clear();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ShowUtils.show(LoginActivity.this, "欢迎使用");
                }
            });
        } else if (status.equals("303")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ShowUtils.show(LoginActivity.this, "用户不存在，请重新输入");
                }
            });
        } else if (status.equals("304")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ShowUtils.show(LoginActivity.this, "您输入的密码有误，请重新输入密码");
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ShowUtils.show(LoginActivity.this, "请正确输入");
                }
            });
        }
    }


    @OnClick({R.id.btn_login, R.id.tv_noid,R.id.tv_look})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                initView();
                postData();
                break;
            case R.id.tv_noid:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.tv_look:
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                break;
        }
    }

    private void initView() {
        username = edId.getText().toString().trim();
        password = edPassword.getText().toString().trim();
    }
}