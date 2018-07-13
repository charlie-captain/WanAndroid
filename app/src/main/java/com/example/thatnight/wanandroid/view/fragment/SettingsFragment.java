package com.example.thatnight.wanandroid.view.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v14.preference.SwitchPreference;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceGroup;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.thatnight.wanandroid.BuildConfig;
import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.TinkerApp;
import com.example.thatnight.wanandroid.callback.LoginState;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.BmobAccount;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.utils.AccountUtil;
import com.example.thatnight.wanandroid.utils.GlideEngine;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.utils.LoginContextUtil;
import com.example.thatnight.wanandroid.utils.OkHttpResultCallback;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;
import com.example.thatnight.wanandroid.utils.SharePreferenceUtil;
import com.example.thatnight.wanandroid.utils.ToastUtil;
import com.example.thatnight.wanandroid.view.activity.LoginActivity;
import com.example.thatnight.wanandroid.view.customview.IconPreference;
import com.huantansheng.easyphotos.EasyPhotos;
import com.takisoft.fix.support.v7.preference.EditTextPreference;
import com.takisoft.fix.support.v7.preference.PreferenceCategory;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;
import com.tencent.bugly.beta.Beta;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.bmob.v3.okhttp3.Call;
import skin.support.SkinCompatManager;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.app.Activity.RESULT_OK;

/**
 * 设置界面
 */
