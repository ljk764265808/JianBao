package us.mifeng.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import us.mifeng.utils.ShowUtils;

/**
 * Created by admin on 2016/11/23.
 */

public class RegisterActivity extends Activity {
    public static final String TAG = "RegisterActivity";
    @InjectView(R.id.ed_name)
    EditText edName;
    @InjectView(R.id.ed_password)
    EditText edPassword;
    @InjectView(R.id.ed_email)
    EditText edEmail;
    @InjectView(R.id.ed_code)
    EditText edCode;
    @InjectView(R.id.ed_weixin)
    EditText edWeixin;
    @InjectView(R.id.btn_next)
    Button btnNext;
    @InjectView(R.id.rg_gender)
    RadioGroup rgGender;
    @InjectView(R.id.ed_phone)
    EditText edPhone;
    private String gender;
    private String username;
    private String password;
    private String phone;
    private String code;

    private HashMap<String, String> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
        //获取RadioButton选中的值
        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                RadioButton rb = (RadioButton) findViewById(id);
                gender = rb.getText().toString().trim();
                map.put("gender", gender);
            }
        });
    }


    private void initView() {

        username = edName.getText().toString().trim();
        password = edPassword.getText().toString().trim();
        phone = edPhone.getText().toString().trim();
        code = edCode.getText().toString().trim();

        map.put("name", username);
        map.put("password", password);
        map.put("mobile", phone);
        map.put("code", code);
       /* ShareUtils.setData(Regis terActivity.this,"username",username);
        ShareUtils.setData(RegisterActivity.this,"password",password);,
        ShareUtils.setData(RegisterActivity.this,"phone",phone);
        ShareUtils.setData(RegisterActivity.this,"code",code);
        ShareUtils.setData(RegisterActivity.this,"gender",gender);*/
    }


    @OnClick(R.id.btn_next)
    public void onClick() {
        initView();
        Intent intent = new Intent(RegisterActivity.this, Register_PhotoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("map", map);
        intent.putExtras(bundle);
        startActivity(intent);


    }
}
