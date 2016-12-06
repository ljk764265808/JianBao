package us.mifeng.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import us.mifeng.been.AdverBeen;
import us.mifeng.view.AdversView_datials;

/**
 * Created by admin on 2016/11/29.
 */

public class DetailsActivity extends Activity implements View.OnClickListener {
    private Button mBtn_back_details;
    private ImageView mImg_guanzhu;
    private LinearLayout mLine_qq,mLine_wechat;
    private TextView mTv_qq,mTv_wechat,mTv_mobile;
    private TextView mTv_guanzhu;
    private LinearLayout mLine_guanzhu;
    private AdversView_datials Aview_details;
    private boolean GuanzhuFlag=true;
    private LinearLayout mLinear;
    private List<AdverBeen> list;
    private String urlStr="http://apis.juhe.cn/cook/query?key=6a487def5ec129b0a42d64c21bf6a0f0&menu=%E8%A5%BF%E7%BA%A2%E6%9F%BF&rn=10&pn=3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initView();
        getStr(urlStr);
        }

    private void initView() {
        mBtn_back_details= (Button) findViewById(R.id.mBtn_back_detalis);
        mImg_guanzhu= (ImageView) findViewById(R.id.mImg_guanzhu);
        mTv_guanzhu= (TextView) findViewById(R.id.mTv_guanzhu);
        mLine_guanzhu= (LinearLayout) findViewById(R.id.mLine_guanzhu);
        mLinear= (LinearLayout) findViewById(R.id.mAdver_details);
        mLine_wechat= (LinearLayout) findViewById(R.id.mLine_wechat);
        mLine_qq= (LinearLayout) findViewById(R.id.mLine_qq);
        mTv_mobile= (TextView) findViewById(R.id.mTv_mobile);
        mTv_wechat= (TextView) findViewById(R.id.mTv_wechat);
        mTv_qq= (TextView) findViewById(R.id.mTv_qq);

        mBtn_back_details.setOnClickListener(this);
        mLine_guanzhu.setOnClickListener(this);
        mTv_mobile.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int ID=view.getId();
        switch (ID){
            case R.id.mBtn_back_detalis:
                this.finish();
                break;
            case R.id.mLine_guanzhu:
                guanzhu();
                break;
            case R.id.mTv_mobile:
                String tel=mTv_mobile.getText().toString();
                // TODO Auto-generated method stub
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+tel));
                //让打电话的应用重新开启任务栈
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(intent);
                break;
        }
    }
    private void guanzhu(){
        if(GuanzhuFlag){
            mTv_guanzhu.setTextColor(Color.RED);
            mImg_guanzhu.setImageResource(R.mipmap.aixin_highlighted);
        }
        if (!GuanzhuFlag){
            mTv_guanzhu.setTextColor(Color.GRAY);
            mImg_guanzhu.setImageResource(R.mipmap.aixin_normal);
        }
        GuanzhuFlag=!GuanzhuFlag;
    }
    private Handler hand=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                String jsonStr=(String) msg.obj;
               // utils=new JsonUtils(DetailsActivity.this,jsonStr,1);
                //准备接受json解析封装好的list集合
               // list=utils.BuildList(jsonStr);
                if(list!=null){
                    Aview_details=new AdversView_datials(DetailsActivity.this, list);
                    mLinear.addView(Aview_details.getView());
                }
            }
        }
    };
    /*
	 * 开启子线程1 ---》获取json字符串串的方法
	 * */
    private void getStr(final String urlStr){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url=new URL(urlStr);
                    HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                    InputStream is = conn.getInputStream();
                    BufferedReader br=new BufferedReader(new InputStreamReader(is));
                    StringBuffer sb=new StringBuffer();
                    String tumb;
                    while((tumb=br.readLine())!=null){
                        sb.append(tumb);
                    }
                    br.close();
                    is.close();
                    String jsonStr=sb.toString();
                    Message msg=hand.obtainMessage();
                    msg.what=1;
                    msg.obj=jsonStr;
                    hand.sendMessage(msg);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
