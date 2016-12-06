package us.mifeng.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 *
 * 自定义写的双指放大：原理----->根据两指间的距离的大小和手指按下的大小的比例判断图片的放大和缩小
 * */
public class ScaleImg extends View {
    private Bitmap bitmap;//要操作的图片
    private Matrix matrix=new Matrix();//矩阵
    private boolean IsFrist=true;//图片第一次设置的标志
    private boolean PointFlag=true;//手指触摸的标志：true：一个手指触摸  false：两个手指触摸
    private PointF pf=new PointF();//记录手指按下的坐标类
    private float dis=0f;//两指之间的距离
    private float midX=0f;//两指之间X距离的中心
    private float midY=0f;//两指之间Y距离之间的距离
    private float maxScale=5f;//最大的放大倍数
    private float minScale=0.5f;//最小的缩小倍数
    private float []arr=new float[9];//矩阵里面的9个值

    public ScaleImg(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //动态设置放大的最大倍数（0-5）
    public void setMaxScale(float maxScale){
        if (maxScale>5 || maxScale<0.5) {//规定缩放的范围，超出范围执行默认的（5-0.5）
            return;
        }
        this.maxScale=maxScale;
    }
    //动态设置缩小的倍数
    public void setMinScale(float minScale){
        if (minScale>5 || minScale<0.5) {
            return;
        }
        this.minScale=minScale;
    }
    //图片设置的方法
    public void setBitmap(Bitmap bitmap){
        IsFrist=true;//图片第一次进入的标志
        this.bitmap=bitmap;
        invalidate();//刷新界面
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (IsFrist & bitmap!=null) {
            float wScale=getMeasuredWidth()/bitmap.getWidth();//宽的倍数
            float yScale=getMeasuredHeight()/bitmap.getHeight();//高的倍数
            float Scale= Math.min(wScale, yScale);//取两个中最小值对图片进行缩放处理
            //累乘
            maxScale*=Scale;
            minScale*=Scale;
            matrix.postScale(Scale,Scale);//进行缩放
            //获取图片现在位置距离自定义View的中心需要移动的距离
            float disX=(getMeasuredWidth()-bitmap.getWidth()*Scale)/2;
            float disY=(getMeasuredHeight()-bitmap.getHeight()*Scale)/2;
            matrix.postTranslate(disX, disY);
        }
        IsFrist=false;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap!=null) {
            canvas.drawBitmap(bitmap, matrix, null);//将处理过的图片画到自定义view上
        }
    }
    //事件分发处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()== MotionEvent.ACTION_DOWN) {
            //手指按下
            PointFlag=true;
            //记录当前手指的坐标
            pf.x=event.getX();
            pf.y=event.getY();
        }else if(event.getAction()== MotionEvent.ACTION_POINTER_2_DOWN){
            //第二根手指按下
            PointFlag=false;
            //获取第二根手指的坐标
            float x=event.getX();
            float y=event.getY();
            //计算移动前两指之间的距离：勾股定理的应用
            //math.sqrt:开平方   math.pow:平方   --->arg0：要计算的数值：底数   arg1 ：平方或者是立方 ：幂值
            dis=(float) Math.sqrt(Math.pow(event.getX(0)-event.getX(1),2 )+
                    Math.pow(event.getY(0)-event.getY(1), 2));
            //获得两指距离的中心的坐标--->图片缩放以此坐标为中心
            midX=(event.getX(0)+event.getX(1))/2;
            midY=(event.getY(0)+event.getY(1))/2;
        }else if(event.getAction()== MotionEvent.ACTION_MOVE){
            //手指移动
            if (PointFlag && event.getPointerCount()==1) {
                //一个手指滑动
                float x=event.getX();//获取手指移动后的点的坐标
                float y=event.getY();//获取手指移动后的点的坐标
                //获取到末点需要移动的xy轴的距离
                float disx=x-pf.x;
                float disy=y-pf.y;
                //将末点的坐标作为再次移动的初始点
                pf.x=x;
                pf.y=y;
                //图片的位移
                matrix.postTranslate(disx, disy);
            }else if(!PointFlag && event.getPointerCount()==2){
                //两根手指的缩放
                //计算移动后两指间的距离
                float nowdis=(float) Math.sqrt(Math.pow(event.getX(0)-event.getX(1), 2)+
                        Math.pow(event.getY(0)-event.getY(1), 2));
                //获取图片缩放的倍数：大于1--->放大   小于1--->缩小
                float scale=nowdis/dis;
                //矩阵获取其中9个值
                matrix.getValues(arr);
                //将缩放后的两指将的距离当做下一次缩放的初始距离
                dis=nowdis;
                //缩放移动边界的判断
                if (arr[0]>=maxScale && scale>=1) {//放大越界
                    return true;
                }
                if (arr[0]<=minScale && scale<1) {//缩小越界
                    return true;
                }
                //设置缩放
                matrix.postScale(scale, scale, midX, midY);
            }
            invalidate();
        }
        return true;
    }
}
