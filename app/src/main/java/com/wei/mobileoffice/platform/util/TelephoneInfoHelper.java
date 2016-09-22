package com.wei.mobileoffice.platform.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Window;

import com.wei.mobileoffice.platform.AppContextBase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

public class TelephoneInfoHelper {
    static final String TAG = "TelephoneInfoHelper";
    static TelephoneInfoHelper telephoneHelper = null;
    static boolean isInitedByActivity = false;
    private String m_IMEI = null;
    private String m_IMSI = null;
    private String m_Product;
    private String m_ProductVersionName;
    private String m_ProductVersionCode;
    private int m_ScreenHeight;
    private int m_ScreenWidth;
    private String mtype = null;
    private String brand = null;
    private String numer = null;
    private String resoulution;
    private String sID = null;
    private static final String INSTALLATION = "INSTALLATION";

    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getM_Product() {
        return m_Product;
    }
    public String getResoulution() {
        return resoulution;
    }

    public void setResoulution() {
        this.resoulution = getScreenHeight() + "X" + getScreenWidth();
    }
    public String getOsVersion() {
        return osVersion;
    }

    private String osVersion = null;

    private TelephoneInfoHelper(Activity activity) {
        if(activity!=null){
            Window localWindow = activity.getWindow();
            DisplayMetrics dm = new DisplayMetrics();
            localWindow.getWindowManager().getDefaultDisplay().getMetrics(dm);
            float width = dm.widthPixels;
            float height = dm.heightPixels;
            setScreen(Math.round(width), Math.round(height));
            setResoulution();
            isInitedByActivity = true;
            Logger.i(TAG, "TelephoneInfoHelper(Activity activity),activity!=null");
        }else{
            Logger.i(TAG, "TelephoneInfoHelper(Activity activity),activity=null");
        }
        TelephonyManager localTelephonyManager = (TelephonyManager) AppContextBase.getApplication().getSystemService(Context.TELEPHONY_SERVICE);
        String imei = localTelephonyManager.getDeviceId();
        setIMEI(imei);
        String imsi = localTelephonyManager.getSubscriberId();
        setIMSI(imsi);
        mtype = android.os.Build.MODEL; // 手机型号
        brand = android.os.Build.BRAND; // 手机厂商
        osVersion=android.os.Build.VERSION.RELEASE; //手机os的版本
        //不取手机号，很多机器本身取不到，而且加这个360会蛋疼报警，影响体验
        //numer = localTelephonyManager.getLine1Number(); // 手机号码，有的可得，有的不可得
    }

    public static String getTag() {
        return TAG;
    }

    public static TelephoneInfoHelper getTelephoneHelper() {
        if(telephoneHelper!=null){
            Logger.i(TAG, "getTelephoneHelper(),telephoneHelper is inited");
            return telephoneHelper;
        }
        Logger.i(TAG, "getTelephoneHelper(),telephoneHelper is not inited");
        return getTelephoneHelper(null);
    }

    public String getM_IMEI() {
        return m_IMEI;
    }

    public String getM_IMSI() {
        return m_IMSI;
    }

    public int getM_ScreenHeight() {
        return m_ScreenHeight;
    }

    public int getM_ScreenWidth() {
        return m_ScreenWidth;
    }

    public String getMtype() {
        return mtype;
    }

    public String getNumer() {
        return numer;
    }

    /**
     * activity传null不要调用这个方法
     * 会每次都重新初始化
     * 没有activity的调用getTelephoneHelper()方法
     * @param activity
     * @return
     */
    public static TelephoneInfoHelper getTelephoneHelper(Activity activity) {
        if (telephoneHelper == null || !isInitedByActivity){
            Logger.i(TAG, "initTelephoneHelper,isInited=" + isInitedByActivity);
            try {
                telephoneHelper = new TelephoneInfoHelper(activity);
                Context ctx = AppContextBase.getApplication();
                String packageName = ctx.getPackageName();
                PackageInfo localPackageInfo = ctx.getPackageManager().getPackageInfo(packageName, 100);
                telephoneHelper.m_ProductVersionName = localPackageInfo.versionName;
                telephoneHelper.m_ProductVersionCode = String.valueOf(localPackageInfo.versionCode);
                telephoneHelper.m_Product= localPackageInfo.packageName;
                return telephoneHelper;
            } catch (NameNotFoundException localNameNotFoundException) {
                localNameNotFoundException.printStackTrace();
            } catch(Exception ex){
                Logger.e(TAG, ex.toString());
                return telephoneHelper;
            }
        }
        return telephoneHelper;
    }
    private Object lock = new Object();
    public String deviceId() {
        if (sID == null) {
            synchronized (lock) {
                if(sID !=null){
                    return sID;
                }
                Logger.d(TAG, "dir:"+AppContextBase.getApplication().getFilesDir());
                File installation = new File(AppContextBase.getApplication().getFilesDir(), INSTALLATION);
                try {
                    if (!installation.exists())
                        writeInstallationFile(installation);
                    sID = readInstallationFile(installation);
                } catch (Exception e) {
                    e.printStackTrace();//生成失败，返回clientID
                    sID = getClientID();
                    return sID;
                }
            }
        }
        return sID;
    }
    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }
    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }

    public String getClientID(){
        if ((this.m_IMEI == null) || (this.m_IMSI == null))
            return "123450000000000|000000000000000";
        StringBuilder builder = new StringBuilder(getIMEI());
        builder.append("|");
        builder.append(getIMSI());
        return builder.toString();
    }

    public String getIMEI() {
        return this.m_IMEI;
    }

    public String getIMSI() {
        return this.m_IMSI;
    }

    public String getProductId() {
        return "ANDROID";// + getScreenHeight() + "X" + getScreenWidth()
    }

    //只用来显示
    public String getProductVersionName() {
        return this.m_ProductVersionName;
    }

    //用来升级使用
    public String getProductVersionCode() {
        return this.m_ProductVersionCode;
    }

    public int getScreenHeight() {
        return this.m_ScreenHeight;
    }

    public int getScreenWidth() {
        return this.m_ScreenWidth;
    }

    public void setIMEI(String imei) {
        if (imei != null){
            String str = String.valueOf(imei);
            imei = (str + "123456789012345").substring(0, 15);
        }
        this.m_IMEI = imei;
    }

    public void setIMSI(String imsi) {
        if (imsi != null){
            String str = String.valueOf(imsi);
            imsi = (str + "123456789012345").substring(0, 15);
        }
        this.m_IMSI = imsi;
    }

    public void setScreen(int width, int height) {
        this.m_ScreenWidth = width;
        this.m_ScreenHeight = height;
    }
}
