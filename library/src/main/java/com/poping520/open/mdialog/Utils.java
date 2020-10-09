package com.poping520.open.mdialog;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * @author WangKZ
 * @version 1.0.0
 * create on 2018/7/16 17:52
 */
class Utils {

    static float dp2px(Context context, float dpValue) {
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, dm);
    }

    @ColorInt
    static int getDarkerColor(@ColorInt int color, float level) {
        final float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[1] = hsv[1] + level;
        hsv[2] = hsv[2] - level;
        return Color.HSVToColor(hsv);
    }

    @ColorInt
    public static int getBrighterColor(@ColorInt int color) {
        final float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[1] = hsv[1] - 0.1f;
        hsv[2] = hsv[2] + 0.1f;
        return Color.HSVToColor(hsv);
    }

    static float getBrightness(@ColorInt int color) {
        return 0.299f * ((color >> 16) & 0xFF) +
                0.587f * ((color >> 8) & 0xFF) +
                0.114f * (color & 0xFF);
    }
}
