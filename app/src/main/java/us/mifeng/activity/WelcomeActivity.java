package us.mifeng.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import butterknife.ButterKnife;
import us.mifeng.utils.ShareUtils;

/**
 * Created by admin on 2016/12/12.
 */

public class WelcomeActivity extends Activity {
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                String tag = (String) ShareUtils.getData(WelcomeActivity.this, "tag", "");
                if (tag.equals("")) {
                    startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
                } else {
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.inject(this);
        initSkip();
    }

    private void initSkip() {
        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);

            }
        }).start();
    }


}
