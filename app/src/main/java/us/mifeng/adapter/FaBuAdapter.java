package us.mifeng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import us.mifeng.activity.R;

/**
 * Created by xy on 2016/11/30.
 */

public class FaBuAdapter extends BaseAdapter {

    private ArrayList<String> list=new ArrayList<>();
    private Context context;

    public FaBuAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if (view==null){
            view= LayoutInflater.from(context).inflate(R.layout.fabu_item,null);
            vh=new ViewHolder();
            vh.photo= (ImageView) view.findViewById(R.id.photo);
            vh.issue_time= (TextView) view.findViewById(R.id.issue_time);
            vh.price= (TextView) view.findViewById(R.id.price);
            vh.state= (TextView) view.findViewById(R.id.state);
            vh.title= (TextView) view.findViewById(R.id.title);
            view.setTag(vh);
        }
        vh= (ViewHolder) view.getTag();


        return view;
    }



     class ViewHolder{
        ImageView photo;
         TextView title,price,issue_time,state;


     }
}
