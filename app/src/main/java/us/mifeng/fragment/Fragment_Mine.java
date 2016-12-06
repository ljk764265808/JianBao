package us.mifeng.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import us.mifeng.activity.R;

/**
 * Created by k on 2016/11/24.
 */

public class Fragment_Mine extends Fragment {
    @InjectView(R.id.yonghuming_text)
    TextView yonghumingText;
    @InjectView(R.id.image)
    ImageView image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mine_layout, null);
        ButterKnife.inject(this, v);
        Bitmap bit = BitmapFactory.decodeResource(getResources(), R.mipmap.yonghu);
        //image.setImageBitmap(MQieYuan.getYuan(bit));
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.yonghuming_text, R.id.image})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.yonghuming_text:
                break;
            case R.id.image:
                new AlertDialog.Builder(getActivity())
                        .setTitle("                  设置头像")
                        .setView(R.layout.dialog_item)
                        .show();
              break;
        }
    }
}
