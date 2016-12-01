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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import us.mifeng.activity.DetailsActivity;
import us.mifeng.activity.R;
import us.mifeng.activity.SeekActivity;
import us.mifeng.adapter.GoodsAdapter;
import us.mifeng.view.AdversView;
import us.mifeng.view.MeasuredListView;
import us.mifeng.view.PullToRefreshScrollView;

/**
 * Created by k on 2016/11/24.
 */

public class Fragment_Goods extends Fragment implements PullToRefreshScrollView.Pull_To_Load, View.OnClickListener, AdapterView.OnItemClickListener {
    private EditText mEdit_SP;
    private Button mBtn_seek;
    private PullToRefreshScrollView mSc;//自定义的上下拉刷新
    private View centerView;
    private LinearLayout mLinear;//轮播的控件
    private MeasuredListView mLv;//自定义的listView
    private List<String> list = new ArrayList<String>();
    private GoodsAdapter adapter;
    private View v;
    private LinearLayout ll_search;
    //private static final String TAG = "Fragment_Goods";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = View.inflate(getActivity(), R.layout.fragment_home, null);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        initView();
        return v;
    }

    private void initList() {

        for (int i = 0; i < 5; i++) {
            list.add("");
        }
    }

    private void initView() {
        ll_search = (LinearLayout) v.findViewById(R.id.ll_search);
        mEdit_SP = (EditText) v.findViewById(R.id.mEdit_SP);
        mBtn_seek = (Button) v.findViewById(R.id.mBtn_seek);
        mSc = (PullToRefreshScrollView) v.findViewById(R.id.mSC);
        mSc.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        centerView = View.inflate(getActivity(), R.layout.view_center, null);
        mSc.setCenterView(centerView);
        mLinear = (LinearLayout) centerView.findViewById(R.id.mAdver);
        mEdit_SP.setInputType(InputType.TYPE_NULL);
        mEdit_SP.setOnClickListener(this);
        mLinear.addView(new AdversView(getActivity()).getView());
        mLv = (MeasuredListView) centerView.findViewById(R.id.mLv);
        adapter = new GoodsAdapter(getActivity(), list);
        mLv.setAdapter(adapter);
        //mLv.setFocusable(false);//不获取焦点

        mSc.MesureListHeight(mLv);
        mSc.setCall(this);
        mSc.scrollTo(0,0);
        mBtn_seek.setOnClickListener(this);
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
            case R.id.mEdit_SP:
                //Log.e(TAG, "onClick: +++ ");
                mEdit_SP.setInputType(InputType.TYPE_CLASS_TEXT);
                mEdit_SP.setFocusableInTouchMode(true);
                InputMethodManager inputMethodManager2 = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager2.showSoftInput(mEdit_SP,0);
                break;
            case R.id.mBtn_seek:
                String sp = mEdit_SP.getText().toString();
                if (sp.equals("")) {
                    Toast.makeText(getActivity(), "请输入您要搜索的商品", Toast.LENGTH_SHORT).show();
                } else {
                    thread();

                }
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
                //加载
                loadMore();
                adapter.notifyDataSetChanged();
                mSc.MesureListHeight(mLv);
            } else if (msg.what == 1) {
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
        int count = adapter.getCount();
        for (int i = count; i < count + 5; i++) {
            list.add("第" + i + "条数据");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_frombottom, R.anim.out_from);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initList();
    }
}
