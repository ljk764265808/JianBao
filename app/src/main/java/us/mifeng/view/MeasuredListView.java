package us.mifeng.view;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/11/22.
 */

public class MeasuredListView extends ListView {
    public MeasuredListView(Context context) {
        super(context);
    }

    public MeasuredListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MeasuredListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
