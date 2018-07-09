package com.cgqfinal.library.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.winstar.library.R;


/**
 * ToastUtils
 */

public class ToastUtils {

    private static Context context = Utils.getContext();
    private static Toast toast;
    private static TextView tv;

    public static void show(int resId) {
        show(context.getResources().getText(resId), Toast.LENGTH_SHORT);
    }

    public static void show(int resId, int duration) {
        show(context.getResources().getText(resId), duration);
    }

    public static void show(CharSequence text) {
        show(text, Toast.LENGTH_SHORT);
    }

    public static void show(CharSequence text, int duration) {
        text = TextUtils.isEmpty(text == null ? "" : text.toString()) ? "请检查您的网络！" : text;
        if (toast == null || tv == null) {
            View layout = LayoutInflater.from(context).inflate(R.layout.toast_view, null);
            tv = layout.findViewById(R.id.toast_text);
            tv.setTextColor(context.getResources().getColor(R.color.white1));
            toast = new Toast(context);
            toast.setGravity(Gravity.BOTTOM, 0, 40);
            toast.setView(layout);
        }
        tv.setText(text);
        toast.setDuration(duration);
        toast.show();
    }

    public static void show(int resId, Object... args) {
        show(String.format(context.getResources().getString(resId), args),
                Toast.LENGTH_SHORT);
    }

    public static void show(String format, Object... args) {
        show(String.format(format, args), Toast.LENGTH_SHORT);
    }

    public static void show(int resId, int duration, Object... args) {
        show(String.format(context.getResources().getString(resId), args),
                duration);
    }

    public static void show(String format, int duration, Object... args) {
        show(String.format(format, args), duration);
    }
}
