package com.example.thatnight.wanandroid.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.Article;
import com.example.thatnight.wanandroid.view.activity.LoginActivity;

import java.util.List;

/**
 * Created by thatnight on 2017.10.31.
 */

public class UiHelper {

    public static void setSelected(View v) {
        v.setSelected(!v.isSelected());
    }

    //    public static void inputSoftWare(boolean isShow, View v) {
    //        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    //        if (isShow) {
    //            imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    //        } else {
    //            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    //        }
    //    }

    public static void inputSoftWare(boolean isShow, View v) {
        if (v == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        if (isShow) {
            v.setFocusable(true);
            v.setFocusableInTouchMode(true);
            v.requestFocus();
            imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            imm.showSoftInput(v, 0);
        } else {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }


    public static void inputSoftWare(AlertDialog.Builder builder, final View view) {
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                inputSoftWare(false, view);

            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                inputSoftWare(true, view);
            }
        });

        alertDialog.show();
    }

    public static void inputSoftWare(boolean isShow, AlertDialog dialog) {
        if (dialog == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) dialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        if (isShow) {
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        } else {
            imm.hideSoftInputFromWindow(dialog.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public static void inputSoftWare(boolean isShow, Activity activity) {
        if (activity == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        if (isShow) {
            imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
        } else {
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }


    public static void inputSoftWare(boolean isShow, Window window) {
        if (window == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) window.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        if (isShow) {
            imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
        } else {
            imm.hideSoftInputFromWindow(window.getDecorView().getWindowToken(), 0);
        }
    }

    public static final void showLoginDialog(final Context context) {
        new android.app.AlertDialog.Builder(context).setTitle("提示").setMessage("请先登录").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((Activity) context).startActivityForResult(new Intent(context, LoginActivity.class), Constant.REQUEST_LOGIN);
                ((Activity) context).overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        }).setNegativeButton("取消", null).show();
    }

    /**
     * 显示复制文章弹窗
     *
     * @param context
     * @param articles
     * @param pos
     */
    public static void showCopyArticleDialog(Context context, final List<Article> articles, final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(new String[]{"复制链接", "复制作者"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Article article = articles.get(pos);
                switch (which) {
                    case 0:
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(article.getTitle() + " [" + article.getAuthor() + "] ");
                        stringBuilder.append("\t\r\n");
                        stringBuilder.append(article.getLink());
                        TextHelper.copyToBoard(stringBuilder.toString());
                        break;
                    case 1:
                        String author = article.getAuthor();
                        TextHelper.copyToBoard(author);
                        break;
                    default:
                        break;
                }
            }
        }).show();
    }


}
