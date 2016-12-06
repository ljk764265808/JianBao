package us.mifeng.utils;

/**
 * Created by admin on 2016/11/29.
 */

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import us.mifeng.been.AdverBeen;
import us.mifeng.been.GoodsBeen;

/**
 * 处理整个项目中所有的json解析
 */

public class JsonUtils {
    private Context ctx;
    private String jsonStr;
    private int index;

    public JsonUtils(Context ctx, String jsonStr, int index) {
        this.ctx = ctx;
        this.jsonStr = jsonStr;
        this.index = index;

    }

    /*
     * 带返回值的获取list集合的方法
	 * */
    public List BuildList(String jsonStr) {
        List list = null;
        if (index == 1) {
            if (jsonStr != null) {
                list = getAdvers(jsonStr);
            }
        } else if (index == 2) {
            if (jsonStr != null) {
                list = getGoods(jsonStr);
            }
        }
        return list;
    }



    private List getAdvers(String jsonStr) {
        List<AdverBeen> list = new ArrayList<AdverBeen>();
        try {
            JSONObject json = new JSONObject(jsonStr);
            JSONObject json1 = json.getJSONObject("result");
            JSONArray array = json1.getJSONArray("data");
            for (int i = 6; i < 11; i++) {
                JSONObject jo = array.getJSONObject(i);
                AdverBeen ad = new AdverBeen();
                JSONArray arry2 = jo.getJSONArray("albums");
                String imgUrl = arry2.getString(0);
                ad.setImgpath(imgUrl);
                list.add(ad);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    private List getGoods(String jsonStr) {
        List<GoodsBeen> list = new ArrayList<GoodsBeen>();
        try {
            JSONObject json = new JSONObject(jsonStr);
            JSONObject data = json.getJSONObject("data");
            JSONArray array = data.getJSONArray("list");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jo = array.getJSONObject(i);
                GoodsBeen been = new GoodsBeen();
                int id = jo.getInt("id");
                String title = jo.getString("title");
                String image = jo.getString("image");
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

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void getDetails(String jsonStr) {


    }
}
