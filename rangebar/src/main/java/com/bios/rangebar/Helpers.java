package com.bios.rangebar;

import android.content.res.Resources;
import android.util.TypedValue;

class Helpers
{
    public static float dp2px(float dp)
    {
        Resources r = Resources.getSystem();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }
}
