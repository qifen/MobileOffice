package com.wei.mobileoffice.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;

import com.wei.mobileoffice.R;


public class AndroidUtils {

    /*
     * 通过ACTION_DIAL拨打电话，如果系统找不到拨打电话应用，弹出提示
     */
    public static void dialPhone(Context ctx, String phone) {
	try {
	    ctx.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone)));
	} catch (Exception e) {
	    UIHelper.showToastAsCenter(ctx, R.string.no_dial_phone_app);
	    e.printStackTrace();
	}

    }
    /*
     * 通过ACTION_PICK获取联系人信息，如果系统找不到联系人应用，弹出提示
     */
    public static void pickContact(Activity mActivity, int requestCode) {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setData(ContactsContract.Contacts.CONTENT_URI);
            mActivity.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            UIHelper.showToastAsCenter(mActivity, R.string.no_pick_contacts_app);
            e.printStackTrace();
        }
    }
    
    /*
     * 通过ACTION_GET_CONTENT获取image，如果系统找不到图片选取应用，弹出提示
     */
    public static void selectPhoto(Activity mActivity, int requestCode) {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            mActivity.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            UIHelper.showToastAsCenter(mActivity, R.string.no_pick_photo_app);
            e.printStackTrace();
        }
    }
    
    /*
     * 通过MediaStore.ACTION_IMAGE_CAPTURE拍摄照片，如果系统找不到拍照应用，弹出提示
     */
    public static void takePhoto(Activity mActivity, Uri uri, int requestCode) {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
            mActivity.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            UIHelper.showToastAsCenter(mActivity, R.string.no_take_photo_app);
            e.printStackTrace();
        }
    }

}
