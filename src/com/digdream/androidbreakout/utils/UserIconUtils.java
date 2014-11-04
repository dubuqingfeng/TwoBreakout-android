package com.digdream.androidbreakout.utils;


import com.digdream.androidbreakout.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class UserIconUtils {
    private static final String TAG = "UserIconUtil";

    public static final int USER_ICON_INVALID = 0;

    private static final int USER_ICONS[] = {
            USER_ICON_INVALID,
            R.drawable.share_user_icon_1,
            R.drawable.share_user_icon_2,
            R.drawable.share_user_icon_3,
            R.drawable.share_user_icon_4,
            R.drawable.share_user_icon_5,
            R.drawable.share_user_icon_6,
            R.drawable.share_user_icon_7,
            R.drawable.share_user_icon_8,
    };

    public static final int USER_ICON_COUNT = USER_ICONS.length - 1;
    public static final int DEFAULT_USER_ICON = 1;

    private static final int ICON_INDEX_MIN = 1;
    private static final int ICON_INDEX_MAX = USER_ICONS.length - 1;

    public static Drawable getIcon(Context context, int index) {
        if (index < ICON_INDEX_MIN || index > ICON_INDEX_MAX) {
            Log.d(TAG, "Warning: icon index [" + index + "] is invalid, use default icon: " + DEFAULT_USER_ICON);
            index = DEFAULT_USER_ICON;
        }

        return context.getResources().getDrawable(USER_ICONS[index]);
    }
}
