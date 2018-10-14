package com.example.thatnight.wanandroid.view.fragment;


import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.thatnight.wanandroid.BuildConfig;
import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.App;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.Account;
import com.example.thatnight.wanandroid.entity.BmobAccount;
import com.example.thatnight.wanandroid.utils.AccountUtil;
import com.example.thatnight.wanandroid.utils.DayLightUtil;
import com.example.thatnight.wanandroid.utils.GlideEngine;
import com.example.thatnight.wanandroid.utils.LoginContextUtil;
import com.example.thatnight.wanandroid.utils.OkHttpResultCallback;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;
import com.example.thatnight.wanandroid.utils.SharePreferenceUtil;
import com.example.thatnight.wanandroid.utils.ToastUtil;
import com.example.thatnight.wanandroid.utils.ViewHepler;
import com.example.thatnight.wanandroid.view.activity.LoginActivity;
import com.example.thatnight.wanandroid.view.customview.IconPreference;
import com.huantansheng.easyphotos.EasyPhotos;
import com.takisoft.fix.support.v7.preference.PreferenceCategory;
import com.tencent.bugly.beta.Beta;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import okhttp3.Call;
import skin.support.SkinCompatManager;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.app.Activity.RESULT_OK;

/**
 * 设置界面
 */
public class SettingsFragment extends android.support.v7.preference.PreferenceFragmentCompat implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    private SwitchPreference mSpDayLight;
    private SwitchPreference mSpAutoLogin;
    private SwitchPreference mSpAutoDayLight;
    private SwitchPreference mSpBanner;
    private SwitchPreference mSpComment;
    private Preference mEtpUserName;
    private Preference mEtpUserNickName;
    private Preference mEtpUserPwd;
    private PreferenceScreen mPsHelp, mPsUpdate;
    private ListPreference mTheme;
    private AboutFragment mAboutFragment;
    private IconPreference mIconPreference;
    public static final int CODE_REQUEST = 5;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_settings, rootKey);
        init();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

