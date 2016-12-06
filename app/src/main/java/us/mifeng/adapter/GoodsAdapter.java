package us.mifeng.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import us.mifeng.activity.R;
import us.mifeng.been.GoodsBeen;

public class GoodsAdapter extends BaseAdapter {
    private Context ctx;
    private List<GoodsBeen> list;

    public GoodsAdapter(Context ctx, List<GoodsBeen> list) {
        this.ctx = ctx;
        this.list = list;
    }
    public void RefrashAdapter(List<GoodsBeen> list){
        this.list=list;
        notifyDataSetChanged();
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
            convertView = View.inflate(ctx, R.layout.item_list, null);
            holder.mItem_img = (ImageView) convertView.findViewById(R.id.mItem_img);
            holder.mItem_title = (TextView) convertView.findViewById(R.id.mItem_title);
            holder.mItem_price = (TextView) convertView.findViewById(R.id.mItem_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mItem_title.setText(list.get(position).getTitle());
       holder.mItem_price.setText(list.get(position).getPrice());
        Glide.with(ctx).load(list.get(position).getImage()).into(holder.mItem_img);
        return convertView;
    }

    private class ViewHolder {
        private ImageView mItem_img;
        private TextView mItem_title, mItem_price;
    }
}
