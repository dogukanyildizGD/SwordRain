package com.sr.swordrain;

import android.graphics.Bitmap;
import android.view.View;

public class ScreenShot {

    public static Bitmap takscreenshot(View v){
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return b;
    }

    public static Bitmap takescreenshotOfRootView(View v){
        return takscreenshot(v.getRootView());
    }

}
