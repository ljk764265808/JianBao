package us.mifeng.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by xy on 2016/11/30.
 */
public class ShezhiActivity extends Activity {
    @InjectView(R.id.shezhi_zuojiantou)
    ImageView shezhiZuojiantou;
    @InjectView(R.id.shezhi_geren)
    LinearLayout shezhiGeren;
    @InjectView(R.id.shezhi_xiugaimima)
    LinearLayout shezhiXiugaimima;
    @InjectView(R.id.shehzi_bangding)
    LinearLayout shehziBangding;
    @InjectView(R.id.shezhi_yaoqing)
    LinearLayout shezhiYaoqing;
    @InjectView(R.id.shezhi_qingli)
    LinearLayout shezhiQingli;
    @InjectView(R.id.shezhi_fankui)
    LinearLayout shezhiFankui;
    @InjectView(R.id.shezhi_women)
    LinearLayout shezhiWomen;
    @InjectView(R.id.shezhi_tuichu)
    RelativeLayout shezhiTuichu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shezhi_main);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.shezhi_zuojiantou,  R.id.shezhi_geren,
            R.id.shezhi_xiugaimima, R.id.shehzi_bangding, R.id.shezhi_yaoqing,
            R.id.shezhi_qingli, R.id.shezhi_fankui, R.id.shezhi_women,
            R.id.shezhi_tuichu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shezhi_zuojiantou:
                finish();
                break;
            case R.id.shezhi_geren:
                startActivity(new Intent(ShezhiActivity.this,GeRenZiLiao.class));

                break;
            case R.id.shezhi_xiugaimima:
                break;
            case R.id.shehzi_bangding:
                break;
            case R.id.shezhi_yaoqing:
                break;
            case R.id.shezhi_qingli:
                break;
            case R.id.shezhi_fankui:
                break;
            case R.id.shezhi_women:
                break;
            case R.id.shezhi_tuichu:
                break;
        }
    }
}
