package com.ty.androidservicepractice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class HelloService extends Service {
    private HelloBinder mBinder;

    public HelloService() {
        mBinder = new HelloBinder(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "I'm about to start!", Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "I'm about to stop!", Toast.LENGTH_LONG).show();
        mBinder = null;
    }

    class HelloBinder extends Binder {
        private final Context mCtxt;

        public HelloBinder(Context ctxt) {
            mCtxt = ctxt;
        }

        void sayHi() {
            Toast.makeText(mCtxt, "Hi, buddy!", Toast.LENGTH_LONG).show();
        }

        void sayBye() {
            Toast.makeText(mCtxt, "Bye, buddy!", Toast.LENGTH_LONG).show();
        }
    }
}