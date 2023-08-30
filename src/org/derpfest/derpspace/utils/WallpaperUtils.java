package org.derpfest.derpspace.utils;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.ParcelFileDescriptor;

public class WallpaperUtils {
    public static Drawable getWall(Context context, boolean z) {
        WallpaperManager instance = WallpaperManager.getInstance(context);
        ParcelFileDescriptor wallpaperFile = instance.getWallpaperFile(z ? 2 : 1);
        if (wallpaperFile == null) {
            return instance.getDrawable();
        }
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFileDescriptor(wallpaperFile.getFileDescriptor()), 1080, 1080, true);
        try {
            wallpaperFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BitmapDrawable(context.getResources(), createScaledBitmap);
    }

    public static boolean isLiveWall(Context context) {
        return WallpaperManager.getInstance(context).getWallpaperInfo() != null;
    }
}
