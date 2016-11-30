package us.mifeng.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private PullToRefreshScrollView mSc;
    private View centerView;
    private LinearLayout mLinear;
    private MeasuredListView mLv;
    private List<String> list = new ArrayList<String>();
    private GoodsAdapter adapter;
    private View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = View.inflate(getActivity(), R.layout.fragment_home, null);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        initView();
        intList();
        return v;
    }

    private void intList() {
        for (int i = 0; i < 30; i++) {
            list.add("");
        }
    }

    private void initView() {
        mEdit_SP = (EditText) v.findViewById(R.id.mEdit_SP);
        mBtn_seek = (Button) v.findViewById(R.id.mBtn_seek);
        mSc = (PullToRefreshScrollView) v.findViewById(R.id.mSC);
        mSc.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        centerView = View.inflate(getActivity(), R.layout.view_center, null);
        mSc.setCenterView(centerView);
        mLinear = (LinearLayout) centerView.findViewById(R.id.mAdver);

        mLinear.addView(new AdversView(getActivity()).getView());
        mLv = (MeasuredListView) centerView.findViewById(R.id.mLv);
        adapter = new GoodsAdapter(getActivity(), list);
        mLv.setAdapter(adapter);
        mLv.setFocusable(false);//不获取焦点
        mSc.MesureListHeight(mLv);
        mSc.setCall(this);
        mBtn_seek.setOnClickListener(this);
        mLv.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String sp=mEdit_SP.getText().toString();
        if(sp.equals("")){
            Toast.makeText(getActivity(),"输入您要搜索的商品",Toast.LENGTH_SHORT).show();
        }else{
            thread();
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
                String sp=mEdit_SP.getText().toString().trim();
                Intent intent = new Intent(getActivity(), SeekActivity.class);
                intent.putExtra("ShangPin", sp);
                startActivity(intent);
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
        for (int i = count; i < count + 30; i++) {
            list.add("第" + i + "条数据");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        startActivity(intent);
    }
}
