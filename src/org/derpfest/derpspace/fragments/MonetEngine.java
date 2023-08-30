/*
 * Copyright (C) 2021 The Android Open Source Project
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

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.content.DialogInterface;
import android.view.ViewGroup;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;

import android.content.res.Resources;
import android.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import java.util.ArrayList;
import java.util.List;
import com.skydoves.colorpickerview.ActionMode;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorListener;
import com.skydoves.colorpickerview.listeners.ColorPickerViewListener;
import com.skydoves.colorpickerview.preference.ColorPickerPreferenceManager;
import org.derpfest.derpspace.utils.WallpaperUtils;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.Preference.OnPreferenceClickListener;

public class MonetEngine extends SettingsPreferenceFragment implements
        OnPreferenceClickListener {

    final static String TAG = "MonetEngine";

    private static final String PREF_CHROMA_FACTOR ="monet_engine_chroma_factor";
    private static final String PREF_LUMINANCE_FACTOR ="monet_engine_luminance_factor";
    private static final String PREF_TINT_BACKGROUND ="monet_engine_tint_background";
    private static final String PREF_CUSTOM_COLOR ="monet_engine_custom_color";
    private static final String PREF_COLOR_OVERRIDE ="monet_engine_color_override";
    private static final String PREF_CUSTOM_BGCOLOR ="monet_engine_custom_bgcolor";
    private static final String PREF_BGCOLOR_OVERRIDE ="monet_engine_bgcolor_override";
    private static final String PREF_WALLPAPER_COLOR ="monet_engine_wall_color_select";

    private Preference mWallColorPicker;
    public ColorPickerView mColorPickerView;
    public int mUserColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.monet_engine);

        final PreferenceScreen prefScreen = getPreferenceScreen();
        final Context mContext = getActivity().getApplicationContext();
        final Resources res = mContext.getResources();
        final ContentResolver resolver = mContext.getContentResolver();
        mColorPickerView = new ColorPickerView.Builder(getContext()).setActionMode(ActionMode.LAST).setPaletteDrawable(WallpaperUtils.getWall(getContext(), false)).setPreferenceName("GhostColorPicker").setColorListener(new ColorListener() {
            public void onColorSelected(int color, boolean fromUser) {
                if (fromUser) {
                    mUserColor = color;
                    Settings.Secure.putInt(resolver, PREF_COLOR_OVERRIDE, mUserColor);
                    ColorPickerPreferenceManager.getInstance(getContext()).saveColorPickerData(mColorPickerView);
                }
            }
        }).build();
        mWallColorPicker = (Preference) prefScreen.findPreference(PREF_WALLPAPER_COLOR);
        mWallColorPicker.setOnPreferenceClickListener(this);
        mWallColorPicker.setEnabled(!WallpaperUtils.isLiveWall(getContext()));
        mWallColorPicker.setSummary(WallpaperUtils.isLiveWall(getContext()) ? R.string.not_available : R.string.monet_engine_wallpaper_color_select_summary);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == mWallColorPicker) {
            ColorPickerPreferenceManager.getInstance(getContext()).restoreColorPickerData(mColorPickerView);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.Theme_WallpaperColorPicker_Dialog_Alert);
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                  dialogInterface.dismiss();
              }
           });
            builder.setView(mColorPickerView);
            builder.setCancelable(true);
            builder.setTitle(R.string.monet_engine_wallpaper_color_select_summary);
            builder.show();
            return true;
        } else {
            return true;
        }
    }

    public static void reset(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();
        Settings.Secure.putIntForUser(resolver,
                PREF_CHROMA_FACTOR, 100, UserHandle.USER_CURRENT);
        Settings.Secure.putIntForUser(resolver,
                PREF_LUMINANCE_FACTOR, 100, UserHandle.USER_CURRENT);
        Settings.Secure.putIntForUser(resolver,
                PREF_TINT_BACKGROUND, 0, UserHandle.USER_CURRENT);
        Settings.Secure.putIntForUser(resolver,
                PREF_CUSTOM_COLOR, 0, UserHandle.USER_CURRENT);
        Settings.Secure.putIntForUser(resolver,
                PREF_COLOR_OVERRIDE, 0xFF1b6ef3, UserHandle.USER_CURRENT);
        Settings.Secure.putIntForUser(resolver,
                PREF_CUSTOM_BGCOLOR, 0, UserHandle.USER_CURRENT);
        Settings.Secure.putIntForUser(resolver,
                PREF_BGCOLOR_OVERRIDE, 0xFF1b6ef3, UserHandle.USER_CURRENT);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.DERP;
    }
}
