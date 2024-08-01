package com.ty.androidservicepractice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.FileDescriptor;

public class Hello2Service extends Service {
    private Hello2Binder mBinder;

    public Hello2Service() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new Hello2Binder(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    class Hello2Binder extends Binder {
        private Context mCtxt;

        public Hello2Binder(Context ctxt) {
            mCtxt = ctxt;
        }

        public void showToast(String msg) {
            Toast.makeText(mCtxt, msg, Toast.LENGTH_LONG).show();
        }

        @Override
        public void linkToDeath(@NonNull DeathRecipient recipient, int flags) {
            super.linkToDeath(recipient, flags);
            Toast.makeText(mCtxt, "A death recipient is linked.", Toast.LENGTH_SHORT).show();
        }
    }
}