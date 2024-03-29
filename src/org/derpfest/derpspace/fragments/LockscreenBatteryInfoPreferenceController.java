/*
 * Copyright (C) 2022 Nameless-AOSP Project
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

import com.android.settings.core.BasePreferenceController;

public class LockscreenBatteryInfoPreferenceController extends BasePreferenceController {

    private static final String KEY_BATTERY_INFO = "lockscreen_battery_info";

    public LockscreenBatteryInfoPreferenceController(Context context) {
        super(context, KEY_BATTERY_INFO);
    }

    @Override
    public int getAvailabilityStatus() {
        return mContext.getResources().getBoolean(com.android.internal.R.bool.config_enable_charging_info)
                ? AVAILABLE : UNSUPPORTED_ON_DEVICE;
    }

    @Override
    public String getPreferenceKey() {
        return KEY_BATTERY_INFO;
    }
}
