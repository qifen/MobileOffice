package com.wei.mobileoffice.util;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class ImageUtils {

	public static String getImageUUID(String path) {
		return UUID.nameUUIDFromBytes(path.getBytes()).toString() + "_"
				+ path.hashCode();
	}

	public static int dipToPx(Context context, float dipValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5F);
	}

	public static float pxToDip(Context context, int pxValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5F);
	}

	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap,
			float reflectionGapPx) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1.0F, -1.0F);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0,
				(int) (height * reflectionGapPx), width,
				(int) (height * reflectionGapPx), matrix, false);
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(int) (height * (1.0F + reflectionGapPx)),
				Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0.0F, 0.0F, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0.0F, height, width, height + 4, deafalutPaint);
		canvas.drawBitmap(reflectionImage, 0.0F, height + 4, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0.0F, bitmap.getHeight(),
				0.0F, bitmapWithReflection.getHeight() + 4, 1895825407,
				16777215, Shader.TileMode.CLAMP);
		paint.setShader(shader);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

		canvas.drawRect(0.0F, height, width,
				bitmapWithReflection.getHeight() + 4, paint);
		bitmap.recycle();
		reflectionImage.recycle();
		return bitmapWithReflection;
	}

	public static Bitmap createReflectedImage(Bitmap originalBitmap) {

		int width = originalBitmap.getWidth();

		int height = originalBitmap.getHeight();

		Matrix matrix = new Matrix();

		matrix.preScale(1.0F, -1.0F);

		Bitmap reflectionBitmap = Bitmap.createBitmap(originalBitmap, 0,
				height / 2, width, height / 2, matrix, false);

		Bitmap withReflectionBitmap = Bitmap.createBitmap(width, height
				+ height / 3 + 2, Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(withReflectionBitmap);

		canvas.drawBitmap(originalBitmap, 0.0F, 0.0F, null);

		Paint defaultPaint = new Paint();
		canvas.drawRect(0.0F, height, width, height + 2, defaultPaint);

		canvas.drawBitmap(reflectionBitmap, 0.0F, height + 2, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0.0F,
				originalBitmap.getHeight(), 0.0F,
				withReflectionBitmap.getHeight(), 1895825407, 16777215,
				Shader.TileMode.MIRROR);
		paint.setShader(shader);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

		canvas.drawRect(0.0F, height, width, withReflectionBitmap.getHeight(),
				paint);

		return withReflectionBitmap;
	}

	public static void saveImageToSdcard(String path, Bitmap bitmap)
			throws Exception {
		if ((path == null) || ("".equals(path))) {
			return;
		}
		File file = new File(path);
		FileOutputStream fos = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			throw e;
		} finally {
			if (fos != null)
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public static Bitmap readBitmap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;

		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	public static void saveImageToSdcardandDelete(String path, Bitmap bitmap)
			throws Exception {
		saveImageToSdcard(path, bitmap);
		if ((bitmap != null) && (!bitmap.isRecycled())) {
			bitmap.recycle();
			bitmap = null;
		}
	}

	public static void saveImageByUri(Context context, String path, Uri paramUri) {
		InputStream inputStream = null;
		File file = new File(path);
		FileOutputStream fos = null;
		byte[] arrayOfByte = new byte[2048];
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			inputStream = context.getContentResolver()
					.openInputStream(paramUri);
			while (true) {
				int i = inputStream.read(arrayOfByte);
				if (i == -1)
					break;
				fos.write(arrayOfByte, 0, i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.flush();
					fos.close();
					fos = null;
				}
				if (inputStream != null) {
					inputStream.close();
					inputStream = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Bitmap getScaleBitmap(Bitmap srcBitmap, int wBoundPx,
			float rate) {
		Bitmap retBitmap = srcBitmap;
		if (srcBitmap.getWidth() > wBoundPx) {
			retBitmap = compressByWidth(srcBitmap, wBoundPx);
		}
		int hBoundPx = (int) (retBitmap.getWidth() * rate);
		if (retBitmap.getHeight() > hBoundPx) {
			retBitmap = Bitmap.createBitmap(retBitmap, 0, 0,
					retBitmap.getWidth(), hBoundPx);
		}
		return retBitmap;
	}

	public static Bitmap getScaleBitmap(Bitmap srcBitmap, float bound) {
		if (srcBitmap == null) {
			return null;
		}
		float width = srcBitmap.getWidth();
		float height = srcBitmap.getHeight();

		if ((width > bound) || (height > bound)) {
			float dstWidth = 0.0F;
			float dstHeight = 0.0F;
			if (width > height) {
				dstHeight = bound / width * height;
				dstWidth = bound;
			} else {
				dstWidth = bound / height * width;
				dstHeight = bound;
			}
			Bitmap temp = Bitmap.createScaledBitmap(srcBitmap, (int) dstWidth,
					(int) dstHeight, false);
			srcBitmap.recycle();
			srcBitmap = null;
			return temp;
		}
		return srcBitmap;
	}

	public static Bitmap compressByWidth(Bitmap srcBitmap, float widthBound) {
		if (srcBitmap == null) {
			return null;
		}
		float width = srcBitmap.getWidth();
		if (width > widthBound) {
			float height = srcBitmap.getHeight();
			float dstHeight = widthBound / width * height;
			Bitmap tmp = Bitmap.createScaledBitmap(srcBitmap, (int) widthBound,
					(int) dstHeight, false);

			srcBitmap.recycle();
			srcBitmap = null;
			return tmp;
		}
		return srcBitmap;
	}

    public static Bitmap getScaleBitmap(Bitmap bitmap, int newWidth,
                                        int newHeight) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        float scaleWidth = newWidth * 1.0F / width;
        float scaleHeight = newHeight * 1.0F / height;
        if (scaleWidth <= 0.0F) {
            scaleWidth = 1.0F;
        }
        if (scaleHeight <= 0.0F) {
            scaleHeight = 1.0F;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap
                .createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);
    }

	public static Bitmap getScaleBitmap(InputStream is, int width, int height) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inDither = false;

		BitmapFactory.decodeStream(is, null, options);
		options.inJustDecodeBounds = false;

		int be = 1;
		if (options.outWidth > options.outHeight)
			be = options.outWidth / width;
		else
			be = options.outHeight / height;
		if (be <= 0)
			be = 1;
		options.inSampleSize = be;

		return BitmapFactory.decodeStream(is, null, options);
	}

	public static Bitmap getScaleBitmap(String path, int width, int height) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inDither = false;

		BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false;

		int be = 1;
		if (options.outWidth > options.outHeight)
			be = options.outWidth / width;
		else
			be = options.outHeight / height;
		if (be <= 0)
			be = 1;
		options.inSampleSize = be;

		return BitmapFactory.decodeFile(path, options);
	}

	public static Bitmap getScaleBitmap(String path, int bound) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inDither = false;
		BitmapFactory.decodeFile(path, options);
		if ((options.outWidth > bound) || (options.outHeight > bound)) {
			int be = 1;
			if (options.outWidth > options.outHeight)
				be = options.outWidth / bound;
			else {
				be = options.outHeight / bound;
			}
			if (be <= 0) {
				be = 1;
			}
			options.inJustDecodeBounds = false;
			options.inSampleSize = be;
			return BitmapFactory.decodeFile(path, options);
		}
		return BitmapFactory.decodeFile(path);
	}

	public static Bitmap getScaleBitmapByWidth(String path, int widthBound) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inDither = false;
		BitmapFactory.decodeFile(path, options);
		if (options.outWidth > widthBound) {
			int be = options.outWidth / widthBound;
			options.inJustDecodeBounds = false;
			options.inSampleSize = be;
			return BitmapFactory.decodeFile(path, options);
		}
		return BitmapFactory.decodeFile(path);
	}

	public static Bitmap getScaleBitmap(byte[] data, int width, int height) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inDither = false;

		BitmapFactory.decodeByteArray(data, 0, data.length, options);
		options.inJustDecodeBounds = false;
		int be = 1;
		if (options.outWidth > options.outHeight)
			be = options.outWidth / width;
		else
			be = options.outHeight / height;
		if (be <= 0)
			be = 1;
		options.inSampleSize = be;

		return BitmapFactory.decodeByteArray(data, 0, data.length, options);
	}

	public static Bitmap getScaleBitmap(String path, int widthPx, int heightPx,
			Bitmap.Config config) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inDither = false;

		@SuppressWarnings("unused")
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		bitmap = null;

		options.inJustDecodeBounds = false;
		options.inPreferredConfig = config;

		int be = 1;
		if (options.outWidth > options.outHeight)
			be = options.outWidth / widthPx;
		else
			be = options.outHeight / heightPx;
		if (be <= 0)
			be = 1;
		options.inSampleSize = be;

		return BitmapFactory.decodeFile(path, options);
	}

	public static Bitmap getScaleBitmap(Context context, Uri uri, int width,
			int height) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inDither = false;

		InputStream is = null;
		try {
			is = context.getContentResolver().openInputStream(uri);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BitmapFactory.decodeStream(is, null, options);
		try {
			if(is !=null)
				is.close();
			is = null;
			is = context.getContentResolver().openInputStream(uri);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		options.inJustDecodeBounds = false;

		int be = 1;
		if (options.outWidth > options.outHeight)
			be = options.outWidth / width;
		else {
			be = options.outHeight / height;
		}
		if (be <= 0)
			be = 1;
		options.inSampleSize = be;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inPurgeable = true;
		options.inInputShareable = true;

		return BitmapFactory.decodeStream(is, null, options);
	}

    public static boolean compressImage(String srcPath, String compressPath, int byWidth, Bitmap.CompressFormat format, int quality) {
        Bitmap bitmap = null;
        Bitmap scaleBitmap = null;
        OutputStream out = null;
        try {
            bitmap = BitmapFactory.decodeFile(srcPath);
            if (bitmap == null) {
                return false;
            }

            scaleBitmap = getScaleBitmap(bitmap, byWidth,
                    (int) (byWidth * bitmap.getHeight() * 1f / bitmap.getWidth() + 0.5f));
            if (scaleBitmap == null) {
                return false;
            }
            out = new FileOutputStream(compressPath);
            return scaleBitmap.compress(format, quality, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            System.gc();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bitmap != null) {
                bitmap.recycle();
            }
            if (scaleBitmap != null) {
                scaleBitmap.recycle();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		RectF rectF = new RectF(rect);
		float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(-12434878);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
	
	public static Bitmap rotateBitmap(Context ctx, int resId, float rotate){
		Resources res = ctx.getResources();
		Bitmap img = BitmapFactory.decodeResource(res, resId);
		Matrix matrix = new Matrix();
		matrix.postRotate(rotate);
		int width = img.getWidth();  
		int height = img.getHeight();  
		return Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
	}
	
	/**
	 * Get a file path from a Uri. This will get the the path for Storage Access
	 * Framework Documents, as well as the _data field for the MediaStore and
	 * other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @author paulburke
	 */
    @TargetApi(19)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= 19;

        // DocumentProvider
        if (isKitKat && android.provider.DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = android.provider.DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = android.provider.DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = android.provider.DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }
                else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                }
                else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
	        String[] selectionArgs) {

	    Cursor cursor = null;
	    final String column = "_data";
	    final String[] projection = {
	            column
	    };

	    try {
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                null);
	        if (cursor != null && cursor.moveToFirst()) {
	            final int column_index = cursor.getColumnIndexOrThrow(column);
	            return cursor.getString(column_index);
	        }
	    } finally {
	        if (cursor != null)
	            cursor.close();
	    }
	    return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
	    return "com.android.providers.media.documents".equals(uri.getAuthority());
	}
}
