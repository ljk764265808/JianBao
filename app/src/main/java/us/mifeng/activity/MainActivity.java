package us.mifeng.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;
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

    private ViewPager vp;
    private RadioGroup rg;
    private RadioButton rb_goods,rb_release,rb_mine;
    private ArrayList<Fragment> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //确保底部的RadioGroup会被输入法盖住
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        initView();
        ininData();
        initFirst();
    }

    private void initFirst() {
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                switch (arg1) {
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

    private void ininData() {
        list=new ArrayList<Fragment>();
        list.add(new Fragment_Goods());
        list.add(new Fragment_Release());
        list.add(new Fragment_Mine());
        Framework_Adapter mAdapter = new Framework_Adapter(getSupportFragmentManager());
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
