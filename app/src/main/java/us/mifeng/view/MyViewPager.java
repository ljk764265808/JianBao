package us.mifeng.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

/**
 * Created by admin on 2016/11/29.
 */

/*
 * 自定义的ViewPager  处理：viewpager和Scrollview滑动冲突的问题
 * */
public class MyViewPager extends ViewPager {
    float curX = 0f;
    float downX = 0f;
    OnSingleTouchListener onSingleTouchListener;
    public MyViewPager(Context context) {
        super(context);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        curX = ev.getX();
        // TODO Auto-generated method stub
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downX = curX;
        }
        int curIndex = getCurrentItem();
        if (curIndex == 0) {
            if (downX <= curX) {
                getParent().requestDisallowInterceptTouchEvent(false);
            } else {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        } else if (curIndex == getAdapter().getCount() - 1) {
            if (downX >= curX) {
                getParent().requestDisallowInterceptTouchEvent(false);
            } else {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        } else {
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        return super.onTouchEvent(ev);
    }

    public void onSingleTouch() {
        if (onSingleTouchListener != null) {
            onSingleTouchListener.onSingleTouch();
        }
    }

    public interface OnSingleTouchListener {
        public void onSingleTouch();
    }

    public void setOnSingleTouchListner(
            OnSingleTouchListener onSingleTouchListener) {
        this.onSingleTouchListener = onSingleTouchListener;
    }

}
