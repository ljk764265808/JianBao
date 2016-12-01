package us.mifeng.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import us.mifeng.activity.R;

/**
 * 广告轮播
 */
public class AdversView implements OnPageChangeListener {
    private ViewPager mVp;
    private Context ctx;
    private RelativeLayout relat;
    private LinearLayout mLinear;//存放底部显示点的容器
    private Bitmap pointS;//选中的点
    private Bitmap pointN;//没有选中的点
    private List<View> list = new ArrayList<View>();
    private int[] arr = {R.mipmap.tupian1, R.mipmap.tupian2, R.mipmap.tupian3, R.mipmap.tupian4};
    private boolean touchFlag = false;//触摸屏
    private boolean threadFlag = true;//控制发送信息的锁
    View[] views = new View[arr.length];
    private int index = 0;
    private VpAdapter vpAdapter;

    public AdversView(Context ctx) {
        this.ctx = ctx;
        initList();
        initView();
        initPoint();
        TimeThread thread = new TimeThread();
        thread.start();
    }

    //获取当前View的方法
    public View getView() {
        return relat;
    }

    private void initView() {
        relat = new RelativeLayout(ctx);
        mVp = new ViewPager(ctx);
        vpAdapter = new VpAdapter();
        mVp.setAdapter(vpAdapter);
        mVp.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        mLinear = new LinearLayout(ctx);
        mLinear.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mLinear.setLayoutParams(lp);

        relat.addView(mVp);
        relat.addView(mLinear);
        //读取圆点图片
        pointN = BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.community_ad_banner_point_nor);
        pointS = BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.community_ad_banner_point_sel);

        mVp.setOnPageChangeListener(this);
    }

    /*
     * 初始化底部圆点的方法
	 * */
        private void initPoint() {
            for (int i = 0; i < list.size(); i++) {
                ImageView img = new ImageView(ctx);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.rightMargin = 10;
                lp.bottomMargin = 30;
                img.setLayoutParams(lp);
                if (i == 0) {
                    img.setImageBitmap(pointS);
                } else {
                    img.setImageBitmap(pointN);
                }
                mLinear.addView(img, i);
            }
    }

    //初始化数据源
    private void initList() {
        for (int i = 0; i < arr.length; i++) {
            ImageView mImg = new ImageView(ctx);
            mImg.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mImg.setScaleType(ScaleType.FIT_XY);
            mImg.setImageResource(arr[i]);
            list.add(mImg);
            views[i] = mImg;
        }
    }

    private class VpAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(list.get(position % views.length));
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(list.get(position % views.length));
            return list.get(position % views.length);
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        if (arg0 == ViewPager.SCROLL_STATE_IDLE) {
            touchFlag = false;
        } else {
            touchFlag = true;
        }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        index = arg0;
        //根据当前viewpager页卡切换底部对应点
        for (int i = 0; i < list.size(); i++) {
            ImageView mImg = (ImageView) mLinear.getChildAt(i);
            if (i != (arg0 % list.size())) {
                mImg.setImageBitmap(pointN);
            } else {
                mImg.setImageBitmap(pointS);
            }
        }
    }

    private Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (touchFlag) {
                    return;
                }
                if (!threadFlag) {
                    return;
                }
                index++;
                mVp.setCurrentItem(index);
            }
        }
    };

    private class TimeThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (threadFlag) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hand.sendEmptyMessage(0);
            }
        }
    }
}
