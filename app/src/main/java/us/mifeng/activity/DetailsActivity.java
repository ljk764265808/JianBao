package us.mifeng.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import us.mifeng.been.DetailsBeen;
import us.mifeng.view.AdversView_datials;

import static java.lang.String.valueOf;

/**
 * Created by admin on 2016/11/29.
 */

public class DetailsActivity extends Activity implements View.OnClickListener {
    private int[] arr;
    private LinearLayout mBtn_back_details;
    private ImageView mImg_guanzhu;
    private LinearLayout mLine_qq,mLine_wechat,mLine_email;
    private TextView mTv_qq,mTv_wechat,mTv_mobile,mTv_email,mTv_follow,mTv_issue_time;
    private TextView mTv_guanzhu,mTv_contact,mTv_description,mTv_title_details,mTv_price_details;
    private TextView mTv_state,mTv_owner,mTv_title;
    private Map<String,Object> map=new HashMap<String,Object>();
    private int id;
    private LinearLayout mLine_guanzhu;
    private AdversView_datials Aview_details;
    private boolean GuanzhuFlag=true;
    private LinearLayout mLinear;
    private List<String> list_url = new ArrayList<>();

    private String urlStr="http://192.168.4.188/Goods/app/item_list/detail.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        id=getIntent().getIntExtra("id",0);
        initView();
        initMap();
        post_file(urlStr,map);
        }

    private void initMap() {
        map.put("id",id);
        map.put("token","3E738515C6BD4D1CA0AB32C01AC8C713");

    }

    private void initView() {
        mBtn_back_details= (LinearLayout) findViewById(R.id.mBtn_back_details);
        mImg_guanzhu= (ImageView) findViewById(R.id.mImg_guanzhu);
        mTv_guanzhu= (TextView) findViewById(R.id.mTv_guanzhu);
        mLine_guanzhu= (LinearLayout) findViewById(R.id.mLine_guanzhu);
        mLinear= (LinearLayout) findViewById(R.id.mAdver_details);
        mLine_wechat= (LinearLayout) findViewById(R.id.mLine_wechat);
        mLine_qq= (LinearLayout) findViewById(R.id.mLine_qq);
        mLine_email= (LinearLayout) findViewById(R.id.mLine_email);
        mTv_contact= (TextView) findViewById(R.id.mTv_contact);
        mTv_mobile= (TextView) findViewById(R.id.mTv_mobile);
        mTv_wechat= (TextView) findViewById(R.id.mTv_wechat);
        mTv_qq= (TextView) findViewById(R.id.mTv_qq);
        mTv_email= (TextView) findViewById(R.id.mTv_email);
        mTv_issue_time= (TextView) findViewById(R.id.mTv_issue_time);
        mTv_title_details= (TextView) findViewById(R.id.mTv_title_details);
        mTv_price_details= (TextView) findViewById(R.id.mTv_price_details);
        mTv_follow= (TextView) findViewById(R.id.mTv_follow);
        mTv_owner= (TextView) findViewById(R.id.mTv_follow);
        mTv_state= (TextView) findViewById(R.id.mTv_state);
        mTv_description= (TextView) findViewById(R.id.mTv_description);
        mTv_title= (TextView) findViewById(R.id.mTv_title);

        mBtn_back_details.setOnClickListener(this);
        mLine_guanzhu.setOnClickListener(this);
        mTv_mobile.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int ID=view.getId();
        switch (ID){
            case R.id.mBtn_back_details:
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
            if(msg.what==0){

                DetailsBeen detaBeen= (DetailsBeen) msg.obj;

                mTv_title.setText(detaBeen.getTitle());
                mTv_title_details.setText(detaBeen.getTitle());
                mTv_description.setText(detaBeen.getDescription());
                mTv_price_details.setText(""+detaBeen.getPrice());
                mTv_contact.setText(detaBeen.getContact());
                mTv_mobile.setText(detaBeen.getMobile());
                //发布时间
                mTv_issue_time.setText(""+detaBeen.getIssue_time());
                mTv_follow.setText(""+detaBeen.getFollow());

                if(Integer.valueOf(detaBeen.getState())==0){
                    mTv_state.setText("正常");
                }else if(Integer.valueOf(detaBeen.getState())==1){
                    mTv_state.setText("出售");
                }else if(Integer.valueOf(detaBeen.getState())==2){
                    mTv_state.setText("下架");
                }
                if(Integer.valueOf(detaBeen.getOwner())==0){
                    mTv_owner.setText("否");
                }else if(Integer.valueOf(detaBeen.getOwner())==1){
                    mTv_owner.setText("是");
                }
                if(TextUtils.isEmpty(detaBeen.getQq())){
                    mTv_qq.setText("");
                    mLine_qq.setVisibility(View.GONE);
                }else{
                    mTv_qq.setText(detaBeen.getQq());
                }
                if(TextUtils.isEmpty(detaBeen.getWechat())){
                    mTv_wechat.setText("");
                    mLine_wechat.setVisibility(View.GONE);
                }else{
                    mTv_wechat.setText(detaBeen.getWechat());
                }
                if(TextUtils.isEmpty(detaBeen.getEmail())){
                    mTv_email.setText("");
                    mLine_email.setVisibility(View.GONE);
                }else{
                    mTv_email.setText(detaBeen.getEmail());
                }
                List<String> list=detaBeen.getList();
                if (list.size()==0){
                    ImageView image=new ImageView(DetailsActivity.this);
                    image.setImageResource(R.mipmap.detail);
                    mLinear.addView(image);
                }else{
                    Log.e("","list"+list.size());
                    Aview_details=new AdversView_datials(DetailsActivity.this,list);
                    mLinear.addView(Aview_details.getView());
                }


            }
        }
    };
    protected void post_file(String urlStr,Map<String,Object> map){
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
                .url(urlStr)
                .post(build)
                .build();
        Call call=ok.newCall(request);

        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call arg0, Response arg1) throws IOException {
             final String str=arg1.body().string();

                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            DetailsBeen detail = new DetailsBeen();
                            JSONObject json = new JSONObject(str);
                            JSONObject data = json.getJSONObject("data");
                            int id = data.getInt("id");
                            detail.setId(id);
                            int user_id = data.getInt("user_id");
                            detail.setUser_id(user_id);
                            String title = data.getString("title");
                            detail.setTitle(title);
                            String description = data.getString("description");
                            detail.setDescription(description);
                            String price = data.getString("price");
                            detail.setPrice(price);
                            String contact = data.getString("contact");
                            detail.setContact(contact);
                            int issue_time = data.getInt("issue_time");

                            detail.setIssue_time(issue_time);
                            int follow = data.getInt("follow");
                            detail.setFollow(follow);
                            int state = data.getInt("state");
                            detail.setState(state);
                            int owner = data.getInt("owner");
                            detail.setOwner(owner);
                            String mobile = data.getString("mobile");
                            detail.setMobile(mobile);
                            if (data.has("qq")) {
                                String qq = data.getString("qq");
                                detail.setQq(qq);
                            }
                            if (data.has("wechat")) {
                                String wechat = data.getString("wechat");
                                detail.setWechat(wechat);
                            }
                            if (data.has("email")) {
                                String email = data.getString("email");
                                detail.setEmail(email);
                            }
                            if (data.has("photos")) {
                                JSONArray photos = data.getJSONArray("photos");
                                for (int i = 0; i < photos.length(); i++) {
                                    String string = "http://192.168.4.188/Goods/uploads/" + photos.getString(i);
                                    list_url.add(string);
                                }
                                detail.setList(list_url);
                            } else {
                                detail.setList(null);
                            }
                            Message msg=hand.obtainMessage();
                            msg.what=0;
                            msg.obj=detail;
                            hand.sendMessage(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

            @Override
            public void onFailure(Call arg0, IOException arg1) {
                // TODO Auto-generated method stub

            }
        });
    }

}
