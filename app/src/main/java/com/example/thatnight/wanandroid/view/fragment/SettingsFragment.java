package com.example.thatnight.wanandroid.view.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.thatnight.wanandroid.BuildConfig;
import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.utils.SharePreferenceUtil;
import com.example.thatnight.wanandroid.view.activity.AboutActivity;
import com.tencent.bugly.beta.Beta;

import skin.support.SkinCompatManager;
import skin.support.content.res.SkinCompatResources;
import skin.support.widget.SkinCompatHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    private SwitchPreference mSpDayLight, mSpAutoLogin;
    private EditTextPreference mEtpUserName;
    private EditTextPreference mEtpUserPwd;
    private PreferenceScreen mPsHelp, mPsUpdate;
    private ListPreference mTheme;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_settings);
        init();
    }

    private void init() {
        mSpDayLight = (SwitchPreference) findPreference(getString(R.string.pref_day_light));
        mSpAutoLogin = (SwitchPreference) findPreference(getString(R.string.pref_auto_login));

//        mEtpUserName = (EditTextPreference) findPreference(getString(R.string.pref_user_name));
//        mEtpUserPwd = (EditTextPreference) findPreference(getString(R.string.pref_user_password));
        mPsHelp = (PreferenceScreen) findPreference(getString(R.string.pref_help));
        mPsUpdate = (PreferenceScreen) findPreference(getString(R.string.pref_update));
        mTheme = (ListPreference) findPreference(getString(R.string.pref_theme));

        boolean isAutoLogin = (boolean) SharePreferenceUtil.get(getActivity().getApplicationContext(), getString(R.string.sp_auto_login), true);
        if (isAutoLogin) {
            mSpAutoLogin.setChecked(true);
        } else {
            mSpAutoLogin.setChecked(false);
        }
        mPsUpdate.setSummary("版本号 " + BuildConfig.VERSION_NAME);
        mPsUpdate.setOnPreferenceClickListener(this);
        mPsHelp.setOnPreferenceClickListener(this);
        mTheme.setOnPreferenceChangeListener(this);
        mSpDayLight.setOnPreferenceClickListener(this);
        mSpAutoLogin.setOnPreferenceClickListener(this);
        String skin = (String) SharePreferenceUtil.get(getActivity().getApplicationContext(), "skin_cn", "");
        if (!TextUtils.isEmpty(skin)) {
            mTheme.setSummary(skin);
            mTheme.setValue(skin);
        }
//        mTheme.setValue(mTheme.getValue());
    }

    @Override
    public void onResume() {
        super.onResume();
//        String userName = SharePreferenceUtil.get(getActivity().getApplicationContext(), "account", "").toString();
//        String userPassword = SharePreferenceUtil.get(getActivity().getApplicationContext(), "password", "").toString();
//        mEtpUserName.setTitle(userName);
//        mEtpUserName.setText(userName);
//        String length = "";
//        for (int i = 0; i < userPassword.length(); i++) {
//            length += "*";
//        }
//        mEtpUserPwd.setTitle(length);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        //夜间模式
        if (mSpDayLight == preference) {
            if (mSpDayLight.isChecked()) {
                SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
            } else {
                String skinName = (String) SharePreferenceUtil.get(getActivity().getApplicationContext(), "skin", "");
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
                SharePreferenceUtil.put(getActivity().getApplicationContext(), getString(R.string.sp_auto_login), true);
            } else {
                SharePreferenceUtil.put(getActivity().getApplicationContext(), getString(R.string.sp_auto_login), false);
            }
        }

        //检查更新
        if(mPsUpdate==preference){
            Beta.checkUpgrade();
//            Toast.makeText(getActivity(), "正在检查更新...", Toast.LENGTH_SHORT).show();
        }

        //关于
        if (mPsHelp == preference) {
            startActivity(new Intent(getActivity(), AboutActivity.class));
            getActivity().overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
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
                    SharePreferenceUtil.put(getActivity().getApplicationContext(), "skin", "");
                    SharePreferenceUtil.put(getActivity().getApplicationContext(), "skin_cn", "安卓蓝");
                    break;
                case 1:
                    SkinCompatManager.getInstance().loadSkin("green", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
                    SharePreferenceUtil.put(getActivity().getApplicationContext(), "skin", "green");
                    SharePreferenceUtil.put(getActivity().getApplicationContext(), "skin_cn", "酷安绿");
                    break;
                case 2:
                    SkinCompatManager.getInstance().loadSkin("blue", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
                    SharePreferenceUtil.put(getActivity().getApplicationContext(), "skin", "blue");
                    SharePreferenceUtil.put(getActivity().getApplicationContext(), "skin_cn", "知乎蓝");
                    break;
                default:
                    break;
            }
        }
        return false;
    }

}
