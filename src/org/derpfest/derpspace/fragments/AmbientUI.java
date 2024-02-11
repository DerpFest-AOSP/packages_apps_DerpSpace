/*
 * Copyright (C) 2021 The PixelDust Project
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
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManagerGlobal;
import android.view.IWindowManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.Locale;
import android.text.TextUtils;
import android.view.View;

import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.search.SearchIndexable;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.settings.Utils;

import org.derpfest.support.preferences.SystemSettingListPreference;
import org.derpfest.support.colorpicker.ColorPickerPreference;

@SearchIndexable
public class AmbientUI extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String OMNI_KEYGUARD_BATTERY_BAR_COLOR_SOURCE = "sysui_keyguard_battery_bar_color_source";
    private static final String OMNI_KEYGUARD_BATTERY_BAR_CUSTOM_COLOR = "sysui_keyguard_battery_bar_custom_color";

    private SystemSettingListPreference mBarColorSource;
    private ColorPickerPreference mBarCustomColor;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.ambient_ui);

        // ambient batterybar color type
        mBarColorSource = (SystemSettingListPreference) findPreference(OMNI_KEYGUARD_BATTERY_BAR_COLOR_SOURCE);
        mBarColorSource.setValue(String.valueOf(Settings.System.getInt(
                getContentResolver(), Settings.System.OMNI_KEYGUARD_BATTERY_BAR_COLOR_SOURCE, 0)));
        mBarColorSource.setSummary(mBarColorSource.getEntry());
        mBarColorSource.setOnPreferenceChangeListener(this);

        mBarCustomColor = (ColorPickerPreference) findPreference(OMNI_KEYGUARD_BATTERY_BAR_CUSTOM_COLOR);
        mBarCustomColor.setOnPreferenceChangeListener(this);
        int batteryBarColor = Settings.System.getInt(getContentResolver(),
                Settings.System.OMNI_KEYGUARD_BATTERY_BAR_CUSTOM_COLOR, 0xFF39FF42);
        String batteryBarColorHex = String.format("#%08x", (0xFF39FF42 & batteryBarColor));
        mBarCustomColor.setNewPreviewColor(batteryBarColor);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsEvent.DERP;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
         if (preference == mBarColorSource) {
             int value = Integer.valueOf((String) objValue);
             int vIndex = mBarColorSource.findIndexOfValue((String) objValue);
             mBarColorSource.setSummary(mBarColorSource.getEntries()[vIndex]);
             Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.OMNI_KEYGUARD_BATTERY_BAR_COLOR_SOURCE, value);
            if (value == 2) {
                mBarCustomColor.setEnabled(true);
            } else {
                mBarCustomColor.setEnabled(false);
            }
            return true;
        } else if (preference == mBarCustomColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(objValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.OMNI_KEYGUARD_BATTERY_BAR_CUSTOM_COLOR, intHex);
            return true;
        }
        return false;
    }

    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider(R.xml.ambient_ui);
}
