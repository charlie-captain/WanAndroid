package com.example.thatnight.wanandroid.utils;

import com.example.thatnight.wanandroid.entity.Account;
import com.example.thatnight.wanandroid.entity.BmobAccount;

import cn.bmob.v3.BmobUser;

/**
 * date: 2018/7/13
 * author: thatnight
 * 用户管理类
 */
public class AccountUtil {
    public static final String KEY_ACCOUNT = "sp_account";

    public static void saveAccount(Account account) {
        if (account == null) {
            return;
        }
        String json = GsonUtil.gsonToJson(account);
        SharePreferenceUtil.getInstance().putString(KEY_ACCOUNT, json);
    }

    public static Account getAccount() {
        String json = SharePreferenceUtil.getInstance().getString(KEY_ACCOUNT, null);
        if (json == null) {
            return null;
        }
        Account account = GsonUtil.gsonToBean(json, Account.class);
        return account;
    }

    /**
     * 获取当前在线用户
     *
     * @return
     */
    public static BmobAccount getBmobAccount() {
        return BmobUser.getCurrentUser(BmobAccount.class);
    }

}
