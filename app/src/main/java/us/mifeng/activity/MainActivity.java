package us.mifeng.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

import us.mifeng.adapter.Framework_Adapter;
import us.mifeng.fragment.Fragment_Goods;
import us.mifeng.fragment.Fragment_Mine;
import us.mifeng.fragment.Fragment_Release;

/**
 * Created by k on 2016/11/24.
 */
public class MainActivity extends FragmentActivity {
    private ArrayList<Fragment> list;
    private RadioGroup rg;
    private RadioButton rb_goods,rb_release,rb_mine;
    private ViewPager vp;
    private Framework_Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initList();
        initFirst();
    }

    private void initFirst() {
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_goods:
                        rb_goods.setChecked(true);
                        vp.setCurrentItem(0);
                        break;
                    case R.id.rb_release:
                        rb_release.setChecked(true);
                        vp.setCurrentItem(1);
                        break;
                    case R.id.rb_mine:
                        rb_mine.setChecked(true);
                        vp.setCurrentItem(2);
                        break;
                }
            }
        });
    }

    private void initList() {
        list=new ArrayList<Fragment>();
        list.add(new Fragment_Goods());
        list.add(new Fragment_Release());
        list.add(new Fragment_Mine());
        mAdapter = new Framework_Adapter(getSupportFragmentManager());
        mAdapter.setList(list);
        vp.setAdapter(mAdapter);
    }

    private void initView() {
        vp = (ViewPager) findViewById(R.id.vp);
        rg = (RadioGroup) findViewById(R.id.rg);
        rb_goods = (RadioButton) findViewById(R.id.rb_goods);
        rb_release = (RadioButton) findViewById(R.id.rb_release);
        rb_mine = (RadioButton) findViewById(R.id.rb_mine);
    }
}
