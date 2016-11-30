package us.mifeng.utils;


import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by admin on 2016/10/23.
 */

public class OkHttpUtils {


    //创建接口回调1
    public interface GetEntityCallBack {
        void getEntity(Object obj);
    }

    //2
    public static GetEntityCallBack getEntityCallBack;

    //3
    public static void setGetEntityCallBack(GetEntityCallBack getEntityCallBack1) {
        getEntityCallBack = getEntityCallBack1;
    }


    /*
    * post网络请求
    */
    public static void post(final Context context,String path, Map<String, String> map,  final Class cla) {
        OkHttpClient client = new OkHttpClient();

        final FormBody.Builder builder = new FormBody.Builder();
        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        for (Map.Entry<String, String> entry : entrySet
                ) {
            builder.add(entry.getKey(), entry.getValue());
        }
        FormBody body = builder.build();

        Request request = new Request.Builder()
                .url(path)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(context, "网络请求失败啦", Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    //Gson解析
                    //先获取联网请求的数据
                    String dataJson = response.body().string();
                    Log.i(TAG, "onResponse: " + dataJson);
                    Gson gson = new Gson();
                    Object object = gson.fromJson(dataJson, cla);
                    //4
                    getEntityCallBack.getEntity(object);
                }
            }
        });
    }

    /*
    * get网络请求
    * */
    public static void get(final Context context, String path, final Class type) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(path)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(context, "网络请求失败啦", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String dataString = response.body().string();
                    Gson gson = new Gson();
                    Object json = gson.fromJson(dataString, type);
                    //4
                    getEntityCallBack.getEntity(json);
                }
            }
        });
    }
}
