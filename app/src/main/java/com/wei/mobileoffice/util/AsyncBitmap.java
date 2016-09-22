package com.wei.mobileoffice.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.wei.mobileoffice.constants.AppConstants.FileConstants;
import com.wei.mobileoffice.platform.config.AppConfigFactory;
import com.wei.mobileoffice.platform.http.AndroidHttpClient;
import com.wei.mobileoffice.platform.util.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AsyncBitmap {
	  
	//global
    private static LruCache<String, Bitmap> cache;
    private static ExecutorService pool;
    private static Map<ImageView, String> imageViews;
    
    private Bitmap defaultBmp;
    private ExecuteBitmap executeBitmap;
    private ExecuteCustom executeCustom;
    private String cacheDir = FileConstants.IMG_SUB_DIR;
    
    private int fileCacheQuality = 100;
    private boolean cacheByFile = true;//需要缓存到文件，默认值true，如果为false，则存取都不走File
    private boolean cacheInMemory = true;//缓存到内存，默认值true，如果为false，不在内存做缓存
    
    public void setCacheInMemory(boolean cacheInMemory) {
		this.cacheInMemory = cacheInMemory;
	}

	public void setCacheByFile(boolean cacheByFile) {
		this.cacheByFile = cacheByFile;
	}

	public void setFileCacheQuality(int fileCacheQuality) {
		this.fileCacheQuality = fileCacheQuality;
	}

	public void setExecuteBitmap(ExecuteBitmap executeBitmap) {
		this.executeBitmap = executeBitmap;
	}

	public void setExecuteCustom(ExecuteCustom executeCustom) {
		this.executeCustom = executeCustom;
	}

	static {
		//最多占用1/8
        cache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 1024/ 8));//HashMap<String, SoftReference<Bitmap>>();
        pool = Executors.newFixedThreadPool(5);  //固定线程池
        imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    }  
    
    public AsyncBitmap(){}
    
    public AsyncBitmap(Bitmap def) {
    	this.defaultBmp = def;
    }
    
    public void setCacheDir(String cacheDir){
    	this.cacheDir = cacheDir;
    }
    
    /**
     * 设置默认图片
     * @param bmp
     */
    public void setDefaultBmp(Bitmap bmp) {
    	defaultBmp = bmp;  
    }
  
    /**
     * 加载图片
     * @param url
     * @param imageView
     */
    public void loadBitmap(String url, ImageView imageView) {
    	loadBitmap(url, imageView, this.defaultBmp, 0, 0, null);
    }
    
    /**
     * 加载图片，传额外数据进来，可根据具体业务场景来控制bitmap
     */
    public void loadBitmap(String url, ImageView imageView, Map<String, Object> extData){
    	loadBitmap(url, imageView, this.defaultBmp, 0, 0, extData);
    }
	
    /**
     * 加载图片-可设置加载失败后显示的默认图片
     * @param url
     * @param imageView
     * @param defaultBmp
     */
    public void loadBitmap(String url, ImageView imageView, Bitmap defaultBmp) {
    	loadBitmap(url, imageView, defaultBmp, 0, 0, null);
    }
    
    /**
     * 加载图片-可指定显示图片的高宽
     * @param url
     * @param imageView
     * @param width
     * @param height
     */
    public void loadBitmap(String url, ImageView imageView, Bitmap defaultBmp, int width, int height, Map<String, Object> extData) {
        imageViews.put(imageView, url);  
        Bitmap bitmap = getBitmapFromCache(url);
   
        if (bitmap != null) {  
			//显示缓存图片
        	setBitmapToImageView(imageView, bitmap, extData);
        } else { 
        	//走文件设置为false，直接走网络
        	if(!cacheByFile){
        		//线程加载网络图片
        		setBitmapToImageView(imageView, defaultBmp, extData);
        		queueJob(url, imageView, width, height, extData);
        		return;
        	}
        	//加载SD卡中的图片缓存
        	String filename = FileUtils.getFileNameByUrl(url);
        	String filepath = imageView.getContext().getFilesDir() + File.separator + filename;
        	if(FileUtils.checkSaveLocationExists()){
        		File extFile = FileUtils.getAppExtFileDir(cacheDir);
        		filepath = extFile.getAbsolutePath() + File.separator + filename;
			}
    		File file = new File(filepath);
    		if(file.exists()){
				//显示SD卡中的图片缓存
    			Bitmap bmp = ImageUtils4AsyncBitmap.getBitmap(imageView.getContext(), filename, cacheDir);
    			addBitmapToMemoryCache(url, bmp);//将文件缓存到内存
        		setBitmapToImageView(imageView, bmp, extData);
        	}else{
				//线程加载网络图片
        		setBitmapToImageView(imageView, defaultBmp, extData);
        		queueJob(url, imageView, width, height, extData);
        	}
        }  
    }  
  
    /**
     * 从缓存中获取图片
     * @param url
     */
    public Bitmap getBitmapFromCache(String url) {
        Bitmap retBitmap = null;
        Bitmap bitmap = getBitmapFromMemCache(url);
        if (bitmap != null) {
            Logger.v("LRUCache", "========get image from cache========");
            retBitmap = bitmap;
        }
        return retBitmap;
    }
    
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
    	if(!cacheInMemory){
    		return;
    	}
        synchronized (cache) {
            if (getBitmapFromMemCache(key) == null) {
                Logger.v("LRUCache", "========save image to cache========");
                if(key != null && bitmap != null)
                		cache.put(key, bitmap);
            }
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return cache.get(key);
    }
    /**
     * 从网络中加载图片
     * @param url
     * @param imageView
     * @param width
     * @param height
     */
    @SuppressLint("HandlerLeak")
	public void queueJob(final String url, final ImageView imageView, final int width, final int height, final Map<String, Object> extData) {
        /* Create handler in UI thread. */  
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                String tag = imageViews.get(imageView);
                if (tag != null && tag.equals(url)) {  
                    if (msg.obj != null) {
                    	setBitmapToImageView(imageView, (Bitmap) msg.obj, extData);
                        try {
                        	if(cacheByFile){
                        		//向SD卡中写入图片缓存
                        		ImageUtils4AsyncBitmap.saveImage(imageView.getContext(), FileUtils.getFileNameByUrl(url), (Bitmap) msg.obj, fileCacheQuality, cacheDir, url);
                        	}
						} catch (IOException e) {
							e.printStackTrace();
						}
                    } 
                }  
            }  
        };  
  
        pool.execute(new Runnable() {
            public void run() {
                Message message = Message.obtain();
                message.obj = downloadBitmap(url, width, height);
                handler.sendMessage(message);
            }
        });
    }
  
    /**
     * 下载图片-可指定显示图片的高宽
     * @param url
     * @param width
     * @param height
     */
    private Bitmap downloadBitmap(String url, int width, int height) {
        Bitmap bitmap = null;
        try {
        	//http加载图片
        	AndroidHttpClient httpClient = AndroidHttpClient.newInstance(AppConfigFactory.getAppConfig().getAgentId());
        	HttpGet httpGet = new HttpGet(url);
        	HttpResponse response = httpClient.execute(httpGet);
        	if(response == null)
        		return null;
        	ByteArrayOutputStream os = new ByteArrayOutputStream();
        	response.getEntity().writeTo(os);
        	InputStream is = new ByteArrayInputStream(os.toByteArray());
        	os.close();
    		bitmap = BitmapFactory.decodeStream(is);
			if(width > 0 && height > 0) {
				//指定显示图片的高宽
				bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
			} 
			//放入缓存
			addBitmapToMemoryCache(url, bitmap);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			//404不重试
			if (defaultBmp != null) {
                addBitmapToMemoryCache(url, defaultBmp);// 失败直接放个默认图片，防止重复发无用请求
            }
		} catch (Exception e) {
            e.printStackTrace();
		} catch(OutOfMemoryError oom){
    		oom.printStackTrace();
    	}
        
        return bitmap;  
    }
    
    private void setBitmapToImageView(ImageView imageView, Bitmap bitmap, Map<String, Object> extData){
    	Drawable drawable = null;
    	if(executeCustom!=null){
    		executeCustom.doExecute(imageView, bitmap, extData);
    	}else if(executeBitmap!=null){
    		drawable = executeBitmap.doExecute(bitmap, extData);
    		//bitmap处理
        	imageView.setImageDrawable(drawable);
    	}else{
    		imageView.setImageBitmap(bitmap);
    	}
    	
    }
    
    /**
     * 实现此接口，自定义处理bitmap的逻辑，返回Drawable
     */
    public interface ExecuteBitmap{
    	public Drawable doExecute(Bitmap bitmap, Map<String, Object> extData);
    }
    
    /**
     * 实现此接口，自定义设置image的逻辑
     */
    public interface ExecuteCustom{
    	public void doExecute(ImageView imageView, Bitmap bitmap, Map<String, Object> extData);
    }

}
