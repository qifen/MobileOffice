package com.wei.mobileoffice.util;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.wei.mobileoffice.OfficeAppContext;
import com.wei.mobileoffice.platform.util.Logger;
import com.wei.mobileoffice.platform.util.StringUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils4AsyncBitmap {

	private static final String TAG = ImageUtils4AsyncBitmap.class.getSimpleName();

	/**
	 * 写图片文件 在Android系统中，文件优先保存在sd卡图片目录下，如果没有sd卡则保存在
	 * /data/data/PACKAGE_NAME/files 目录下
	 * 
	 * @throws IOException
	 */
	public static void saveImage(Context context, String fileName,
			Bitmap bitmap, String extPath, String originUrl) throws IOException {
		saveImage(context, fileName, bitmap, 100, extPath, originUrl);
	}

	public static void saveImage(Context context, String fileName,
			Bitmap bitmap, int quality, String extPath, String originUrl) throws IOException {
		if (bitmap == null || fileName == null || context == null)
			return;

		FileOutputStream fos = null;
		try{
			if (FileUtils.checkSaveLocationExists()
					&& StringUtil.isNotBlank(extPath)) {
				File extFile = FileUtils.getAppExtFileDir(extPath);
				fos = new FileOutputStream(extFile.getAbsolutePath()
						+ File.separator + fileName);
			} else {
				fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			}
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			if(StringUtil.isNotBlank(originUrl) && originUrl.endsWith(".png")){
				bitmap.compress(CompressFormat.PNG, quality, stream);
			}else{
				bitmap.compress(CompressFormat.JPEG, quality, stream);
			}
			byte[] bytes = stream.toByteArray();
			fos.write(bytes);
		}catch(IOException ioe){
			throw ioe;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(fos!=null){
				try{
					fos.close();	
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 写图片文件到SD卡
	 * 
	 * @throws IOException
	 */
	public static void saveImageToSD(String filePath, Bitmap bitmap, int quality)
			throws IOException {
		if (bitmap != null) {
			FileOutputStream fos = new FileOutputStream(filePath);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.JPEG, quality, stream);
			byte[] bytes = stream.toByteArray();
			fos.write(bytes);
			fos.close();
		}
	}

	/**
	 * 获取bitmap
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static Bitmap getBitmap(Context context, String fileName,
			String extPath) {
		FileInputStream fis = null;
		Bitmap bitmap = null;
		try {
			if (FileUtils.checkSaveLocationExists()
					&& StringUtil.isNotBlank(extPath)) {
				File extFile = FileUtils.getAppExtFileDir(extPath);
				fis = new FileInputStream(extFile.getAbsolutePath()
						+ File.separator + fileName);
			} else {
				fis = context.openFileInput(fileName);
			}
			bitmap = BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return bitmap;
	}

	/**
	 * 计算图片缩放比例
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 将bitmap处理为圆角
	 * 
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * 灰度处理图片
	 * 
	 * @param drawable
	 */
	public static void filterDrawableToGray(Drawable drawable) {
		drawable.mutate();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter cf = new ColorMatrixColorFilter(cm);
		drawable.setColorFilter(cf);
	}

	/**
	 * 缩放并压缩保存图片
	 * 
	 * @param imgFile
	 * @param maxNumOfPixels
	 */
	public static void zoomAndSaveImg(File imgFile,
			int maxNumOfPixels) {
		if (imgFile == null || !imgFile.exists()) {
			return;
		}
		// 如果需要还可以处理，横竖方向
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 先指定原始大小
		options.inSampleSize = 1;
		// 只进行大小判断
		options.inJustDecodeBounds = true;
		// 调用此方法得到options得到图片的大小
		BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
		// 调用computeSampleSize得到图片缩放的比例
		options.inSampleSize = ImageUtils4AsyncBitmap.computeSampleSize(options, -1,
				maxNumOfPixels);
		// 得到了缩放的比例，正式读入BitMap数据
		options.inJustDecodeBounds = false;
		options.inDither = false;
		options.inPreferredConfig = Config.ARGB_8888;
		// 根据options参数，减少所需要的内存
		Bitmap sourceBitmap = BitmapFactory.decodeFile(
				imgFile.getAbsolutePath(), options);
		if(sourceBitmap==null){
			return;
		}
		//保证图片是正向上的
		int degree = readPictureDegree(imgFile.getAbsolutePath());
		sourceBitmap = rotaingImageView(degree, sourceBitmap);
		try {
			FileOutputStream out = new FileOutputStream(imgFile);
			if (sourceBitmap.compress(CompressFormat.JPEG, 70, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			Logger.e(TAG, "compressError,FileNotFoundException", e);
		} catch (IOException e) {
			Logger.e(TAG, "compressError,IOException", e);
		}
	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path 图片绝对路径
	 * @return degree 旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
	
	public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
        //旋转图片 动作  
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片  
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
	}
	
	/**
     * 通过uri获取文件的绝对路径
     * @param uri
     * @return
     */
	@SuppressWarnings("deprecation")
	public static String getAbsoluteImagePath(Activity context,Uri uri) {
		
		if(context==null || uri==null){
			return null;
		}
		
		String filePath = null;
		String mUriString = uri.toString();
		mUriString = Uri.decode(mUriString);
		
		String f1 = "file://mtn/sdcard" + File.separator;
		String f2 = "file://sdcard" + File.separator;
		if( mUriString.startsWith(f1)){    
			filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + mUriString.substring(f1.length());
		}
		else if( mUriString.startsWith(f2)){
			filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + mUriString.substring(f2.length());
		}else{
	        String[] proj={MediaStore.Images.Media.DATA};
	        Cursor cursor = context.managedQuery(uri,proj,null,null,null);
	        if(cursor!=null){
	        	int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	        	if(  cursor.getCount()>0 && cursor.moveToFirst() ){
	        		filePath = cursor.getString(column_index);
	            }
	        }
		}
		
        return filePath;
    }
	
	public static int convertDipOrPx(int dip) {
	    float scale = OfficeAppContext.getApplication().getResources().getDisplayMetrics().density;
	    return (int)(dip*scale + 0.5f*(dip>=0?1:-1));
	}

}
