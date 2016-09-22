package com.wei.mobileoffice.util;

import android.os.Environment;

import com.wei.mobileoffice.constants.AppConstants.FileConstants;
import com.wei.mobileoffice.platform.util.MD5Helper;
import com.wei.mobileoffice.platform.util.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;

public class FileUtils {

	/**
	 * 根据url MD5编码出图片缓存的文件名
	 * @param url
	 * @return
	 */
	public static String getFileNameByUrl(String url){
		if(StringUtil.isBlank(url)){
			return null;
		}
		return MD5Helper.md5(url);
	}
	
	/**
	 * 检查是否安装SD卡
	 * @return
	 */
	public static boolean checkSaveLocationExists() {
		String sDCardStatus = Environment.getExternalStorageState();
		boolean status;
		if (sDCardStatus.equals(Environment.MEDIA_MOUNTED)) {
			status = true;
		} else
			status = false;
		return status;
	}
	
	/**
	 * 获取APP在SD卡上的存储路径
	 * @param subDir
	 * @return
	 */
	public static File getAppExtFileDir(String subDir){
		if(!checkSaveLocationExists()){
			return null;
		}
		File sdCardDir = Environment.getExternalStorageDirectory();
		File subDirFile = new File(sdCardDir.getAbsolutePath() + FileConstants.APP_DIR_NAME + subDir);
		// 创建图片保存目录
		if (!subDirFile.exists()) {
			subDirFile.mkdirs();
		}
		return subDirFile;
	}
	
	/**
	 * 根据文件绝对路径获取文件名
	 * @param filePath
	 * @return
	 */
	public static String getFileName( String filePath )
	{
		if( StringUtil.isBlank(filePath) )	return "";
		return filePath.substring( filePath.lastIndexOf( File.separator )+1 );
	}
	
	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 * @param newPath
	 */
	public static boolean copyFile(String oldPath, String newPath) {
	    FileOutputStream fs = null;
	    InputStream inStream = null;
		try {
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) {
				inStream = new FileInputStream(oldPath); // 读入原文件
				fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
		    try {
		        if( inStream != null)
		            inStream.close();
                if( fs !=null)
                    fs.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
		}
	}
	
	/**
	 * 把目标文件拷贝到公共图片目录
	 * @param filePath
	 */
	public static boolean copyToPubImgDir(String filePath){
		if(StringUtil.isBlank(filePath)){
			return false;
		}
		File extFile = getAppExtFileDir(FileConstants.PUB_IMG_DIR);
		String newFileName = getFileName(filePath);
		if(extFile==null || StringUtil.isBlank(newFileName)){
			return false;
		}
		return copyFile(filePath, extFile.getAbsolutePath() + File.separator + newFileName);
	}
	
	/**
	 * 删除dirPath文件夹中超出maxSize的过时的文件
	 * 
	 */
	public static void deleteFilesOutOfMaxSize(String dirPath, int maxSize) {
		File dir = new File(dirPath);
		if (dir.exists() && dir.isDirectory()) {
			File[] files = dir.listFiles();
			if (files.length <= maxSize) {
				return;
			}
			// 排序 由旧到新
			Arrays.sort(files, new FileCompratorByLastModified());
			int len = files.length-maxSize*9/10;
			for (int i =0; i <len; i++) {
				files[i].delete();
			}
		}
	}
	public static class FileCompratorByLastModified implements Comparator<File> {
		@Override
		public int compare(File o1, File o2) {
			long diff = o1.lastModified() - o2.lastModified();
			if (diff > 0)
				return 1;
			else if (diff < 0)
				return -1;
			else
				return 0;
		}

	}
}
