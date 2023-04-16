/*
 * Copyright (C) 2023 The LeafOS Project
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
import androidx.preference.Preference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;

import com.android.internal.logging.nano.MetricsProto.MetricsEvent;

import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.search.SearchIndexable;


@SearchIndexable
public class QSPanelSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String TAG = "QSPanelSettings";
    private static final String[] qsCustPreferences = { "qs_tile_shape",
            "qqs_num_columns", "qqs_num_columns_landscape",
            "qs_num_columns", "qs_num_columns_landscape" };

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.qs_panel_settings);

        PreferenceScreen preferenceScreen = getPreferenceScreen();

        boolean qsStyleRound = Settings.Secure.getIntForUser(getContext().getContentResolver(),
                Settings.Secure.QS_STYLE_ROUND, 1, UserHandle.USER_CURRENT) == 1;

        if (!qsStyleRound) {
            for (String key : qsCustPreferences) {
                Preference preference = preferenceScreen.findPreference(key);
                if (preference != null) {
                    preference.setEnabled(false);
                }
            }
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsEvent.DERP;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        return false;
    }

    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider(R.xml.qs_panel_settings);
}
