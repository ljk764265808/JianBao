package us.mifeng.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import us.mifeng.app.Constant;
import us.mifeng.base.Login;
import us.mifeng.utils.MShow;
import us.mifeng.utils.OkHttpUtils;

import static android.content.ContentValues.TAG;

/**
 * Created by yst on 2016/11/24.
 */
/*
* 登陆页面
* */
public class LoginActivity extends Activity {

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
    private SharedPreferences pre;
    private String username;
    private String password;
    private String status;
    private HashMap<String, String> map;
    private SharedPreferences settings;
    private Login login;
    private String token;
    private SharedPreferences tokens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        settings = getSharedPreferences("setting_infos", MODE_PRIVATE);
        tokens = getSharedPreferences("tokens", MODE_PRIVATE);
        isKeepPassword();
        initLister();

    }

    private void initLister() {
        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked == true) {
                    settings.edit().putString("judgeText", "yes")
                            .putString("username", edId.getText().toString())
                            .putString("password", edPassword.getText().toString())

                            .commit();
                    Log.e("TAG", "getEntity: " + token + "1111111111111111111111111111111");
                    MShow.show(LoginActivity.this, edId.getText().toString() + edPassword.getText().toString());
                } else {
                    settings.edit().putString("judgetext", "no")
                            .putString("username", "")
                            .putString("password", "")
                            .putString("token", "")
                            .commit();
                }
            }
        });
    }

    private void isKeepPassword() {
        //从配置文件中取用户名密码的键值对
        //若第一运行，则取出的键值对为所设置的默认值
        SharedPreferences settings = getSharedPreferences("setting_infos", 0);
        String strJudge = settings.getString("judgeText", "no");// 选中状态
        String strUserName = settings.getString("username", "");// 用户名
        String strPassword = settings.getString("password", "");// 密码
        if (strJudge.equals("yes")) {
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

                String path = Constant.HOST + Constant.LOGIN_PATH;
                Log.e(TAG, "run: " + path);
                map = new HashMap<>();
                //把用户输入的文本携带到工具类进行联网操作，通过联网中判断是否为正确的token值
                map.put("username", username);
                map.put("password", password);
                OkHttpUtils.setGetEntityCallBack(new OkHttpUtils.GetEntityCallBack() {
                    @Override
                    public void getEntity(Object obj) {
                        login = (Login) obj;
                        juegeStatus();
                    }
                });
                OkHttpUtils.post(LoginActivity.this, path, map, Login.class);
            }



    private void juegeStatus() {
        if (login != null) {
            status = login.getStatus();
             /*通过用户输入的用户名密码获取token值* */
            //token = login.getData().getToken();
            //tokens.edit().putString("token",token).commit();/*把token值存入本地*/
            //再次判断请求回来的code码是否为200，正确的话跳转，并清空这个集合，这样的话避免重复以前的数据
            if (status.equals("200")) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                map.clear();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MShow.show(LoginActivity.this, "欢迎使用");
                    }
                });
            } else if (status.equals("303")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MShow.show(LoginActivity.this, "用户不存在，请重新输入");
                    }
                });
            } else if (status.equals("304")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MShow.show(LoginActivity.this, "您输入的密码有误，请重新输入密码");
                    }
                });
            }
        }
    }

    @OnClick({R.id.btn_login, R.id.tv_noid})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                username = edId.getText().toString().trim();
                password = edPassword.getText().toString().trim();
                postData();
                break;
            case R.id.tv_noid:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
        }
    }
}
