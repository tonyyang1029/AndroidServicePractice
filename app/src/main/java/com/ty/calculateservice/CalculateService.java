package com.ty.calculateservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class CalculateService extends Service {
    private CalculateBinder mBinder;

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new CalculateBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {
        showToast("I'm bound with client.");
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinder = null;
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}