package com.wei.mobileoffice;

import android.os.Handler;
import android.os.Looper;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.wei.mobileoffice.platform.AppContextBase;

import java.io.File;

/**
 * Created by weiyilin on 16/5/9.
 */
public class OfficeAppContext extends AppContextBase {

    public static String TAG = OfficeAppContext.class.getSimpleName();
    //获取到主线程
    private static Thread mainThead = null;
    //获取到主线程的id
    private static int mainTheadId;
    //获取到主线程的handler
    private static Handler mainThreadHandler = null;
    //获取到主线程的looper
    private static Looper mainThreadLooper = null;
    //app是否初始化
    private boolean isAppInited = false;

    private static DisplayImageOptions options;

    @Override
    public void onCreate() {
        super.onCreate();

        initWhenAppStart();
    }

    public static OfficeAppContext getApplication() {
        return (OfficeAppContext) AppContextBase.getApplication();
    }

    /**
     * app启动时初始化，不放在Application的onCreate，因为无法控制Application的销毁
     * 由AppStart来调用
     * 与之对应的是exitApplication，结束app时会被调用
     */
    public void initWhenAppStart() {
        if (isAppInited) {
            return;
        }

        finishAllActivity();

        isAppInited = true;

        this.mainThreadHandler = new Handler();
        this.mainThreadLooper = getMainLooper();
        this.mainThead = Thread.currentThread();
        this.mainTheadId = android.os.Process.myTid();

        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "imageloader/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheFileCount(100) //缓存的文件数量
                .diskCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();//开始构建
        ImageLoader.getInstance().init(config);//全局初始化此配置

        //设置DisplayImageOptions
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_launcher) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ic_launcher)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .build();//构建完成
    }

    public static Handler getMainThreadHandler() {
        return mainThreadHandler;
    }
    public static Looper getMainThreadLooper() {
        return mainThreadLooper;
    }
    public static Thread getMainThread() {
        return mainThead;
    }
    public static int getMainThreadId() {
        return mainTheadId;
    }
    public static DisplayImageOptions getOptions() {
        return options;
    }

    /**
     * 退出app
     */
    public void exitApplication() {
        if (!isAppInited) {
            return;
        }

        isAppInited = false;

        finishAllActivity();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void finishAllActivity(){
        super.finishAllActivity();
        if(MainActivity.thisInstance!=null){
            MainActivity.thisInstance.finish();
        }
    }
}
