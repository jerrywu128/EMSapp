package com.honestmc.ems.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.WindowManager;

import java.lang.reflect.Method;

public abstract class BasePresenter {
    private final String tag = "BasePresenter";
    protected Activity activity;
    public BasePresenter(Activity activity) {
        this.activity = activity;
    }
    public void initCfg(){

        // never sleep when run this activity
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // do not display menu bar
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void setCurrentApp(){

    }


    public void redirectToAnotherActivity(Context context, Class<?> cls){

        Intent intent = new Intent();

        intent.setClass(context, cls);
        context.startActivity(intent);
    }

    public void finishActivity() {
        activity.finish();
    }

    public void isAppBackground() {

    }

    public void submitAppInfo() {

    }

    public void removeActivity(){

    }

    public void showOptionIcon(Menu menu) {
        //display icon
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "onMenuOpened...unable to set icons for overflow menu", e);
                }
            }
        }
    }
}
