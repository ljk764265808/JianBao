package us.mifeng.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import us.mifeng.activity.MainActivity;
import us.mifeng.activity.R;
import us.mifeng.app.MInterface;
import us.mifeng.utils.OkUtils;

/**
 * Created by k on 2016/12/3.
 */

public class Fragment_Release extends Fragment {
    private EditText et_title,et_miaoshu,et_price,et_phone,et_qq,et_weixin;
    private Button bt_fabu;
    private ImageButton iv1;
    private ImageButton iv2;
    private ImageButton iv3;
    private static final int IMAGE = 1;
    ArrayList<String>lists=new ArrayList<String>();
    Map<String,String> map=new HashMap<String,String>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fabu_main, null);
        Init(view);
        return view;
    }



    public void Init(View view) {
        et_title = (EditText) view.findViewById(R.id.et_title);
        et_miaoshu = (EditText) view.findViewById(R.id.et_miaoshu);
        et_price = (EditText) view.findViewById(R.id.et_price);
        et_phone = (EditText) view.findViewById(R.id.et_phone);
        et_qq = (EditText) view.findViewById(R.id.et_qq);
        et_weixin = (EditText) view.findViewById(R.id.et_weixin);
        bt_fabu = (Button) view.findViewById(R.id.bt_fabu);
        iv1 = (ImageButton) view.findViewById(R.id.iv1);
        iv2 = (ImageButton) view.findViewById(R.id.iv2);
        iv3 = (ImageButton) view.findViewById(R.id.iv3);
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE);
            }
        });
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE);
            }
        });
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE);
            }
        });
        bt_fabu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trim1 = et_title.getText().toString().trim();
                String trim2 = et_miaoshu.getText().toString().trim();
                String trim3 = et_price.getText().toString().trim();
                String trim4 = et_phone.getText().toString().trim();
                String trim5 = et_qq.getText().toString().trim();
                String trim6 = et_weixin.getText().toString().trim();
                map.put("token","D02CCD29BCD24780AC8751FE73F8263B");
                map.put("title",trim1);
                map.put("description",trim2);
                map.put("price",trim3);
                map.put("mobile",trim4);
                OkUtils.UploadFileSCMore(MInterface.zhuji+MInterface.fabu, lists, map, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String string = response.body().string();
                        System.out.println(string);
                    }
                });
                if(TextUtils.isEmpty(trim1)){
                    Toast.makeText(getActivity(),"宝贝名称不能为空",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(trim2)){
                    Toast.makeText(getActivity(),"宝贝描述不能为空",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(trim3)){
                    Toast.makeText(getActivity(),"宝贝价格不能为空",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(trim4)){
                    Toast.makeText(getActivity(),"手机号不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),"发布成功",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), MainActivity.class  ));
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
            String imagePath = cursor.getString(columnIndex);
            lists.add(imagePath);
            showImage(imagePath);
            cursor.close();
        }
    }

    private void showImage(String imaePath){
        Bitmap bm = BitmapFactory.decodeFile(imaePath);
        iv1.setImageBitmap(bm);
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.iv1:
//
//                break;
//            case R.id.iv2:
//
//                break;
//            case R.id.iv3:
//
//                break;
//        }
//    }
}
