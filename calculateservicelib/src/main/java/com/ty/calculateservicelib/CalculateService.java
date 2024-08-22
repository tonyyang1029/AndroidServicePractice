package com.ty.calculateservicelib;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CalculateService extends Service {
    private final int MSG_ADD      = 1;
    private final int MSG_MINUS    = 2;
    private final int MSG_MULTIPLY = 3;
    private final int MSG_DIVIDE   = 4;
    //
    private ArrayList<IResultListener> mListeners;
    private CalculateHandler mHandler;
    private ICalculate.Stub mBinder;
    //
    private String mServiceName;

    public CalculateService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //
        mListeners = new ArrayList<>();
        //
        mHandler = createCalculateHandler();
        //
        mBinder = new ICalculate.Stub() {
            @Override
            public void registerResultListener(IResultListener listener) throws RemoteException {
                mListeners.add(listener);
            }

            @Override
            public void unregisterResultListener(IResultListener listener) throws RemoteException {
                mListeners.remove(listener);
            }

            @Override
            public void add(int a, int b) throws RemoteException {
                Message msg = mHandler.obtainMessage(MSG_ADD);
                msg.arg1 = a;
                msg.arg2 = b;
                mHandler.sendMessage(msg);
            }

            @Override
            public void minus(int a, int b) throws RemoteException {
                Message msg = mHandler.obtainMessage(MSG_MINUS);
                msg.arg1 = a;
                msg.arg2 = b;
                mHandler.sendMessage(msg);
            }

            @Override
            public void multiply(int a, int b) throws RemoteException {
                Message msg = mHandler.obtainMessage(MSG_MULTIPLY);
                msg.arg1 = a;
                msg.arg2 = b;
                mHandler.sendMessage(msg);
            }

            @Override
            public void divide(int a, int b) throws RemoteException {
                Message msg = mHandler.obtainMessage(MSG_DIVIDE);
                msg.arg1 = a;
                msg.arg2 = b;
                mHandler.sendMessage(msg);
            }

            @Override
            public String getName() throws RemoteException {
                return mServiceName;
            }
        };
    }

    @Override
    public IBinder onBind(Intent intent) {
        String client_name = null;
        String service_name = null;

        if (intent.hasExtra("client_name")) {
            client_name = intent.getStringExtra("client_name");
        }
        if (intent.hasExtra("service_name")) {
            service_name = intent.getStringExtra("service_name");
        }
        if (mServiceName == null) {
            mServiceName = service_name;
        }

        if (client_name != null && service_name != null) {
            Toast.makeText(this, client_name + " is bound to " + service_name, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "A client is bound to service", Toast.LENGTH_LONG).show();
        }

        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListeners = null;
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        mBinder = null;
        mServiceName = null;
    }

    private CalculateHandler createCalculateHandler() {
        return new CalculateHandler(this.getMainLooper());
    }

    private class CalculateHandler extends Handler {
        public CalculateHandler(@NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MSG_ADD:
                    for (IResultListener listener : mListeners) {
                        try {
                            listener.onAdd(msg.arg1 + msg.arg2);
                        } catch (RemoteException e) {
                            mListeners.remove(listener);
                        }
                    }
                    break;

                case MSG_MINUS:
                    for (IResultListener listener : mListeners) {
                        try {
                            listener.onMinus(msg.arg1 - msg.arg2);
                        } catch (RemoteException e) {
                            mListeners.remove(listener);
                        }
                    }
                    break;

                case MSG_MULTIPLY:
                    for (IResultListener listener : mListeners) {
                        try {
                            listener.onMultiply(msg.arg1 * msg.arg2);
                        } catch (RemoteException e) {
                            mListeners.remove(listener);
                        }
                    }
                    break;

                case MSG_DIVIDE:
                    for (IResultListener listener : mListeners) {
                        try {
                            listener.onDivide(msg.arg1 / msg.arg2);
                        } catch (RemoteException e) {
                            mListeners.remove(listener);
                        }
                    }
                    break;
            }
        }
    }
}