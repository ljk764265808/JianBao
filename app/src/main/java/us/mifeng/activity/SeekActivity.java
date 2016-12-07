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
import android.widget.ImageView;
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
import us.mifeng.adapter.GoodsAdapter;
import us.mifeng.been.GoodsBeen;
import us.mifeng.view.MeasuredListView;
import us.mifeng.view.PullToRefreshScrollView;

import static java.lang.String.valueOf;
import static us.mifeng.activity.R.id.mBtn_seek_seek;

/**
 * Created by admin on 2016/11/28.
 */

public class SeekActivity extends Activity implements View.OnClickListener, PullToRefreshScrollView.Pull_To_Load, AdapterView.OnItemClickListener {
    private EditText mEdit_SP_seek;
    private ImageView mBack_seek;
    private PullToRefreshScrollView mSc_seek;
    private View centerView;
    private MeasuredListView mLv_seek;
    private List<GoodsBeen> list_goods;
    private List<GoodsBeen> list_first = new ArrayList<GoodsBeen>();
    private GoodsAdapter adapter;
    private int max;
    private Map<String, Object> map = new HashMap<String, Object>();
    private String listStr = "http://192.168.4.188/Goods/app/item/list.json?token=E4B7D7027E4E4F4BB1EADC70EE963472&curPage=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek);
        adapter = new GoodsAdapter(SeekActivity.this, list_first);
        initView();
        initMap();
        post_file(listStr, map);
    }

    private void initMap() {
        String SPstr = getIntent().getStringExtra("SPstr");
            map.put("title", SPstr);
            map.put("curPage", "1");
    }

    private void initView() {
        mBack_seek= (ImageView) findViewById(R.id.mBack_seek);
        mEdit_SP_seek = (EditText) findViewById(R.id.mEdit_SP_seek);
        mBtn_seek_seek = (Button) findViewById(mBtn_seek_seek);
        mLine_sp = (LinearLayout) findViewById(R.id.mLine_sp);
        mSc_seek = (PullToRefreshScrollView) findViewById(R.id.mSC_seek);
        mEdit_SP_seek.setInputType(InputType.TYPE_NULL);
        mSc_seek.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        centerView = View.inflate(this, R.layout.view_seek, null);
        mSc_seek.setCenterView(centerView);
        mLv_seek = (MeasuredListView) centerView.findViewById(R.id.mLv_seek);
        mSc_seek.setCall(this);
        String SP = getIntent().getStringExtra("SPstr");
        mEdit_SP_seek.setText(SP);
        mBtn_back_seek.setOnClickListener(this);
        mBtn_seek_seek.setOnClickListener(this);
        mLine_sp.setOnClickListener(this);
        mBack_seek.setOnClickListener(this);
        mLv_seek.setOnItemClickListener(this);
        mEdit_SP_seek.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int ID = view.getId();
        switch (ID) {
            case R.id.mBack_seek:
                this.finish();
                break;
            case R.id.mEdit_SP_seek:
                mEdit_SP_seek.setInputType(InputType.TYPE_CLASS_TEXT);
                mEdit_SP_seek.setFocusableInTouchMode(true);
                InputMethodManager inputMethodManager2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager2.showSoftInput(mEdit_SP_seek, 0);
                Intent intent = new Intent(SeekActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_frombottom, R.anim.out_from);
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
                if (adapter.getCount() == max) {
                    Toast.makeText(SeekActivity.this, "已经滑到底部，暂无更新", Toast.LENGTH_SHORT).show();
                    mLv_seek.setSelection(max);
                }
            }
            if (msg.what == 1) {
                mSc_seek.complate();
                Toast.makeText(SeekActivity.this, "联网失败，稍后刷新！", Toast.LENGTH_SHORT).show();
            }
            if (msg.what == 2) {
                list_goods = (List<GoodsBeen>) msg.obj;
                for (int i = 0; i < 5; i++) {
                    GoodsBeen been = list_goods.get(i);
                    list_first.add(been);
                }
                if (adapter != null) {
                    adapter.RefrashAdapter(list_first);
                } else {
                    //adapter = new GoodsAdapter(SeekActivity.this, list_first);
                    mLv_seek.setAdapter(adapter);
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
        max = list_goods.size();
        int count = adapter.getCount();
        if ((count + 5) < max) {
            for (int i = count; i < count + 5; i++) {
                GoodsBeen been = list_goods.get(i);
                list_goods.add(been);
            }
        } else {
            for (int i = count; i < max; i++) {
                GoodsBeen been = list_goods.get(i);
                list_first.add(been);
            }
        }
    }

    private void post_file(String listStr, Map<String, Object> map) {
        OkHttpClient ok = new OkHttpClient();
        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder();
        requestBody.setType(MultipartBody.FORM);
        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
                if (entry.getValue() != null && !"".equals(entry.getValue())) {
                    requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
                }
            }
        }
        MultipartBody build = requestBody.build();
        Request request = new Request.Builder()
                .url(listStr)
                .post(build)
                .build();
        Call call = ok.newCall(request);

        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call arg0, Response arg1) throws IOException {
                final String str = arg1.body().string();
                List<GoodsBeen> list = new ArrayList<GoodsBeen>();
                try {
                    JSONObject json = new JSONObject(str);
                    JSONObject data = json.getJSONObject("data");
                    JSONArray array = data.getJSONArray("list");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jo = array.getJSONObject(i);
                        GoodsBeen been = new GoodsBeen();
                        int id = jo.getInt("id");
                        String title = jo.getString("title");
                        String image = "http://192.168.4.188/Goods/uploads/" + jo.getString("image");
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
                        Message msg = hand.obtainMessage();
                        msg.what = 2;
                        msg.obj = list;
                        hand.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(SeekActivity.this, DetailsActivity.class);
        GoodsBeen been = (GoodsBeen) adapterView.getAdapter().getItem(i);
        intent.putExtra("id", been.getId());
        startActivity(intent);
        overridePendingTransition(R.anim.in_frombottom, R.anim.out_from);
    }
}