//    @Override
//    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
//        setPreferencesFromResource(R.xml.preference_settings, rootKey);
//        init();
//    }

    private void init() {
        mSpDayLight = (SwitchPreference) findPreference(getString(R.string.pref_day_light));
        mSpAutoLogin = (SwitchPreference) findPreference(getString(R.string.pref_auto_login));
        mSpAutoDayLight = (SwitchPreference) findPreference(getString(R.string.pref_auto_day_light));
        mSpComment = (SwitchPreference) findPreference(getString(R.string.pref_comment));
        mSpBanner = (SwitchPreference) findPreference(getString(R.string.pref_banner));

        mEtpUserName = findPreference(getString(R.string.pref_user_name));
        mEtpUserNickName = findPreference(getString(R.string.pref_user_nickname));
        mEtpUserPwd = findPreference(getString(R.string.pref_user_password));
        mPsHelp = (PreferenceScreen) findPreference(getString(R.string.pref_help));
        mPsUpdate = (PreferenceScreen) findPreference(getString(R.string.pref_update));
        mTheme = (ListPreference) findPreference(getString(R.string.pref_theme));
        mIconPreference = (IconPreference) findPreference(getString(R.string.pref_user_icon));

        initData();

        mPsUpdate.setOnPreferenceClickListener(this);
        mPsHelp.setOnPreferenceClickListener(this);
        mTheme.setOnPreferenceChangeListener(this);
        mSpDayLight.setOnPreferenceClickListener(this);
        mSpAutoLogin.setOnPreferenceClickListener(this);
        mSpAutoDayLight.setOnPreferenceClickListener(this);
        mEtpUserName.setOnPreferenceClickListener(this);
        mEtpUserPwd.setOnPreferenceClickListener(this);
        mIconPreference.setOnPreferenceClickListener(this);
        mEtpUserNickName.setOnPreferenceClickListener(this);
        mSpBanner.setOnPreferenceClickListener(this);
    }

    private void initData() {

        boolean isAutoLogin = (boolean) SharePreferenceUtil.getInstance().getBoolean(getString(R.string.sp_auto_login), true);
        if (isAutoLogin) {
            mSpAutoLogin.setChecked(true);
        } else {
            mSpAutoLogin.setChecked(false);
        }
        mPsUpdate.setSummary("版本号 " + BuildConfig.VERSION_NAME);

        //自动夜间模式
        boolean isAutoDayLight = SharePreferenceUtil.getInstance().getBoolean(getString(R.string.pref_auto_day_light), false);
        if (isAutoDayLight) {
            mSpAutoDayLight.setChecked(true);
            mSpAutoDayLight.setSummaryOn(SharePreferenceUtil.getInstance().optString(getString(R.string.summary_auto_day_light)));
            mSpDayLight.setChecked(true);
        } else {
            mSpAutoDayLight.setChecked(false);
        }

        //皮肤选择
        String skin = (String) SharePreferenceUtil.getInstance().optString("skin_cn");
        if (!TextUtils.isEmpty(skin)) {
            mTheme.setSummary(skin);
            mTheme.setValue(skin);
        }
        if (LoginContextUtil.getInstance().isLogin()) {
            Account account = AccountUtil.getAccount();
            if (account == null) {
                return;
            }
            String userName = account.getUsername();
            String userPassword = account.getPassword();
            mEtpUserName.setTitle(TextUtils.isEmpty(userName) ? "用户名" : userName);
            String length = "";
            for (int i = 0; i < userPassword.length(); i++) {
                length += "*";
            }
            mEtpUserPwd.setTitle(length);
            if (AccountUtil.getBmobAccount() != null) {
                mEtpUserNickName.setTitle(AccountUtil.getBmobAccount().getNickName());
            }
        } else {
            //            mEtpUserName.setTitle("Visitor");
            ((PreferenceCategory) (findPreference(getString(R.string.pref_user)))).removeAll();
            ((PreferenceCategory) (findPreference(getString(R.string.pref_user)))).setTitle("无用户信息");
        }

        if (LoginContextUtil.getInstance().isLogin() && AccountUtil.getBmobAccount() != null && AccountUtil.getBmobAccount().getIcon() != null) {
            mIconPreference.setIconUrl(AccountUtil.getBmobAccount().getIcon().getUrl());
        }

        //Banner
        mSpBanner.setChecked(SharePreferenceUtil.getInstance().optBoolean(getString(R.string.pref_banner)));


    }


    @Override
    public boolean onPreferenceClick(Preference preference) {

        if (mIconPreference == preference) {
            if (!LoginContextUtil.getInstance().isLogin()) {
                ToastUtil.showToast("请先登录!");
            } else {
                EasyPhotos.createAlbum(getActivity(), true, GlideEngine.getInstance()).setFileProviderAuthority("android.support.v4.content.FileProvider").start(5);
            }
        }

        if (mEtpUserNickName == preference) {
            showNickNameDialog();
        }

        //用户名
        if (mEtpUserName == preference) {
            ToastUtil.showToast("用户名不支持更改!");
        }
        //修改密码
        if (mEtpUserPwd == preference) {
            showChangePwdDialog();
        }
        //夜间模式
        if (mSpDayLight == preference) {
            if (mSpDayLight.isChecked()) {
                SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                String skinName = (String) SharePreferenceUtil.getInstance().optString("skin");
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

        //自动夜间模式
        if (mSpAutoDayLight == preference) {
            if (mSpAutoDayLight.isChecked()) {
                if (!TextUtils.isEmpty(SharePreferenceUtil.getInstance().optString(getString(R.string.summary_auto_day_light)))) {
                    //如果已存在则直接打开
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setMessage("是否更改时间段?").setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showTimePickerDialog();
                        }
                    }).setNegativeButton("否", null);
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            mSpAutoDayLight.setSummaryOn(SharePreferenceUtil.getInstance().optString(getString(R.string.summary_auto_day_light)));
                        }
                    }).show();
                    setAutoDayLight(true);
                }
            } else {
                setAutoDayLight(false);
            }
        }

        //Banner
        if (mSpBanner == preference) {
            ViewHepler.saveBannerConfig(mSpBanner.isChecked());
        }

        //评论
        if (mSpComment == preference) {
            ViewHepler.saveCommentConfig(mSpComment.isChecked());
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

    /**
     * 用户昵称修改
     */
    private void showNickNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("修改昵称");
        View view = View.inflate(getActivity(), R.layout.dialog_write_comment, null);
        builder.setView(view);
        final EditText nickName = view.findViewById(R.id.et_comment_write);
        nickName.setHint("请输入昵称...");
        builder.setCancelable(false);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(nickName.getText().toString().trim())) {
                    ToastUtil.showToast("输入不能为空");
                } else {
                    if (AccountUtil.getBmobAccount() != null) {
                        Account localAccount = AccountUtil.getAccount();
                        BmobUser.loginByAccount(localAccount.getUsername(), localAccount.getPassword(), new LogInListener<BmobAccount>() {
                            @Override
                            public void done(BmobAccount account, BmobException e) {
                                if (e == null) {
                                    account.setNickName(nickName.getText().toString().trim());
                                    account.update(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                mEtpUserNickName.setTitle(nickName.getText().toString().trim());
                                            } else {
                                                ToastUtil.showToast("修改失败, " + e.toString());
                                            }
                                        }
                                    });
                                } else {
                                    ToastUtil.showToast("修改失败, " + e.toString());
                                }
                            }
                        });
                    }
                }
            }
        }).setNegativeButton("取消", null).show();
    }

    /**
     * 修改密码弹窗
     */
    private void showChangePwdDialog() {
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
                    ToastUtil.showToast("输入不能为空");
                } else {
                    if (pwd.getText().toString().trim().equals(newPwd.getText().toString().trim())) {
                        ToastUtil.showToast("不能使用旧密码");
                    } else {
                        Map<String, String> map = new HashMap<>();
                        map.put("curPassword", pwd.getText().toString().trim());
                        map.put("password", newPwd.getText().toString().trim());
                        map.put("repassword", newPwd.getText().toString().trim());

                        OkHttpUtil.getInstance().postAsync(Constant.URL_BASE + Constant.URL_CHANGE_PWD, map, true, new OkHttpResultCallback() {
                            @Override
                            public void onError(Call call, Exception e) {
                                ToastUtil.showToast(e.getMessage());
                            }

                            @Override
                            public void onResponse(byte[] bytes) {
                                String response = new String(bytes);
                                if (Constant.URL_LOGIN.equals(response)) {
                                    startActivity(new Intent(getActivity(), LoginActivity.class));
                                    getActivity().finish();
                                } else {
                                    ToastUtil.showToast("服务器开小差了");
                                }
                            }
                        });
                    }
                }
            }
        }).setNegativeButton("取消", null).show();
    }

    /**
     * 显示时间选择器
     */
    private void showTimePickerDialog() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                final int hourOne = hourOfDay;
                final int minuteOne = minute;
                TimePickerDialog dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mSpAutoDayLight.setSummaryOn(hourOne + ":" + minuteOne + " ~ " + hourOfDay + ":" + minute);
                        SharePreferenceUtil.getInstance().putString(getString(R.string.summary_auto_day_light), hourOne + ":" + minuteOne + " ~ " + hourOfDay + ":" + minute);
                        setAutoDayLight(true);
                        //马上进行设置
                        DayLightUtil.autoDayLight();
                    }
                }, hourOne, minuteOne + 1, true);
                dialog.setTitle("结束时间");
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        setAutoDayLight(false);
                    }
                });
                dialog.show();
            }

        }, hour, minute, true);
        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                setAutoDayLight(false);
            }
        });
        timePickerDialog.setTitle("开始时间");
        timePickerDialog.show();
    }

    /**
     * 设置自动登陆
     *
     * @param isChecked
     */
    private void setAutoDayLight(boolean isChecked) {
        mSpAutoDayLight.setChecked(isChecked);
        if (isChecked) {
            mSpDayLight.setChecked(isChecked);
        }
        SharePreferenceUtil.getInstance().putBoolean(getString(R.string.pref_auto_day_light), isChecked);
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
                    SharePreferenceUtil.getInstance().putString("skin", "green");
                    SharePreferenceUtil.getInstance().putString("skin_cn", "酷安绿");
                    break;
                case 2:
                    SkinCompatManager.getInstance().loadSkin("blue", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
                    SharePreferenceUtil.getInstance().putString("skin", "blue");
                    SharePreferenceUtil.getInstance().putString("skin_cn", "知乎蓝");
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
        if (resultCode == RESULT_OK && requestCode == CODE_REQUEST) {
            final String filePath = data.getStringArrayListExtra(EasyPhotos.RESULT_PATHS).get(0);
            Luban.with(App.getApplication()).
                    load(filePath).
                    ignoreBy(100).
                    setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                            ToastUtil.showToast("正在处理中...");
                        }

                        @Override
                        public void onSuccess(File file) {
                            final BmobFile mBmobFile = new BmobFile(file);
                            //需登录才能更新
                            BmobUser.loginByAccount(AccountUtil.getAccount().getUsername(), AccountUtil.getAccount().getPassword(), new LogInListener<BmobAccount>() {
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
                                                            ToastUtil.showToast("更新成功");
                                                            mIconPreference.setIconUrl(filePath);
                                                            EventBus.getDefault().post(Constant.REFRESH);
                                                        } else {
                                                            ToastUtil.showToast("更新失败, " + e.getMessage());
                                                        }
                                                    }
                                                });
                                            } else {
                                                ToastUtil.showToast("上传失败");
                                            }
                                        }
                                    });
                                }
                            });

                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtil.showToast(e.getMessage());
                        }
                    }).launch();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
