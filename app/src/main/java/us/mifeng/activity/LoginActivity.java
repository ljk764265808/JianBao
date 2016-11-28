package us.mifeng.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.admin.bubajianbao.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by admin on 2016/11/24.
 */

public class LoginActivity extends Activity {

    @InjectView(R.id.ed_id)
    EditText edId;
    @InjectView(R.id.ed_password)
    EditText edPassword;
    @InjectView(R.id.btn_login)
    Button btnLogin;
    @InjectView(R.id.tv_noid)
    TextView tvNoid;
    @InjectView(R.id.box)
    CheckBox box;
    private String mName;
    private String mPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        mName = edId.getText().toString().trim();
        mPassword = edPassword.getText().toString().trim();
    }


    @OnClick({R.id.box, R.id.btn_login, R.id.tv_noid})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_noid:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.box:
                break;
            case R.id.btn_login:
                if (TextUtils.isEmpty(mName) || TextUtils.isEmpty(mPassword)) {

                } else {

                }
                break;
        }
    }


}
