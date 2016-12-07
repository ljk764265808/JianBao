package us.mifeng.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import us.mifeng.activity.AlbumActivity;
import us.mifeng.activity.GalleryActivity;
import us.mifeng.activity.R;
import us.mifeng.utils.Bimp;
import us.mifeng.utils.FileUtils;
import us.mifeng.utils.ImageItem;
import us.mifeng.utils.PublicWay;
import us.mifeng.utils.Res;

import static android.app.Activity.RESULT_OK;

/**
 * Created by k on 2016/12/3.
 */

public class Fragment_Release extends Fragment {
    private GridView noScrollgridview;
    private GridAdapter adapter;
    private View parentView;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    public static Bitmap bimap ;
    private EditText et_title,et_miaoshu,et_price,et_phone,et_qq,et_weixin;
    private Button bt_fabu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Res.init(getActivity());
        bimap = BitmapFactory.decodeResource(
                getResources(),
                R.mipmap.icon_addpic_unfocused);
        PublicWay.activityList.add(getActivity());
        parentView = LayoutInflater.from(getActivity()).inflate(R.layout.fabu_main, null);
        Init(parentView);
        return parentView;
    }



    public void Init(View view) {
        noScrollgridview = (GridView)view.findViewById(R.id.noScrollgridview);
        et_title = (EditText) view.findViewById(R.id.et_title);
        et_miaoshu = (EditText) view.findViewById(R.id.et_miaoshu);
        et_price = (EditText) view.findViewById(R.id.et_price);
        et_phone = (EditText) view.findViewById(R.id.et_phone);
        et_qq = (EditText) view.findViewById(R.id.et_qq);
        et_weixin = (EditText) view.findViewById(R.id.et_weixin);
        bt_fabu = (Button) view.findViewById(R.id.bt_fabu);
        bt_fabu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trim1 = et_title.getText().toString().trim();
                String trim2 = et_miaoshu.getText().toString().trim();
                String trim3 = et_price.getText().toString().trim();
                String trim4 = et_phone.getText().toString().trim();
                String trim5 = et_qq.getText().toString().trim();
                String trim6 = et_weixin.getText().toString().trim();
                if(TextUtils.isEmpty(trim1)){
                    Toast.makeText(getActivity(),"宝贝名称不能为空",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(trim2)){
                    Toast.makeText(getActivity(),"宝贝描述不能为空",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(trim3)){
                    Toast.makeText(getActivity(),"宝贝价格不能为空",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(trim4)){
                    Toast.makeText(getActivity(),"手机号不能为空",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(trim5)){
                    Toast.makeText(getActivity(),"qq号不能为空",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(trim6)){
                    Toast.makeText(getActivity(),"微信不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),"发布成功",Toast.LENGTH_SHORT).show();
                }
            }
        });

        pop = new PopupWindow(getActivity());
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.item_popupwindows, null);
        ll_popup = (LinearLayout) view1.findViewById(R.id.ll_popup);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view1);

        RelativeLayout parent = (RelativeLayout) view1.findViewById(R.id.parent);
        Button bt1 = (Button) view1
                .findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view1
                .findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view1
                .findViewById(R.id.item_popupwindows_cancel);
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                photo();
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        AlbumActivity.class);
                startActivity(intent);
//                (R.anim.activity_translate_in, R.anim.activity_translate_out);
                pop.dismiss();
                ll_popup.clearAnimation();
            }


        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });


        adapter = new GridAdapter(getActivity());
        adapter.update();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp.tempSelectBitmap.size()) {
                    Log.i("ddddddd", "----------");
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.activity_translate_in));
                    pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                } else {
                    Intent intent = new Intent(getActivity(),
                            GalleryActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            }
        });

    }



    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            if(Bimp.tempSelectBitmap.size() == 9){
                return 9;
            }
            return (Bimp.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_grida,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position ==Bimp.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.mipmap.icon_addpic_unfocused));
                if (position == 9) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };
//测试提交
        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
    }

    public String getString(String s) {
        String path = null;
        if (s == null)
            return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }

    protected void onRestart() {
        adapter.update();
    }

    private static final int TAKE_PICTURE = 0x000001;

    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {

                    String fileName = String.valueOf(System.currentTimeMillis());
                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    FileUtils.saveBitmap(bm, fileName);

                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(bm);
                    Bimp.tempSelectBitmap.add(takePhoto);
                }
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            for(int i=0;i<PublicWay.activityList.size();i++){
                if (null != PublicWay.activityList.get(i)) {
                    PublicWay.activityList.get(i).finish();
                }
            }
            System.exit(0);
        }
        return true;
    }
}
