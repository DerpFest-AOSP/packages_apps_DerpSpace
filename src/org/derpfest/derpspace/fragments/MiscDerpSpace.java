/*
 * Copyright (C) 2017 AospExtended ROM Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.derpfest.derpspace.fragments;

import android.app.ActivityManagerNative;
import android.content.Context;
import android.content.ContentResolver;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.RemoteException;
import android.os.ServiceManager;
import androidx.preference.Preference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;
import android.provider.Settings;
import android.provider.DeviceConfig;
import android.util.Log;
import android.view.WindowManagerGlobal;
import android.view.IWindowManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.Locale;
import android.text.TextUtils;
import android.view.View;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.internal.util.derp.derpUtils;
import com.android.settings.Utils;

public class MiscDerpSpace extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String KEY_STATUS_BAR_LOGO = "status_bar_logo";
    private static final String LOCATION_INDICATOR_PREF_KEY = "enable_location_privacy_indicator";

    private SwitchPreference mShowDerpLogo;
    private SwitchPreference mLocationIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.misc_derpspace);

        final ContentResolver resolver = getActivity().getContentResolver();
        final PreferenceScreen prefSet = getPreferenceScreen();

	    mShowDerpLogo = (SwitchPreference) findPreference(KEY_STATUS_BAR_LOGO);
        mShowDerpLogo.setChecked((Settings.System.getInt(resolver,
             Settings.System.STATUS_BAR_LOGO, 0) == 1));
        mShowDerpLogo.setOnPreferenceChangeListener(this);

        mLocationIndicator = (SwitchPreference) findPreference(LOCATION_INDICATOR_PREF_KEY);
        mLocationIndicator.setChecked((Settings.Secure.getIntForUser(resolver,
                Settings.Secure.ENABLE_LOCATION_PRIVACY_INDICATOR,
                shouldShowLocationIndicator() ? 1 : 0, UserHandle.USER_CURRENT) == 1));
    }

    @Override
    public int getMetricsCategory() {
        return MetricsEvent.DERP;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        final ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mShowDerpLogo) {
            boolean value = (Boolean) objValue;
            Settings.System.putInt(resolver,
                    Settings.System.STATUS_BAR_LOGO, value ? 1 : 0);
            return true;
        } else if (preference == mLocationIndicator) {
            boolean value = (Boolean) objValue;
            Settings.Secure.putIntForUser(resolver,
                    Settings.Secure.ENABLE_LOCATION_PRIVACY_INDICATOR, value ? 1 : 0,
                    UserHandle.USER_CURRENT);
            derpUtils.showSystemUiRestartDialog(getActivity());
            return true;
        }
        return false;
    }

    private static boolean shouldShowLocationIndicator() {
        return DeviceConfig.getBoolean(DeviceConfig.NAMESPACE_PRIVACY,
            "location_indicators_enabled", false);
    }

}
