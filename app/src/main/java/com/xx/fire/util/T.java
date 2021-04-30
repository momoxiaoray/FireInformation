package com.xx.fire.util;

import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.xx.fire.R;

import java.lang.ref.WeakReference;

/**
 * Create by MeansDai(xiaocheng.ok@qq.com)
 * Date: 17/3/17 下午3:19
 * Description: //please input description
 * FIXME Toast Util
 * ${tags}
 */
public class T {
    private static Toast toast = null;
    private static WeakReference<Application> app;

    private T() {
    }

    public static void init(Application application) {
        app = new WeakReference<>(application);
    }

    public static void showToast(int resID) {
        if (app == null) return;
        showToast(app.get(), Toast.LENGTH_SHORT,
                resID);
    }

    public static void showToast(String text) {
        if (app == null) return;
        showToast(app.get(), Toast.LENGTH_SHORT, text);
    }

    //
    public static void showToast(Context ctx, int resID) {
        if (app == null) return;
        showToast(ctx, Toast.LENGTH_SHORT, resID);
    }

    //
    public static void showToast(Context ctx, String text) {
        if (app == null) return;
        showToast(ctx, Toast.LENGTH_SHORT, text);
    }

    public static void showLongToast(Context ctx, int resID) {
        if (app == null) return;
        showToast(ctx, Toast.LENGTH_LONG, resID);
    }

    public static void showLongToast(int resID) {
        if (app == null) return;
        showToast(app.get(), Toast.LENGTH_LONG, resID);
    }

//    public static void showLongToast(Context ctx, String text) {
//        showToast(ctx, Toast.LENGTH_LONG, text);
//    }

    public static void showLongToast(String text) {
        showToast(app.get(), Toast.LENGTH_LONG, text);
    }

    public static void show5sToast(String text) {
        showToast(app.get(), 15000, text);
    }

    private static void showToast(Context ctx, int duration, int resID) {
        showToast(ctx, duration, ctx.getString(resID));
    }

    /**
     * 自定义背景图
     *
     * @param ctx
     * @param resID
     * @return
     */
    public static Toast showToastImage(Context ctx, int resID) {
        final Toast toast = Toast.makeText(ctx, "", Toast.LENGTH_SHORT);
        View mNextView = toast.getView();
        if (mNextView != null)
            mNextView.setBackgroundResource(resID);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        return toast;
    }

    private static void showToast(final Context ctx, final int duration,
                                  final String text) {

        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        toast = Toast.makeText(ctx, text, duration);
        View view = RelativeLayout.inflate(ctx, R.layout.view_toast_layout, null);
        TextView mNextView = (TextView) view.findViewById(R.id.toast_name);
        toast.setView(view);
        mNextView.setText(text);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        if (Looper.myLooper() == null) {
            Looper.loop();
        }
    }

    public static void cancel() {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }
}
