package us.mifeng.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import us.mifeng.activity.R;

public class GoodsAdapter extends BaseAdapter {
    private Context ctx;
    private List<String> list;

    public GoodsAdapter(Context ctx, List<String> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(ctx, R.layout.item, null);
            holder.mItem_img = (ImageView) convertView.findViewById(R.id.mItem_img);
            holder.mItem_title = (TextView) convertView.findViewById(R.id.mItem_title);
            holder.mItem_price = (TextView) convertView.findViewById(R.id.mItem_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mItem_img.setImageResource(R.mipmap.shouji);
        //holder.mItem_title.setText(list.get(position));
       //holder.mItem_price.setText(list.get(position));
        return convertView;
    }

    private class ViewHolder {
        private ImageView mItem_img;
        private TextView mItem_title, mItem_price;
    }
}
