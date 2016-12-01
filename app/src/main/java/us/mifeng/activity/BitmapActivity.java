package us.mifeng.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import us.mifeng.view.ScaleImg;

/**
 * Created by admin on 2016/11/29.
 */

public class BitmapActivity extends Activity {
    private ScaleImg mImg_bitmap;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap);
        initView();
    }

    private void initView() {
        mImg_bitmap= (ScaleImg) findViewById(R.id.mImg_bitmap);
        int i=getIntent().getIntExtra("i",0);
        bitmap= BitmapFactory.decodeResource(getResources(),i);
        mImg_bitmap.setBitmap(bitmap);
    }
}
