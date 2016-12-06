package us.mifeng.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import us.mifeng.adapter.GoodsAdapter;
import us.mifeng.been.GoodsBeen;
import us.mifeng.view.MeasuredListView;
import us.mifeng.view.PullToRefreshScrollView;

/**
 * Created by admin on 2016/11/28.
 */

public class SeekActivity extends Activity implements View.OnClickListener, PullToRefreshScrollView.Pull_To_Load, AdapterView.OnItemClickListener {
    private EditText mEdit_SP_seek;
    private Button mBtn_seek_seek;
    private LinearLayout mBtn_back_seek;
    private LinearLayout mLine_sp;
    private PullToRefreshScrollView mSc_seek;
    private View centerView;
    private MeasuredListView mLv_seek;
    private List<GoodsBeen> list_goods;
    private List<GoodsBeen> list_first = new ArrayList<GoodsBeen>();
    private GoodsAdapter adapter;
    private int max;
    private String listStr="http://192.168.4.188/Goods/app/item/list.json?token=E4B7D7027E4E4F4BB1EADC70EE963472&curPage=1";
//    private String listhead="http://192.168.4.188/Goods/app/item/list.json?token=";
//    private String token;
//    private String  curPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek);
        initView();
    }



    private void initView() {
        getListStr(listStr);
        mBtn_back_seek= (LinearLayout) findViewById(R.id.mBtn_back_seek);
        mEdit_SP_seek = (EditText) findViewById(R.id.mEdit_SP_seek);
        mBtn_seek_seek = (Button) findViewById(R.id.mBtn_seek_seek);
        mLine_sp= (LinearLayout) findViewById(R.id.mLine_sp);

        mSc_seek = (PullToRefreshScrollView) findViewById(R.id.mSC_seek);
       mEdit_SP_seek.setInputType(InputType.TYPE_NULL);
        mSc_seek.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        centerView = View.inflate(this, R.layout.view_seek, null);
        mSc_seek.setCenterView(centerView);
        mLv_seek = (MeasuredListView) centerView.findViewById(R.id.mLv_seek);
        mSc_seek.setCall(this);

        String SP = getIntent().getStringExtra("ShangPin");
        mEdit_SP_seek.setText(SP);

        mBtn_back_seek.setOnClickListener(this);
        mBtn_seek_seek.setOnClickListener(this);
        mLine_sp.setOnClickListener(this);
        mLv_seek.setOnItemClickListener(this);
        mEdit_SP_seek.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
    int ID=view.getId();
        switch (ID){
            case R.id.mBtn_back_seek:
                this.finish();
                break;
            case R.id.mLine_sp:
                mEdit_SP_seek.setInputType(InputType.TYPE_CLASS_TEXT);
                mEdit_SP_seek.setFocusableInTouchMode(true);
                InputMethodManager inputMethodManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(mEdit_SP_seek,0);
                mEdit_SP_seek.setSelection(mEdit_SP_seek.getText().toString().trim().length());
                break;
             case R.id.mEdit_SP_seek:
                mEdit_SP_seek.setInputType(InputType.TYPE_CLASS_TEXT);
                mEdit_SP_seek.setFocusableInTouchMode(true);
                InputMethodManager inputMethodManager2 = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager2.showSoftInput(mEdit_SP_seek,0);
                mEdit_SP_seek.setSelection(mEdit_SP_seek.getText().toString().trim().length());
                break;
            case R.id.mBtn_seek_seek:

                break;
        }
    }

    private Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                mSc_seek.complate();
                //再次向服务器发送请求，然后加载更多
                loadMore();
                adapter.notifyDataSetChanged();
                if(adapter.getCount()==max){
                    Toast.makeText(SeekActivity.this, "已经滑到底部，暂无更新", Toast.LENGTH_SHORT).show();
                    mLv_seek.setSelection(max);
                }
            }
            if (msg.what == 1) {
                mSc_seek.complate();
                Toast.makeText(SeekActivity.this, "联网失败，稍后刷新！", Toast.LENGTH_SHORT).show();
            }
            if(msg.what==2){
//                String goodsStr1= (String) msg.obj;
//                utils=new JsonUtils(SeekActivity.this,goodsStr1);
//                list_goods=utils.BuildList(goodsStr1);
//                for(int i=0;i<5;i++){
//                    GoodsBeen been=list_goods.get(i);
//                    list_first.add(been);
//                }
//                if(adapter!=null){
//                    adapter.RefrashAdapter(list_first);
//                }else{
//                    adapter=new GoodsAdapter(SeekActivity.this,list_first);
//                mLv_seek.setAdapter(adapter);
//
//            }
            }
        }
    };

    @Override
    public void Load() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                hand.sendEmptyMessage(0);
            }
        }).start();
    }

    @Override
    public void Refresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                hand.sendEmptyMessage(1);
            }
        }).start();
    }

    private void loadMore() {
        max=list_goods.size();
        int count=adapter.getCount();
        if((count+5)<max){
            for(int i=count;i<count+5;i++){
                GoodsBeen been=list_goods.get(i);
                list_goods.add(been);
            }
        }else{
            for(int i=count;i<max;i++){
                GoodsBeen been=list_goods.get(i);
                list_first.add(been);
            }
        }
    }
    /*
      * 开启子线程 ---》获取json字符串串的方法
      * */
    private void getListStr(final String listStr){
        new Thread(new Runnable() {
            @Override
            public void run() {
//                token="E4B7D7027E4E4F4BB1EADC70EE963472";
//                curPage="1";
//                listStr=listhead+token+curPage;
                try {
                    URL url=new URL(listStr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    InputStream is = conn.getInputStream();
                    BufferedReader br=new BufferedReader(new InputStreamReader(is));
                    StringBuffer sb=new StringBuffer();
                    String line;
                    while ((line=br.readLine())!=null){
                        sb.append(line);
                    }
                    br.close();
                    is.close();
                    String jsonStr=sb.toString();
                    Message msg=hand.obtainMessage();
                    msg.what=2;
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(this,DetailsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_frombottom, R.anim.out_from);
    }
}
