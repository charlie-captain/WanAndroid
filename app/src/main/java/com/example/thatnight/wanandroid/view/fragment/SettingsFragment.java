package com.example.thatnight.wanandroid.view.fragment;


import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.utils.SharePreferenceUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment {

    private SwitchPreference mSpDayLight;
    private EditTextPreference mEtpUserName;
    private EditTextPreference mEtpUserPwd;
    private PreferenceScreen mPsHelp;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_settings);
        init();
    }

    private void init() {
        mSpDayLight = (SwitchPreference) findPreference(getString(R.string.pref_day_light));
        mEtpUserName = (EditTextPreference) findPreference(getString(R.string.pref_user_name));
        mEtpUserPwd = (EditTextPreference) findPreference(getString(R.string.pref_user_password));
        mPsHelp = (PreferenceScreen) findPreference(getString(R.string.pref_help));
    }

    @Override
    public void onResume() {
        super.onResume();
        String userName = SharePreferenceUtil.get(getActivity(), "account", "").toString();
        String userPassword = SharePreferenceUtil.get(getActivity(), "password", "").toString();
        mEtpUserName.setTitle(userName);
        mEtpUserName.setText(userName);
        String length = "";
        for (int i=0; i < userPassword.length(); i++) {
            length+="*";
        }
        mEtpUserPwd.setTitle(length);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }


}
