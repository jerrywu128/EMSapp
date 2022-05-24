package com.honestmc.ems.Tools;

import android.app.Activity;
import android.app.ProgressDialog;

import com.honestmc.ems.R;

public class EMSProgressDialog {
    private static ProgressDialog mDialog = null;

    public static void showProgressDialog(Activity activity, String text) {

        closeProgressDialog();
        mDialog = new CustomProgressDialog(activity, R.style.CustomDialog,text);
        mDialog.show();
    }

    public static void showProgressDialog(Activity activity, int stringID) {
        closeProgressDialog();
        mDialog = new CustomProgressDialog(activity, R.style.CustomDialog,activity.getString(stringID));
        mDialog.show();
    }

    public static void closeProgressDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            try {
                mDialog.dismiss();
            } catch (Exception e) {

            } finally {
                mDialog = null;
            }
        }
    }

}
