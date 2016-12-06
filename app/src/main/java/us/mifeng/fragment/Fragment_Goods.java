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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import us.mifeng.activity.DetailsActivity;
import us.mifeng.activity.R;
import us.mifeng.activity.SearchActivity;
import us.mifeng.activity.SeekActivity;
import us.mifeng.adapter.GoodsAdapter;
import us.mifeng.been.GoodsBeen;
import us.mifeng.view.AdversView;
import us.mifeng.view.MeasuredListView;
import us.mifeng.view.PullToRefreshScrollView;

import static java.lang.String.valueOf;

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
    private Map<String,Object> map=new HashMap<String,Object>();
    private GoodsAdapter adapter;
    private int max;
    private View v;
    private LinearLayout ll_search;
    private String listStr="http://192.168.4.188/Goods/app/item/list.json";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = View.inflate(getActivity(), R.layout.fragment_home, null);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        initView();
        initMap();
        post_file(listStr,map);
        return v;
    }



    private void initMap() {

        map.put("curPage","2");
    }


    private void initView() {
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
        mEdit_SP.setOnClickListener(this);
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
               list_goods= (List<GoodsBeen>) msg.obj;
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
    private void post_file(String listStr, Map<String, Object> map) {
        OkHttpClient ok=new OkHttpClient();
        // form 表单形式上传
        MultipartBody.Builder requestBody=new MultipartBody.Builder();
        requestBody.setType(MultipartBody.FORM);
        if(map!=null){
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
                if (entry.getValue()!=null&&!"".equals(entry.getValue()))
                {  requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));}
            }
        }
        MultipartBody build = requestBody.build();
        Request request = new Request.Builder()
                .url(listStr)
                .post(build)
                .build();
        Call call=ok.newCall(request);

        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call arg0, Response arg1) throws IOException {
                final String str=arg1.body().string();
                List<GoodsBeen> list=new ArrayList<GoodsBeen>();
                try {
                    JSONObject json = new JSONObject(str);
                    JSONObject data = json.getJSONObject("data");
                    JSONArray array = data.getJSONArray("list");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jo = array.getJSONObject(i);
                        GoodsBeen been = new GoodsBeen();
                        int id = jo.getInt("id");
                        String title = jo.getString("title");
                        String image = "http://192.168.4.188/Goods/uploads/"+jo.getString("image");
                        String price = jo.getString("price");
                        String issue_time = jo.getString("issue_time");
                        int state = jo.getInt("state");
                        been.setId(id);
                        been.setTitle(title);
                        been.setImage(image);
                        been.setIssue_time(issue_time);
                        been.setPrice(price);
                        been.setState(state);
                        list.add(been);
                        Message msg=hand.obtainMessage();
                        msg.what=3;
                        msg.obj=list;
                        hand.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /*
	 * listView的条目点击事件
	 * */

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
GoodsBeen been= (GoodsBeen) adapterView.getAdapter().getItem(i);
        intent.putExtra("id",been.getId());

        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_frombottom, R.anim.out_from);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
