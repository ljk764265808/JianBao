package us.mifeng.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import us.mifeng.activity.R;
import us.mifeng.utils.MQieYuan;

/**
 * Created by k on 2016/11/24.
 */

public class Fragment_Mine extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.mine_layout,null);
        Bitmap bit= BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        ImageView imageview = (ImageView) v.findViewById(R.id.image);
        imageview.setImageBitmap(MQieYuan.getYuan(bit));

        return v;
    }
}
