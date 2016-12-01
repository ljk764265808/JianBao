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
                //list=getNews(jsonStr);
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
}
