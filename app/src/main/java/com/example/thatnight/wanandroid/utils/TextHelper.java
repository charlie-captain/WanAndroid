package com.example.thatnight.wanandroid.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.design.widget.Snackbar;

import com.example.thatnight.wanandroid.base.TinkerApp;

/**
 * date: 2018/9/4
 * author: thatnight
 */
public class TextHelper {

    public static void copyToBoard(String text) {
        ClipboardManager clipboardManager = (ClipboardManager) TinkerApp.getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(ClipData.newPlainText("copy_text", text));
            ToastUtil.showToast("已复制到剪贴板");
        }
    }
}
