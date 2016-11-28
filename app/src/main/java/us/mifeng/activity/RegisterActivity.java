package us.mifeng.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by admin on 2016/11/23.
 */

public class RegisterActivity extends Activity {
    @InjectView(R.id.ed_name)
    EditText edName;
    @InjectView(R.id.ed_password)
    EditText edPassword;
    @InjectView(R.id.ed_info)
    EditText edInfo;
    @InjectView(R.id.ed_code)
    EditText edCode;
    @InjectView(R.id.btn_register)
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.btn_register)
    public void onClick() {
        finish();
    }
}
