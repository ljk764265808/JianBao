package us.mifeng.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by xy on 2016/11/30.
 */

public class MyFaBuActivity extends Activity {

    private ListView listView;
    private ArrayList<String>list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myfabu_main);
            //接口写好后就可以用了暂时用的假数据。。。。。直接上布局
        //listView = (ListView) findViewById(R.id.fabu_listview);
       // FaBuAdapter adapter=new FaBuAdapter(list,this);
        //listView.setAdapter(adapter);
    }
}