public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    private SwitchPreference mSpDayLight;
    private SwitchPreference mSpAutoLogin;
    private Preference mEtpUserName;
    private Preference mEtpUserPwd;
    private PreferenceScreen mPsHelp, mPsUpdate;
    private ListPreference mTheme;
    private AboutFragment mAboutFragment;
    private IconPreference mIconPreference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        getListView().setBackgroundColor(ContextCompat.getColor(inflater.getContext(), R.color.background));

        return view;
    }

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_settings, rootKey);

        init();

    }

    private void init() {
        mSpDayLight = (SwitchPreference) findPreference(getString(R.string.pref_day_light));
        mSpAutoLogin = (SwitchPreference) findPreference(getString(R.string.pref_auto_login));

        mEtpUserName = findPreference(getString(R.string.pref_user_name));
        mEtpUserPwd = findPreference(getString(R.string.pref_user_password));
        mPsHelp = (PreferenceScreen) findPreference(getString(R.string.pref_help));
        mPsUpdate = (PreferenceScreen) findPreference(getString(R.string.pref_update));
        mTheme = (ListPreference) findPreference(getString(R.string.pref_theme));
        mIconPreference= (IconPreference) findPreference(getString(R.string.pref_user_icon));

        initData();

        mPsUpdate.setOnPreferenceClickListener(this);
        mPsHelp.setOnPreferenceClickListener(this);
        mTheme.setOnPreferenceChangeListener(this);
        mSpDayLight.setOnPreferenceClickListener(this);
        mSpAutoLogin.setOnPreferenceClickListener(this);
        mEtpUserName.setOnPreferenceClickListener(this);
        mEtpUserPwd.setOnPreferenceClickListener(this);
        mIconPreference.setOnPreferenceClickListener(this);
    }

    private void initData() {

        boolean isAutoLogin = (boolean) SharePreferenceUtil.getInstance().getBoolean(getString(R.string.sp_auto_login), true);
        if (isAutoLogin) {
            mSpAutoLogin.setChecked(true);
        } else {
            mSpAutoLogin.setChecked(false);
        }
        mPsUpdate.setSummary("版本号 " + BuildConfig.VERSION_NAME);

        String skin = (String) SharePreferenceUtil.getInstance().getString("skin_cn", "");
        if (!TextUtils.isEmpty(skin)) {
            mTheme.setSummary(skin);
            mTheme.setValue(skin);
        }
        if (LoginContextUtil.getInstance().getUserState() instanceof LoginState) {
            String userName = SharePreferenceUtil.getInstance().getString("account", "").toString();
            String userPassword = SharePreferenceUtil.getInstance().getString("password", "").toString();
            mEtpUserName.setTitle(TextUtils.isEmpty(userName) ? "用户名" : userName);
            String length = "";
            for (int i = 0; i < userPassword.length(); i++) {
                length += "*";
            }
            mEtpUserPwd.setTitle(length);
        } else {
            mEtpUserName.setTitle("Visitor");

            //删掉密码
            ((PreferenceCategory) (findPreference(getString(R.string.pref_user)))).removePreference(findPreference(getString(R.string.pref_user_password)));
        }

        if (AccountUtil.getBmobAccount().getIcon() != null) {
            mIconPreference.setIconUrl(AccountUtil.getBmobAccount().getIcon().getUrl());
        }
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {

        if (mIconPreference == preference) {
            EasyPhotos.createAlbum(getActivity(), true, GlideEngine.getInstance()).setFileProviderAuthority("android.support.v4.content.FileProvider").start(5);
        }

        if (mEtpUserName == preference) {
            ToastUtil.showToast(getActivity(), "用户名不支持更改!");
        }
        if (mEtpUserPwd == preference) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("修改密码");
            View view = View.inflate(getActivity(), R.layout.dialog_change_pwd, null);
            builder.setView(view);
            final EditText pwd = view.findViewById(R.id.et_password_cur);
            final EditText newPwd = view.findViewById(R.id.et_password);
            builder.setCancelable(false);
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (TextUtils.isEmpty(pwd.getText().toString().trim()) || TextUtils.isEmpty(newPwd.getText().toString().trim())) {
                        ToastUtil.showToast(getActivity(), "输入不能为空");
                    } else {
                        if (pwd.getText().toString().trim().equals(newPwd.getText().toString().trim())) {
                            ToastUtil.showToast(getActivity(), "不能使用旧密码");
                        } else {
                            Map<String, String> map = new HashMap<>();
                            map.put("curPassword", pwd.getText().toString().trim());
                            map.put("password", newPwd.getText().toString().trim());
                            map.put("repassword", newPwd.getText().toString().trim());

                            OkHttpUtil.getInstance().postAsync(Constant.URL_BASE + Constant.URL_CHANGE_PWD, map, true, new OkHttpResultCallback() {
                                @Override
                                public void onError(Call call, Exception e) {
                                    ToastUtil.showToast(getActivity(), e.getMessage());
                                }

                                @Override
                                public void onResponse(byte[] bytes) {
                                    String response = new String(bytes);
                                    if (Constant.URL_LOGIN.equals(response)) {
                                        startActivity(new Intent(getActivity(), LoginActivity.class));
                                        getActivity().finish();
                                    } else {
                                        ToastUtil.showToast(getActivity(), "服务器开小差了");
                                    }
                                }
                            });
                        }
                    }
                }
            }).setNegativeButton("取消", null).show();
        }
        //夜间模式
        if (mSpDayLight == preference) {
            if (mSpDayLight.isChecked()) {
                SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

            } else {
                String skinName = (String) SharePreferenceUtil.getInstance().getString("skin", "");
                if (!TextUtils.isEmpty(skinName)) {
                    SkinCompatManager.getInstance().loadSkin(skinName, SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
                } else {
                    SkinCompatManager.getInstance().restoreDefaultTheme();
                }
            }
        }

        //自动登录
        if (mSpAutoLogin == preference) {
            if (mSpAutoLogin.isChecked()) {
                SharePreferenceUtil.getInstance().putBoolean(getString(R.string.sp_auto_login), true);
            } else {
                SharePreferenceUtil.getInstance().putBoolean(getString(R.string.sp_auto_login), false);
            }
        }

        //检查更新
        if (mPsUpdate == preference) {
            Beta.checkUpgrade();
            //            Toast.makeText(getActivity(), "正在检查更新...", Toast.LENGTH_SHORT).show();
        }

        //关于
        if (mPsHelp == preference) {
            if (mAboutFragment == null) {
                mAboutFragment = new AboutFragment();
            }
            if (!mAboutFragment.isAdded()) {
                getParentFragment().getChildFragmentManager().beginTransaction().add(R.id.fl_content, mAboutFragment).commit();
            }
            getParentFragment().getChildFragmentManager().beginTransaction().hide(this).show(mAboutFragment).addToBackStack(null).commit();
            ((SettingsContainerFragment) getParentFragment()).setTitle("关于");
        }
        return false;

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (mTheme == preference) {
            CharSequence[] entries = mTheme.getEntries();
            int index = mTheme.findIndexOfValue(String.valueOf(newValue));
            mTheme.setSummary(entries[index]);
            CharSequence[] values = mTheme.getEntryValues();
            int selectedId = Integer.valueOf(String.valueOf(values[index]));
            mTheme.setValue(mTheme.getValue());
            mSpDayLight.setChecked(false);
            switch (selectedId) {
                case 0:
                    SkinCompatManager.getInstance().restoreDefaultTheme();
                    SharePreferenceUtil.getInstance().putString("skin", "");
                    SharePreferenceUtil.getInstance().putString("skin_cn", "安卓蓝");
                    break;
                case 1:
                    SkinCompatManager.getInstance().loadSkin("green", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
                    SharePreferenceUtil.getInstance().putString( "skin", "green");
                    SharePreferenceUtil.getInstance().putString( "skin_cn", "酷安绿");
                    break;
                case 2:
                    SkinCompatManager.getInstance().loadSkin("blue", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
                    SharePreferenceUtil.getInstance().putString( "skin", "blue");
                    SharePreferenceUtil.getInstance().putString( "skin_cn", "知乎蓝");
                    break;
                default:
                    break;
            }
        }
        return false;
    }

    public void hideFragments() {

        if (mAboutFragment != null) {
            getParentFragment().getChildFragmentManager().beginTransaction().hide(mAboutFragment).show(this).commit();
        }
    }

    public boolean isShowAbout() {
        return mAboutFragment != null && mAboutFragment.isVisible();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 5) {
            final String filePath = data.getStringArrayListExtra(EasyPhotos.RESULT_PATHS).get(0);
            Luban.with(TinkerApp.getApplication()).
                    load(filePath).
                    ignoreBy(100).
                    setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(File file) {
                            final BmobFile mBmobFile = new BmobFile(file);
                            BmobUser.loginByAccount(
                                    AccountUtil.getAccount().getUsername(),AccountUtil.getAccount().getPassword(),new LogInListener<BmobAccount>() {
                                @Override
                                public void done(final BmobAccount account, BmobException e) {
                                    mBmobFile.upload(new UploadFileListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                account.setIcon(mBmobFile);
                                                account.update(account.getObjectId(), new UpdateListener() {
                                                    @Override
                                                    public void done(BmobException e) {
                                                        if (e == null) {
                                                            Toast.makeText(getActivity().getApplicationContext(), "更新成功", Toast.LENGTH_SHORT).show();
                                                            mIconPreference.setIconUrl(filePath);
                                                        } else {
                                                            Toast.makeText(getActivity().getApplicationContext(), "更新失败, " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(getActivity().getApplicationContext(), "上传失败", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });

                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).launch();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
