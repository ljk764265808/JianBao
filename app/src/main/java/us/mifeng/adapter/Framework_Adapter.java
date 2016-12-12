package us.mifeng.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by k on 2016/11/24.
 */

public class Framework_Adapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> list = new ArrayList<Fragment>();

    public Framework_Adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public void setList(ArrayList<Fragment> listdemo) {
        this.list = listdemo;
    }
}
