package us.mifeng.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.commit451.nativestackblur.NativeStackBlur;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.internal.Supplier;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.CountingMemoryCache;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.cache.ImageCacheStatsTracker;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.UUID;

/**
 * Created by k on 2016/11/24.
 */

public class FrescoUtils {
    private static final String PHOTO_FRESCO = "frescocache";

    private Bitmap fastBlur(Bitmap bkg, int radius, int downSampling) {
        if (downSampling < 2){
            downSampling = 2;
        }

        Bitmap smallBitmap =   Bitmap.createScaledBitmap(bkg,bkg.getWidth()/downSampling,bkg.getHeight()/downSampling,true);

        return   NativeStackBlur.process(smallBitmap, radius);
    }

    public static void loadUrl(String url, SimpleDraweeView draweeView, BasePostprocessor processor, int width, int height,
                               BaseControllerListener listener){

        load(Uri.parse(url),draweeView,processor,width,height,listener);
    }

    public static void loadFile(String file, SimpleDraweeView draweeView,BasePostprocessor processor,int width,int height,
                                BaseControllerListener listener){

        load(getFileUri(file),draweeView,processor,width,height,listener);

    }

    public static void loadFile(File file, SimpleDraweeView draweeView, BasePostprocessor processor, int width, int height,
                                BaseControllerListener listener){

        load(getFileUri(file),draweeView,processor,width,height,listener);

    }

    public static void loadRes(int resId, SimpleDraweeView draweeView,BasePostprocessor processor,int width,int height,
                               BaseControllerListener listener){

        load(getResUri(resId),draweeView,processor,width,height,listener);

    }

