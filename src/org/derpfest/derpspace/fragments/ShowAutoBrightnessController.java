/*
 * Copyright (C) 2018 The PixelExperience Project
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

public class ShowAutoBrightnessController extends BasePreferenceController {

    public static final String KEY = "qs_show_auto_brightness";

    private Context mContext;

    public ShowAutoBrightnessController(Context context, String key) {
        super(context, key);

        mContext = context;
    }

    public ShowAutoBrightnessController(Context context) {
        this(context, KEY);

        mContext = context;
    }

    @Override
    public int getAvailabilityStatus() {
        boolean exists = mContext.getResources().getBoolean(com.android.internal.R.bool.config_automatic_brightness_available);
        return (exists ? AVAILABLE : UNSUPPORTED_ON_DEVICE);
    }

}
