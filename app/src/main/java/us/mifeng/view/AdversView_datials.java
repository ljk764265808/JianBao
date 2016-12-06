package us.mifeng.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

import us.mifeng.activity.R;
import us.mifeng.been.AdverBeen;

/**
 * Created by admin on 2016/11/29.
 */

public class AdversView_datials implements ViewPager.OnPageChangeListener {
    private Context ctx;
    private RelativeLayout relat;//放置vp和点的的容器
    private MyViewPager mVp;//viewpager
    private LinearLayout mLinear;//存放底部显示点的容器
    private boolean touchFlag = false;//触摸锁
    private Bitmap pointS;//选中的点
    private Bitmap pointN;//没有选中的点
    private List<View> Vplist = new ArrayList<View>();
    private List<AdverBeen> list;
    private VpAdapter adapter;
    private int index = 0;//当前vp的索引页
    private boolean ThreadFlag = true;
    private View[] views;
    private TimeThread tiemThread;

    public AdversView_datials(Context ctx, List<AdverBeen> list) {
        this.ctx = ctx;
        this.list = list;
        views = new View[list.size()];
        initList();
        initView();
        initPoint();
        tiemThread = new TimeThread();
        tiemThread.start();
    }

    /*
     * 获取当前父容器的方法
     * */
    public RelativeLayout getView() {
        return relat;
    }

    /*
     * 关闭线程的方法
     * */
    public void stopTime() {
        ThreadFlag = false;
    }

    //初始化数据
    private void initList() {
        for (int i = 0; i < list.size(); i++) {
            // Log.e("", "1111111"+list.size());
            ImageView mImg = new ImageView(ctx);
            mImg.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mImg.setScaleType(ImageView.ScaleType.FIT_XY);
            mImg.setImageResource(R.mipmap.tupian1);
            //图片的异步加载

            Glide.with(ctx).load(list.get(i).getImgpath()).into(mImg);
            //Log.e("", "22222"+list.get(i).getImgpath());
            Vplist.add(mImg);
            //Log.e("", "33333333"+Vplist.size());
            views[i] = mImg;
        }


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

    private void initView() {
        relat = new RelativeLayout(ctx);
        mVp = new MyViewPager(ctx);
        //mVp.setOffscreenPageLimit(6);
        mVp.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        mLinear = new LinearLayout(ctx);
        mLinear.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mLinear.setLayoutParams(lp);
        //设置adapter
        adapter = new VpAdapter();
        //设置viewpager页面切换的速度
//        ViewPagerScroller scroll=new ViewPagerScroller(ctx);
//        scroll.initViewPagerScroll(mVp);
        mVp.setAdapter(adapter);

        relat.addView(mVp);
        relat.addView(mLinear);
        //读取圆点图片
        pointN = BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.community_ad_banner_point_nor);
        pointS = BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.community_ad_banner_point_sel);

        //给vp设置滑动监听
        mVp.setOnPageChangeListener(this);

    }

    Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (touchFlag) {
                    return;
                }
                if (!ThreadFlag) {
                    return;
                }
                index++;
                mVp.setCurrentItem(index);
            }
        }
    };

    private class VpAdapter extends PagerAdapter {
        private int[] arr;

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            container.addView(Vplist.get(position % Vplist.size()));

            Vplist.get(position % list.size()).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent=new Intent(ctx, BitmapActivity.class);
//                    int i=arr[position%list.size()];
//                    intent.putExtra("i",i);
//                    ctx.startActivity(intent);

                }
            });
            return Vplist.get(position % Vplist.size());
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            try {
                ((ViewPager) container).removeView(Vplist.get(position % Vplist.size()));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /*
     * vp的滑动监听
     * */
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
        for (int i = 0; i < Vplist.size(); i++) {
            ImageView mImg = (ImageView) mLinear.getChildAt(i);
            if (i != (arg0 % Vplist.size())) {
                mImg.setImageBitmap(pointN);
            } else {
                mImg.setImageBitmap(pointS);
            }
        }
    }

    /**
     * 子线程内部类
     */
    public class TimeThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (ThreadFlag) {
                try {
                    Thread.sleep(3000);
                    hand.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }


}
