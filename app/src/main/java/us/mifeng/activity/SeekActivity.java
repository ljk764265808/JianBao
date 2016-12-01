package us.mifeng.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import us.mifeng.adapter.GoodsAdapter;
import us.mifeng.view.MeasuredListView;
import us.mifeng.view.PullToRefreshScrollView;

/**
 * Created by admin on 2016/11/28.
 */

public class SeekActivity extends Activity implements View.OnClickListener, PullToRefreshScrollView.Pull_To_Load, AdapterView.OnItemClickListener {
    private EditText mEdit_SP_seek;
    private Button mBtn_seek_seek;
    private Button mBtn_back_seek;
    private PullToRefreshScrollView mSc_seek;
    private View centerView;
    private MeasuredListView mLv_seek;
    private List<String> list = new ArrayList<String>();
    private GoodsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek);
        initView();
        intList();
    }

    private void intList() {
        for (int i = 0; i < 30; i++) {
            list.add("");
        }
    }

    private void initView() {
        mBtn_back_seek= (Button) findViewById(R.id.mBtn_back_seek);
        mEdit_SP_seek = (EditText) findViewById(R.id.mEdit_SP_seek);
        mBtn_seek_seek = (Button) findViewById(R.id.mBtn_seek_seek);
        mSc_seek = (PullToRefreshScrollView) findViewById(R.id.mSC_seek);
        mSc_seek.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        centerView = View.inflate(this, R.layout.view_seek, null);
        mSc_seek.setCenterView(centerView);
        mLv_seek = (MeasuredListView) centerView.findViewById(R.id.mLv_seek);
        adapter = new GoodsAdapter(this, list);
        mLv_seek.setAdapter(adapter);
        mSc_seek.MesureListHeight(mLv_seek);
        mSc_seek.setCall(this);

        String SP = getIntent().getStringExtra("ShangPin");
        mEdit_SP_seek.setText(SP);

        mBtn_back_seek.setOnClickListener(this);
        mBtn_seek_seek.setOnClickListener(this);
        mLv_seek.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
    int ID=view.getId();
        switch (ID){
            case R.id.mBtn_back_seek:
                this.finish();
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
                //加载
                loadMore();
                adapter.notifyDataSetChanged();
                mSc_seek.MesureListHeight(mLv_seek);
            } else if (msg.what == 1) {
                mSc_seek.complate();
                Toast.makeText(SeekActivity.this, "联网失败，稍后刷新！", Toast.LENGTH_SHORT).show();
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
            list.add("");
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(this,DetailsActivity.class);
        startActivity(intent);
    }
}
