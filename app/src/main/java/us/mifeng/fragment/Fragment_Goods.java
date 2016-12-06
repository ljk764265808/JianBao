package us.mifeng.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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

import us.mifeng.activity.DetailsActivity;
import us.mifeng.activity.R;
import us.mifeng.activity.SearchActivity;
import us.mifeng.activity.SeekActivity;
import us.mifeng.adapter.GoodsAdapter;
import us.mifeng.been.GoodsBeen;
import us.mifeng.utils.JsonUtils;
import us.mifeng.view.AdversView;
import us.mifeng.view.MeasuredListView;
import us.mifeng.view.PullToRefreshScrollView;

import static android.R.attr.id;

/**
 * Created by k on 2016/11/24.
 */

public class Fragment_Goods extends Fragment implements PullToRefreshScrollView.Pull_To_Load, View.OnClickListener, AdapterView.OnItemClickListener {
    private EditText mEdit_SP;
    private PullToRefreshScrollView mSc;//自定义的上下拉刷新
    private View centerView;
    private LinearLayout mLinear;//轮播的控件
    private MeasuredListView mLv;//自定义的listView
    private List<GoodsBeen> list_goods;
    private List<GoodsBeen> list_first = new ArrayList<GoodsBeen>();
    private GoodsAdapter adapter;
    private JsonUtils utils;
    private int max;
    private View v;
    private LinearLayout ll_search;
    private String listStr="http://192.168.4.188/Goods/app/item/list.json?token=FEB8083697FD41608336D5331EC21C98&curPage=1";
//    private String listhead="http://192.168.4.188/Goods/app/item/list.json?token=";
//    private String token;
//    private String  curPage;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = View.inflate(getActivity(), R.layout.fragment_home, null);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        initView();
        return v;
    }




    private void initView() {
        getListStr(listStr);
        ll_search = (LinearLayout) v.findViewById(R.id.ll_search);
        mEdit_SP = (EditText) v.findViewById(R.id.mEdit_SP);
        mSc = (PullToRefreshScrollView) v.findViewById(R.id.mSC);
        mSc.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        centerView = View.inflate(getActivity(), R.layout.view_center, null);
        mSc.setCenterView(centerView);
        mLinear = (LinearLayout) centerView.findViewById(R.id.mAdver);
        mEdit_SP.setInputType(InputType.TYPE_NULL);
        mEdit_SP.setOnClickListener(this);
        mLinear.addView(new AdversView(getActivity()).getView());
        mLv = (MeasuredListView) centerView.findViewById(R.id.mLv);


        //mLv.setFocusable(false);//不获取焦点

        mSc.setCall(this);
        mSc.scrollTo(0,0);
        ll_search.setOnClickListener(this);
        mLv.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int ID=view.getId();
        switch (ID){
            case R.id.ll_search:
                //Log.e(TAG, "onClick: +++ ");
                mEdit_SP.setInputType(InputType.TYPE_CLASS_TEXT);
                mEdit_SP.setFocusableInTouchMode(true);
                InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(mEdit_SP,0);

                break;
//            case R.id.mEdit_SP:
//                //Log.e(TAG, "onClick: +++ ");
//                mEdit_SP.setInputType(InputType.TYPE_CLASS_TEXT);
//                mEdit_SP.setFocusableInTouchMode(true);
//                InputMethodManager inputMethodManager2 = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputMethodManager2.showSoftInput(mEdit_SP,0);
//                break;
            case R.id.mEdit_SP:
                Intent intent=new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
//                String sp = mEdit_SP.getText().toString();
//                if (sp.equals("")) {
//                    Toast.makeText(getActivity(), "请输入您要搜索的商品", Toast.LENGTH_SHORT).show();
//                } else {
//                    thread();
//
//                }
                break;
        }
    }

    private void thread() {
        //String ShangPin = mEdit_SP.getText().toString();
        hand.sendEmptyMessage(2);
    }

    private Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                mSc.complate();
                //再次向服务器发送请求，然后加载更多
                loadMore();
                adapter.notifyDataSetChanged();
                if(adapter.getCount()==max){
                    Toast.makeText(getActivity(), "已经滑到底部，暂无更新", Toast.LENGTH_SHORT).show();
                    mLv.setSelection(max);
                }
            }
            if (msg.what == 1) {
                mSc.complate();
                Toast.makeText(getActivity(), "联网失败，稍后刷新！", Toast.LENGTH_SHORT).show();
            }
            if (msg.what == 2) {
                String sp = mEdit_SP.getText().toString().trim();
                Intent intent = new Intent(getActivity(), SeekActivity.class);
                intent.putExtra("ShangPin", sp);
               startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_frombottom, R.anim.out_from);
            }
            if(msg.what==3){
                String goodsStr1= (String) msg.obj;
                utils=new JsonUtils(getActivity(),goodsStr1,2);
                list_goods=utils.BuildList(goodsStr1);
                for(int i=0;i<5;i++){
                    GoodsBeen been=list_goods.get(i);
                    list_first.add(been);
                }
                if(adapter!=null){
                    adapter.RefrashAdapter(list_first);
                }else{
                    adapter=new GoodsAdapter(getActivity(),list_first);
                    mLv.setAdapter(adapter);

                }
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
                list_first.add(been);
            }
        }else{
            for(int i=count;i<max;i++){
                GoodsBeen been=list_goods.get(i);
                list_first.add(been);
            }
        }
    }
    /*
	 * 开启子线程2 ---》获取json字符串串的方法
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
                    msg.what=3;
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
    /*
	 * listView的条目点击事件
	 * */

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_frombottom, R.anim.out_from);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
