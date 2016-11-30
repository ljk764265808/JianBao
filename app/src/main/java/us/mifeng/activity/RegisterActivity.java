package us.mifeng.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import us.mifeng.utils.MCamera;


/**
 * Created by admin on 2016/11/23.
 */

public class RegisterActivity extends Activity {

    private static final int TAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;
    private static final int SCALE = 5;//照片缩小比例
    @InjectView(R.id.iv_photo)
    ImageView ivPhoto;
    @InjectView(R.id.ed_name)
    EditText edName;
    @InjectView(R.id.ed_password)
    EditText edPassword;
    @InjectView(R.id.ed_phone)
    EditText edPhone;
    @InjectView(R.id.ed_code)
    EditText edCode;
    @InjectView(R.id.ibtn_camera)
    ImageButton ibtnCamera;
    @InjectView(R.id.btn_register)
    Button btnRegister;
    @InjectView(R.id.rg_gender)
    RadioGroup rgGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_simple);
        ButterKnife.inject(this);
        initView();

    }

    @OnClick({R.id.ibtn_camera, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_camera:
                MCamera.showPicturePicker(this);
                break;
            case R.id.btn_register:
                finish();
                break;
        }
    }

    private void initView() {
        String strUsername = edName.getText().toString().trim();
        String strPassword = edPassword.getText().toString().trim();
        String strPhone = edPhone.getText().toString().trim();
        String strCode = edCode.getText().toString().trim();
        //获取RadioButton选中的值
        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                RadioButton rb = (RadioButton) findViewById(id);
                String gender = rb.getText().toString().trim();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MCamera.activityResult(requestCode,resultCode,data,ivPhoto,this);
    }
}
