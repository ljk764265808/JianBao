package us.mifeng.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import us.mifeng.activity.R;

/**
 * Created by k on 2016/11/24.
 */

public class Fragment_Release extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fabu_main, null);
        return v;
    }
}
