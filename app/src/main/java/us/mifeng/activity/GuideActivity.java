package us.mifeng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

import us.mifeng.utils.ShareUtils;


public class GuideActivity extends AppCompatActivity {
    private ViewPager vp;
    private int name[] = {R.mipmap.phono2, R.mipmap.phono3, R.mipmap.phono1};
    private ImageView iv_1, iv_2, iv_3, iv;
    private ArrayList<View> list = new ArrayList<View>();
    private boolean Flag = false;
    private int index = 0;
    private Button bt_tiaozhuan;
    private String tag = "tag";
    private Handler myHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                if (Flag) {
                    return;
                }
                index++;
                if (index > list.size()) {
                    index = 0;
                }
            }
            //禁止掉自动轮播
//            vp.setCurrentItem(index);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        //进入登录页面，存放tag值
        ShareUtils.setData(GuideActivity.this,"tag",tag);
        initList();
        initView();
        initXiancheng();
    }

    private void initView() {
        vp = (ViewPager) findViewById(R.id.heheda);
        iv_1 = (ImageView) findViewById(R.id.iv_1);
        iv_2 = (ImageView) findViewById(R.id.iv_2);
        iv_3 = (ImageView) findViewById(R.id.iv_3);
        bt_tiaozhuan = (Button) findViewById(R.id.bt_tiaozhuan);

        bt_tiaozhuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this,LoginActivity.class));
            }
        });


        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                index = arg0;
                iv_1.setImageResource(R.mipmap.vate_n_can_y);
                iv_2.setImageResource(R.mipmap.vate_n_can_y);
                iv_3.setImageResource(R.mipmap.vate_n_can_y);
                switch (arg0) {
                    case 0:
                        iv_1.setImageResource(R.mipmap.vote_n_can_n);
                        break;
                    case 1:
                        iv_2.setImageResource(R.mipmap.vote_n_can_n);
                        break;
                    case 2:
                        iv_3.setImageResource(R.mipmap.vote_n_can_n);
                        break;

                }
                //滑动到最后一页，显示按钮
                if (arg0 == name.length - 1) {
                    bt_tiaozhuan.setVisibility(View.VISIBLE);
                    //不是最后一页，不显示按钮
                } else {
                    bt_tiaozhuan.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
//                Log.e("", "arg0--->arg0");
//                Log.e("", "arg1--->arg1");
//                Log.e("", "arg2--->arg2");
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                if (arg0 == ViewPager.SCROLL_STATE_IDLE) {
                    Flag = false;
                } else {
                    Flag = true;
                }
            }
        });
        vp.setAdapter(new myAdapter());
    }


    private void initList() {
        for (int i = 0; i < name.length; i++) {
            View v = View.inflate(this, R.layout.fuben_main, null);
            iv = (ImageView) v.findViewById(R.id.iv);
            iv.setImageResource(name[i]);
            list.add(v);
        }
    }

    public class myAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }
    }

    private void initXiancheng() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(3000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    myHandler.sendEmptyMessage(0);
                }
            }
        }).start();
    }
}

