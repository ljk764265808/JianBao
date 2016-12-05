package us.mifeng.utils;

import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import us.mifeng.base.Register;

/**
 * Created by admin on 2016/12/1.
 */
/*
* Okhttp同时上传图片和数据
* */
public class UploadHelper {
    public static final String TAG = "UploadHelper";
    private static final OkHttpClient client = new OkHttpClient();

    public interface GetEntityCallBack {
        void getEntity(Object obj);
    }

    public static MOkHttp.GetEntityCallBack callBack;

    public static void setGetEntityCallBack(MOkHttp.GetEntityCallBack callBack1) {
        callBack = callBack1;
    }

    public static void upload(File file, HashMap<String, String> map, String path) {
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        MultipartBody body = builder.addFormDataPart("card", file.getName(), fileBody).build();
        Request request = new Request.Builder()
                .url(path)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: 失败了~~~~~~", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                Log.e(TAG, "onResponse: " + code);
                if (response.isSuccessful()) {
                    String string = response.body().string();
                    Log.e(TAG, "onResponse: " + string);
                    Gson gson = new Gson();
                    Object object = gson.fromJson(string, Register.class);
                    callBack.getEntity(object);
                }
            }
        });

    }
}
