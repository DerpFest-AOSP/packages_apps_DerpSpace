/*
 * Copyright (C) 2018 AospExtended ROM Project
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

import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.provider.Settings;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;

public class GeneralTweaks extends SettingsPreferenceFragment implements OnPreferenceChangeListener {


    private static final String ALERT_SLIDER_PREF = "alert_slider_notifications";
    private static final String KEY_SPOOF = "use_photos_spoof";
    private static final String SYS_SPOOF = "persist.sys.photo";

    private Preference mAlertSlider;
    private SwitchPreference mSpoof;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.general_tweaks);

        final ContentResolver resolver = getActivity().getContentResolver();
        final PreferenceScreen prefScreen = getPreferenceScreen();
        final Resources res = getResources();

        mAlertSlider = (Preference) prefScreen.findPreference(ALERT_SLIDER_PREF);
        boolean mAlertSliderAvailable = res.getBoolean(
                com.android.internal.R.bool.config_hasAlertSlider);
        if (!mAlertSliderAvailable)
            prefScreen.removePreference(mAlertSlider);

        final String useSpoof = SystemProperties.get(SYS_SPOOF, "1");
        mSpoof = (SwitchPreference) findPreference(KEY_SPOOF);
        mSpoof.setChecked("1".equals(useSpoof));
        mSpoof.setOnPreferenceChangeListener(this);
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
        if (preference == mSpoof) {
            String value = ((Boolean) objValue) ? "1" : "0";
            SystemProperties.set(SYS_SPOOF, value);
            Toast.makeText(getActivity(),
                    (R.string.photos_spoof_toast),
                    Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
}
