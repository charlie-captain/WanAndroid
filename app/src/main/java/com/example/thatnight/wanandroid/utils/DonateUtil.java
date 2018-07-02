package com.example.thatnight.wanandroid.utils;

import android.app.Activity;
import android.content.Context;
import android.didikee.donate.AlipayDonate;
import android.didikee.donate.WeiXinDonate;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.view.activity.MainActivity;

import java.io.File;
import java.io.InputStream;

/**
 * date: 2018/7/2
 * author: thatnight
 */
public class DonateUtil {

    /**
     * 捐献支付宝
     * @param context
     */
    public static void donateAlipay(Activity context) {
        boolean hasInstalledAlipay = AlipayDonate.hasInstalledAlipayClient(context);
        if (hasInstalledAlipay) {
            AlipayDonate.startAlipayClient(context, "FKX02930VD4HBWHJWZW87B");
            ToastUtil.showToast(context,"感谢你的支持");
        }else{
            ToastUtil.showToast(context,"找不到应用，该怎么办");
        }
    }


    /**
     * 捐献微信
     * @param context
     */
    public static void donateWechat(Activity context) {
        boolean hasInstallWechat = WeiXinDonate.hasInstalledWeiXinClient(context);
        if(hasInstallWechat){
            InputStream is = context.getResources().openRawResource(R.raw.wechat);
            String qrPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "WanAndroid" + File.pathSeparator + "wechat.png";
            WeiXinDonate.saveDonateQrImage2SDCard(qrPath, BitmapFactory.decodeStream(is));
            WeiXinDonate.donateViaWeiXin(context, qrPath);
            ToastUtil.showToast(context,"感谢你的支持");
        }else{
            ToastUtil.showToast(context,"找不到应用，该怎么办");
        }
    }
}
