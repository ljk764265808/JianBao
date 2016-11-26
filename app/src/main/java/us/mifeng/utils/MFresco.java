package us.mifeng.utils;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;


/**
 * Created by k on 2016/11/24.
 */

public class MFresco extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        //基本使用的初始化方法
        Fresco.initialize(this);
        //渐进式JPEG图的初始化方法
        ImagePipelineConfig config=ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this,config);
    }
}
