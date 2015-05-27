package com.mkoi.over9000.handler;

import android.app.AlertDialog;
import android.util.Log;

import com.mkoi.over9000.LoginActivity;
import com.mkoi.over9000.R;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.api.rest.RestErrorHandler;
import org.springframework.core.NestedRuntimeException;

/**
 * @Author Bartłomiej Borucki
 */
@EBean
public class ErrorHandler implements RestErrorHandler {
    public static final String LOG_TAG = "Over9000.RestError";

    @RootContext
    LoginActivity loginActivity;

    @Override
    @UiThread
    public void onRestClientExceptionThrown(NestedRuntimeException e) {
        Log.d(LOG_TAG, "Error while logging", e);
        AlertDialog.Builder builder = new AlertDialog.Builder(loginActivity);
        builder.setIcon(android.R.drawable.ic_dialog_alert).setTitle(loginActivity.getString(R.string.errorTitle)).
                setMessage(loginActivity.getString(R.string.errorMessage)).show();
    }
}