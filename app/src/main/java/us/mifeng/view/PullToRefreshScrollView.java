package us.mifeng.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import us.mifeng.activity.R;


/**
 * 带上下拉刷新功能的scrollview：---->一般加载在fragment中，首页
 */
@SuppressLint("NewApi")
public class PullToRefreshScrollView extends ScrollView {
    private LinearLayout mLinear;//scrollview中添加的linear
    private final static int DONE = 0;//完成状态
    private final static int RELASE_REFRESH = 2;//释放刷新状态
    private final static int REFRESHING = 3;//刷新状态
    private final static int RELASE_LOAD = 5;//释放加载状态
    private final static int LOADING = 6;//加载状态
    private int mode = DONE;//当前所处的状态
    private View headView;//头部的布局
    private View footView;//底部的布局
    private ImageView headImg;//头部的图片
    private ImageView footImg;//底部的图片
    private AnimationDrawable draw_P;
    private AnimationDrawable draw_u;
    private int height = 0;//头部的高度
    private PointF startPF = new PointF();
    private int scale = 3;
    private LinearLayout centerView;//scrollview里显示的布局：其中包括广告轮播  listview 及其它
    private Pull_To_Load call;
    private int[] drawables = {R.mipmap.icon_loaing_frame_1, R.mipmap.icon_loaing_frame_2, R.mipmap.icon_loaing_frame_3,
            R.mipmap.icon_loaing_frame_4, R.mipmap.icon_loaing_frame_5, R.mipmap.icon_loaing_frame_6, R.mipmap.icon_loaing_frame_7,
            R.mipmap.icon_loaing_frame_8, R.mipmap.icon_loaing_frame_9, R.mipmap.icon_loaing_frame_10, R.mipmap.icon_loaing_frame_11,
            R.mipmap.icon_loaing_frame_12, R.mipmap.icon_loaing_frame_13,};

    public PullToRefreshScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAnim();
        initView();
    }

    private void initView() {
        //设置scrollview的参数
        mLinear = new LinearLayout(getContext());
        mLinear.setOrientation(LinearLayout.VERTICAL);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mLinear.setLayoutParams(lp);
        //设置scrollview中显示的布局的参数
        centerView = new LinearLayout(getContext());
        centerView.setOrientation(LinearLayout.VERTICAL);
        centerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        //设置头尾布局的参数和动画
        headView = View.inflate(getContext(), R.layout.view_head, null);
        footView = View.inflate(getContext(), R.layout.view_head, null);

        headImg = (ImageView) headView.findViewById(R.id.mSCImg);
        footImg = (ImageView) footView.findViewById(R.id.mSCImg);

        headView.measure(0, 0);
        height = headView.getMeasuredHeight();

        headView.setPadding(0, -height, 0, 0);
        footView.setPadding(0, 0, 0, -height);

        headImg.setImageDrawable(draw_P);
        footImg.setImageDrawable(draw_u);
        //将子布局依次添加到scrollview的linear中：scrollview是单布局，想要多布局使用必须添加linear
        mLinear.addView(headView);
        mLinear.addView(centerView);
        mLinear.addView(footView);
        addView(mLinear);
    }

    //设置centerView的方法
    public void setCenterView(View v) {
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        centerView.addView(v);
    }

    //初始化动画的方法
    private void initAnim() {
        draw_P = new AnimationDrawable();
        draw_u = new AnimationDrawable();
        for (int i : drawables) {
            draw_P.addFrame(getResources().getDrawable(i), 20);
            draw_u.addFrame(getResources().getDrawable(i), 20);
        }
        draw_P.setOneShot(false);
        draw_u.setOneShot(false);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            startPF.y = ev.getY();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            PointF pf = new PointF();
            pf.y = ev.getY();
            float disY = (pf.y - startPF.y) / scale;
            //进行判断，上拉或者下滑
            if (getScrollY() <= 0 && disY > 0) {//下拉
                headView.setPadding(0, (int) (disY - height), 0, 0);
                if (disY > height) {
                    mode = RELASE_REFRESH;
                } else {
                    mode = DONE;
                }
                return true;
            }
            //computeVerticalScrollRange：获取当前view的整体的垂直高度
            if (getScrollY() + getHeight() >= PullToRefreshScrollView.this.computeVerticalScrollRange() && disY < 0) {//�ϻ�
                footView.setPadding(0, 0, 0, (int) (Math.abs(disY) - height));
                if (Math.abs(disY) > height) {
                    mode = RELASE_LOAD;
                } else {
                    mode = DONE;
                }
            }
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {

            select();
            if (mode == REFRESHING || mode == RELASE_LOAD || mode == RELASE_REFRESH) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    //根据当前mode的状态进行判断是刷新还是加载
    private void select() {
        switch (mode) {
            case DONE:
                draw_P.stop();
                draw_u.stop();
                headView.setPadding(0, -height, 0, 0);
                footView.setPadding(0, 0, 0, -height);
                break;
            case RELASE_REFRESH:
                draw_P.start();
                headView.setPadding(0, 0, 0, 0);
                //刷新
                call.Refresh();
                break;
            case RELASE_LOAD:
                draw_u.start();
                footView.setPadding(0, 0, 0, 0);
                //加载
                call.Load();
                break;
        }
    }

    //结束的方法
    public void complate() {
        mode = DONE;
        select();
    }

    //回调接口
    public interface Pull_To_Load {
        public void Load();

        public void Refresh();
    }

    public void setCall(Pull_To_Load call) {
        this.call = call;
    }
}