    public static void load(Uri uri,SimpleDraweeView draweeView,BasePostprocessor processor,int width,int height,
                            BaseControllerListener listener){
        ResizeOptions resizeOptions = null;
        if (width >0 && height > 0){
            resizeOptions = new ResizeOptions(width,height);
        }
        ImageRequest request =
                ImageRequestBuilder.newBuilderWithSource(uri)
                        .setPostprocessor(processor)
                        .setResizeOptions(resizeOptions)
                        //缩放,在解码前修改内存中的图片大小, 配合Downsampling可以处理所有图片,否则只能处理jpg,
                        // 开启Downsampling:在初始化时设置.setDownsampleEnabled(true)
                        .setProgressiveRenderingEnabled(true)//支持图片渐进式加载
                        .setAutoRotateEnabled(true) //如果图片是侧着,可以自动旋转
                        .build();

        PipelineDraweeController controller =
                (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setControllerListener(listener)
                        .setOldController(draweeView.getController())
                        .setAutoPlayAnimations(true) //自动播放gif动画
                        .build();



        draweeView.setController(controller);
    }

    public static Uri getFileUri(File file){
        return Uri.fromFile(file);
    }

    public static Uri getFileUri(String filePath){
        return Uri.fromFile(new File(filePath));
    }

    public static Uri getResUri(int resId){
        return Uri.parse("res://xxyy/" + resId);
    }

    public static void setCircle( SimpleDraweeView draweeView,int bgColor){
        RoundingParams roundingParams = RoundingParams.asCircle();//这个方法在某些情况下无法成圆,比如gif
        roundingParams.setOverlayColor(bgColor);//加一层遮罩
        draweeView.getHierarchy().setRoundingParams(roundingParams);
    }

    /**
     * 暂停网络请求
     * 在listview快速滑动时使用
     */
    public static void pause(){
        Fresco.getImagePipeline().pause();
    }


    /**
     * 恢复网络请求
     * 当滑动停止时使用
     */
    public static void resume(){
        Fresco.getImagePipeline().resume();
    }

    public static void init(final Context context, int cacheSizeInM){


        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context)
                .setMaxCacheSize(cacheSizeInM*1024*1024)
                .setBaseDirectoryName(PHOTO_FRESCO)
                .setBaseDirectoryPathSupplier(new Supplier<File>() {
                    @Override
                    public File get() {
                        return context.getCacheDir();
                    }
                })
                .build();
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context)
                .setMainDiskCacheConfig(diskCacheConfig)
                .setImageCacheStatsTracker(imageCacheStatsTracker)
                .setDownsampleEnabled(true)//Downsampling，它处理图片的速度比常规的裁剪更快，
                // 并且同时支持PNG，JPG以及WEP格式的图片，非常强大,与ResizeOptions配合使用
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .build();
        Fresco.initialize(context, config);
    }

    /**
     * 清除磁盘缓存
     */
    public static void clearDiskCache(){
        Fresco.getImagePipeline().clearDiskCaches();
    }

    /**
     * 清除单张图片的磁盘缓存
     * @param url
     */
    public static void clearCacheByUrl(String url){
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        Uri uri = Uri.parse(url);
        // imagePipeline.evictFromMemoryCache(uri);
        imagePipeline.evictFromDiskCache(uri);
        //imagePipeline.evictFromCache(uri);//这个包含了从内存移除和从硬盘移除
    }

    /**
     * 从fresco的本地缓存拿到图片,注意文件的结束符并不是常见的.jpg,.png等，如果需要另存，可自行另存
     *
     * @param url
     */
    public static File getFileFromDiskCache(String url,Object obj){
        File localFile = null;
        if (!TextUtils.isEmpty(url)) {

            CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(url),obj);
            if (ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            } else if (ImagePipelineFactory.getInstance().getSmallImageFileCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getSmallImageFileCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            }
        }
        return localFile;
    }

    /**
     * 拷贝缓存文件,指定目标路径和文件名
     * @param url
     * @param dir
     * @param fileName
     * @return
     */
    public static boolean copyCacheFile(String url,File dir,String fileName,Object obj){
        File path = new File(dir,fileName);
        return   copyCacheFile(url,path,obj);
    }

    /**
     *拷贝到某一个文件,已指定文件名
     * @param url 图片的完整url
     * @param path 目标文件路径
     * @return
     */
    public static boolean copyCacheFile(String url,File path,Object obj){
        if (path == null ){
            return false;
        }
        File file = getFileFromDiskCache(url,obj);
        if (file == null){
            return false;
        }

        if (path.isDirectory()){
            throw  new RuntimeException(path + "is a directory,you should call copyCacheFileToDir(String url,File dir)");
        }
        boolean isSuccess =   file.renameTo(path);

        return isSuccess;
    }

    /**
     * 拷贝到某一个目录中,自动命名
     * @param url
     * @param dir
     * @return
     */
    public static File copyCacheFileToDir(String url,File dir,Object obj ){

        if (dir == null ){
            return null;
        }
        if (!dir.isDirectory()){
            throw  new RuntimeException(dir + "is not a directory,you should call copyCacheFile(String url,File path)");
        }
        if (!dir.exists()){
            dir.mkdirs();
        }
        String fileName = URLUtil.guessFileName(url,"","");//android SDK 提供的方法.
        // 注意不能直接采用file的getName拿到文件名,因为缓存文件是用cacheKey命名的
        if (TextUtils.isEmpty(fileName)){
            fileName = UUID.randomUUID().toString();
        }
        File newFile = new File(dir,fileName);

        boolean isSuccess =  copyCacheFile(url,newFile,obj);
        if (isSuccess){
            return newFile;
        }else {
            return null;
        }

    }

    /**
     *this method is return very fast, you can use it in UI thread.
     * @param url
     * @return 该url对应的图片是否已经缓存到本地
     */
    public static boolean isCached(String url,Object obj) {

        // return Fresco.getImagePipeline().isInDiskCache(Uri.parse(url));
        ImageRequest imageRequest =  ImageRequest.fromUri(url);
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(imageRequest,obj);
        return ImagePipelineFactory.getInstance()
                .getMainFileCache().hasKey(cacheKey);
    }

    /**
     * 文件下载到文件夹中：将图片缓存到本地后，将缓存的图片文件copy到另一个文件夹中
     *
     * 容易发生如下异常，progress在100处停留时间长
     * @param url
     * @param context
     * @param dir 保存图片的文件夹
     * @param listener 自己定义的回调
     */
    public static void download(final String url, Context context, final File dir, final DownloadListener listener){
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .build();

        final ImagePipeline imagePipeline = Fresco.getImagePipeline();


        DataSource<Void> dataSource = imagePipeline.prefetchToDiskCache(imageRequest, context, Priority.HIGH);
        dataSource.subscribe(new BaseDataSubscriber<Void>() {
            @Override
            protected void onNewResultImpl(DataSource<Void> dataSource) {


                File  file  =   copyCacheFileToDir(url,dir,null);
                clearCacheByUrl(url);//清除缓存
                if (file == null || !file.exists()){
                    listener.onFail();
                }else {
                    listener.onSuccess(file);
                }

            }

            @Override
            public void onProgressUpdate(DataSource<Void> dataSource) {
                super.onProgressUpdate(dataSource);
                listener.onProgress(dataSource.getProgress());
            }

            @Override
            protected void onFailureImpl(DataSource<Void> dataSource) {
                listener.onFail();
            }
        }, CallerThreadExecutor.getInstance());

    }

    public static void loadUrlWithFace(final String url, final SimpleDraweeView draweeView) {
        PointF pointF = new PointF(0.5f, 0.38f);
        draweeView
                .getHierarchy()
                .setActualImageFocusPoint(pointF);
        loadUrl(url, draweeView, null, 0, 0, null);
    }

    public static PointF getFaceFocusRatio(Bitmap bitmap){
        PointF pointF = new PointF(0.5f,0.5f);

//假设最多有5张脸
        int faceCount = 5;
        FaceDetector mFaceDetector = new FaceDetector(bitmap.getWidth(),bitmap.getHeight(),faceCount);
        FaceDetector.Face[] faces = new FaceDetector.Face[faceCount];
        //获取实际上有多少张脸
        faceCount = mFaceDetector.findFaces(bitmap, faces);
        Logger.e(bitmap.getWidth()+"--width---heitht:"+ bitmap.getHeight()+"---count:"+faceCount);
        if (faceCount == 1){
            FaceDetector.Face face = faces[0];
            PointF point = new PointF();
            face.getMidPoint(point);

            pointF.x = point.x/bitmap.getWidth();
            pointF.y = point.y/bitmap.getHeight();


        }else if (faceCount >1){
            int x = 0;
            int y = 0;
            for (int i = 0; i < faceCount; i++) {//计算多边形的中心
                FaceDetector.Face face = faces[i];
                PointF point = new PointF();
                face.getMidPoint(point);
                x += point.x;
                y += point.y;
            }
            pointF.x = x/faceCount/bitmap.getWidth();
            pointF.y = y /faceCount/bitmap.getHeight();
        }else {
            pointF = null;
        }

        if (pointF != null){
            Logger.e("x:"+ pointF.x+"---y:"+pointF.y);
        }

        return pointF;

    }

    /**
     * Created by hss01248 on 11/26/2015.
     */
    public static class MyImageCacheStatsTracker implements ImageCacheStatsTracker {
        @Override
        public void onBitmapCachePut() {

        }

        @Override
        public void onBitmapCacheHit() {

        }

        @Override
        public void onBitmapCacheMiss() {

        }

        @Override
        public void onMemoryCachePut() {

        }

        @Override
        public void onMemoryCacheHit() {

        }

        @Override
        public void onMemoryCacheMiss() {

        }

        @Override
        public void onStagingAreaHit() {

        }

        @Override
        public void onStagingAreaMiss() {

        }

        @Override
        public void onDiskCacheHit() {
            //Logger.e("ImageCacheStatsTracker---onDiskCacheHit");
        }

        @Override
        public void onDiskCacheMiss() {
            //Logger.e("ImageCacheStatsTracker---onDiskCacheMiss");
        }

        @Override
        public void onDiskCacheGetFail() {
            //Logger.e("ImageCacheStatsTracker---onDiskCacheGetFail");
        }

        @Override
        public void registerBitmapMemoryCache(CountingMemoryCache<?, ?> countingMemoryCache) {

        }

        @Override
        public void registerEncodedMemoryCache(CountingMemoryCache<?, ?> countingMemoryCache) {

        }
    }



    public interface BitmapListener{
        void onSuccess(Bitmap bitmap);
        void onFail();
    }

    public interface  DownloadListener{
        void onSuccess(File file);
        void onFail();

        void onProgress(float progress);
    }
}
