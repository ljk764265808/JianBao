package us.mifeng.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by k on 2016/12/6.
 */

public class OkHttpUtils {
    //1.
    public interface GetEntityCallBack{
        void getEntity(Object obj);
    }
    //2.
    public static GetEntityCallBack callBack;
    //3.
    public static  void setGetEntityCallBack(GetEntityCallBack callBack1){
        callBack=callBack1;
    }
    public static Object o;
    private static final String TAG = "OkHttpUtils";

    public static Object post(final Context context, String path, Map<String,String> map, final Type type){
        OkHttpClient client=new OkHttpClient();

        FormBody.Builder builder = new FormBody.Builder();
        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry:entries
                ) {
            builder.add(entry.getKey(),entry.getValue());
        }
        FormBody body = builder.build();

        final Request request= new Request.Builder()
                .url(path)
                .post(body)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {




            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(context, "I am so sorry", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful())
                {
                    String json = response.body().string();
                    Log.i(TAG, "onResponse: "+json);
                    Gson gson = new Gson();
                    o = gson.fromJson(json, type);
                    //4.
                    callBack.getEntity(o);
                }
            }
        });
        return  o;
    }
    public static Object post(final Context context, String path, Map<String,String> map, final Class cla){
        OkHttpClient client=new OkHttpClient();

        FormBody.Builder builder = new FormBody.Builder();
        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry:entries) {
            builder.add(entry.getKey(),entry.getValue());
        }
        FormBody body = builder.build();

        final Request request= new Request.Builder()
                .url(path)
                .post(body)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(context, "I am so sorry", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful())
                {
                    String json = response.body().string();
                    Log.i(TAG, "onResponse: "+json);
                    Gson gson = new Gson();
                    o = gson.fromJson(json,cla);
                    //4.
                    callBack.getEntity(o);
                }
            }
        });
        return  o;
    }

}
