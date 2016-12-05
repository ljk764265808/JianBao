package us.mifeng.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import us.mifeng.app.MInterface;
import us.mifeng.base.Register;
import us.mifeng.utils.MOkHttp;
import us.mifeng.utils.ShareUtils;
import us.mifeng.utils.ShowUtils;
import us.mifeng.utils.UploadHelper;

/**
 * Created by admin on 2016/12/5.
 */
/*
* 上传身份证照片（拍照和本地相册选择）
* */
public class Register_PhotoActivity extends Activity {
    private static final String TAG ="Register_PhotoActivity" ;
    @InjectView(R.id.ibtn_camera)
    ImageButton ibtnCamera;
    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int TAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;
    @InjectView(R.id.iv_photo)
    ImageView ivPhoto;
    @InjectView(R.id.btn_register)
    Button btnRegister;
    private Uri uri;
    private File imageFile;
    private String choosepath;
    private String path = MInterface.zhuji + MInterface.zhuce;
    private String spath;
    private String imagepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_photo);
        ButterKnife.inject(this);
    }

    private void initRequest() {
        String path = (String) ShareUtils.getData(Register_PhotoActivity.this, "path", "");
        Bundle bundle = getIntent().getExtras();
        HashMap<String,String> map = (HashMap<String, String>) bundle.getSerializable("map");

        UploadHelper.setGetEntityCallBack(new MOkHttp.GetEntityCallBack() {
            @Override
            public void getEntity(Object obj) {
                Register register = (Register) obj;
                if (register!= null){
                    String status = register.getStatus();
                    if (status.equals("200")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ShowUtils.show(Register_PhotoActivity.this,"注册成功");
                                startActivity(new Intent(Register_PhotoActivity.this,LoginActivity.class          ));
                            }
                        });
                    }else if (status.equals("206")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ShowUtils.show(Register_PhotoActivity.this,"邀请码无效");
                            }
                        });
                    }else if(status.equals("204")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ShowUtils.show(Register_PhotoActivity.this,"请填写完整信息");
                            }
                        });

                    }
                }
            }
        });

        Log.e(TAG, "initRequest:传递的map集合 "+map);
        UploadHelper.upload(new File(path), map, this.path);
        Log.e(TAG, "initRequest: " + path + "---------------------");

    }

    @OnClick({R.id.ibtn_camera, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_camera:
                showPicturePicker();


                break;
            case R.id.btn_register:
                initRequest();
                break;
        }
    }

    /*
       * 拍照和本地上传图片
       * */
    public void showPicturePicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("图片来源");
        builder.setNegativeButton("取消", null);
        builder.setItems(new String[]{"拍照", "相册"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case TAKE_PICTURE:
                        if (initImageFile()) {
                            photo();
                        }
                        break;

                    case CHOOSE_PICTURE:
                        pictureschose();
                        break;

                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }



    /*
      * 判断sdcard是否被挂载
      */
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public void photo() {
        // 启动系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 设置拍照后保存的图片存储在文件中
        uri = Uri.fromFile(imageFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        // 启动activity并获取返回数据
        startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
    }


    private void pictureschose() {
        Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
        intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    public static byte[] readStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;

    }

    public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes != null) {
            if (opts != null) {
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
            } else {
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        ContentResolver resolver = getContentResolver();
        // 拍照后获取返回值，这里获取到的是原始图片
        if (requestCode == PHOTO_REQUEST_CAREMA
                && resultCode == Activity.RESULT_OK) {
            // 获取到了拍照后的图片文件，从文件解码出Bitmap对象
            if (imageFile.exists()) {
                // 这里直接decode了图片，没有判断图片大小，没有对可能出现的OOM做处理
                Bitmap bm = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                ShareUtils.setData(Register_PhotoActivity.this,"path",imageFile.getAbsolutePath());
                // 显示图片
                ivPhoto.setImageBitmap(bm);
            } else {
                Toast.makeText(this, "图片文件不存在", Toast.LENGTH_SHORT).show();
            }


        } else if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                try {
                    // 获得图片的uri
                    Uri originalUri = data.getData();
                    choosepath = originalUri.toString();
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = managedQuery(originalUri, proj, null, null, null);
                    //按我个人理解 这个是获得用户选择的图片的索引值
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    //将光标移至开头 ，这个很重要，不小心很容易引起越界
                    cursor.moveToFirst();
                    //最后根据索引值获取图片路径
                    imagepath = cursor.getString(column_index);
                    ShareUtils.setData(Register_PhotoActivity.this,"path",imagepath);

                    Log.i(TAG, imagepath + "+++++++++++++++++++++++++++++++++");//file:///storage/emulated/0/1480898730576.jpg
                    // 将图片内容解析成字节数组
                    byte[] mContent = readStream(resolver.openInputStream(Uri
                            .parse(choosepath)));
                    // 将字节数组转换为ImageView可调用的Bitmap对象
                    Bitmap myBitmap = getPicFromBytes(mContent, null);
                    // //把得到的图片绑定在控件上显示
                    ivPhoto.setImageBitmap(myBitmap);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }


            }
        }
        super.onActivityResult(requestCode, resultCode, data);


    }
    private boolean initImageFile() {
        // 有SD卡时才初始化文件
        if (hasSdcard()) {
//            // 构造存储图片的文件的路径，文件名为当前时间
            String filePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath()
                    + "/"
                    + System.currentTimeMillis()
                    + ".jpg";

            imageFile = new File(filePath);
            Log.e(TAG, "initImageFile: " + filePath + "_______________");
            if (!imageFile.exists()) {// 如果文件不存在，就创建文件
                try {
                    imageFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return true;
        }
        return false;
    }


}
