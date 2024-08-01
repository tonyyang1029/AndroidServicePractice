package com.ty.asynccalculateservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import com.ty.calculateservice.ICalculate;

import java.util.ArrayList;

public class AsyncCalculateService extends Service {
    final static int MSG_ADD = 1;
    final static int MSG_MINUS = 2;
    final static int MSG_MULTIPLY = 3;
    final static int MSG_DIVIDE = 4;
    //
    private CalculateHandler mHandler;
    private ArrayList<ICalculateResultListener> mListeners;
    //
    private IAsyncCalculate.Stub mBinder;

    public AsyncCalculateService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //
        mHandler = new CalculateHandler();
        //
        mListeners = new ArrayList<>();
        //
        mBinder = new IAsyncCalculate.Stub() {
            @Override
            public void registerOnCalculateResult(ICalculateResultListener listener) throws RemoteException {
                mListeners.add(listener);
            }

            @Override
            public void unregisterOnCalculateResult(ICalculateResultListener listener) throws RemoteException {
                mListeners.remove(listener);
            }

            @Override
            public void add(int a, int b) throws RemoteException {
                Message msg = mHandler.obtainMessage(MSG_ADD, a, b);
                mHandler.sendMessage(msg);
            }

            @Override
            public void minus(int a, int b) throws RemoteException {
                Message msg = mHandler.obtainMessage(MSG_MINUS, a, b);
                mHandler.sendMessage(msg);
            }

            @Override
            public void multiply(int a, int b) throws RemoteException {
                Message msg = mHandler.obtainMessage(MSG_MULTIPLY, a, b);
                mHandler.sendMessage(msg);
            }

            @Override
            public void divide(int a, int b) throws RemoteException {
                Message msg = mHandler.obtainMessage(MSG_DIVIDE, a, b);
                mHandler.sendMessage(msg);
            }
        };
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private class CalculateHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            //
            switch (msg.what) {
                case MSG_ADD:
                    for (ICalculateResultListener listener : mListeners) {
                        try {
                            listener.onAdd(msg.arg1 + msg.arg2);
                        } catch (RemoteException e) {
                            mListeners.remove(listener);
                        }
                    }
                    break;

                case MSG_MINUS:
                    for (ICalculateResultListener listener : mListeners) {
                        try {
                            listener.onMinus(msg.arg1 - msg.arg2);
                        } catch (RemoteException e) {
                            mListeners.remove(listener);
                        }
                    }
                    break;

                case MSG_MULTIPLY:
                    for (ICalculateResultListener listener : mListeners) {
                        try {
                            listener.onMultiply(msg.arg1 * msg.arg2);
                        } catch (RemoteException e) {
                            mListeners.remove(listener);
                        }
                    }
                    break;

                case MSG_DIVIDE:
                    for (ICalculateResultListener listener : mListeners) {
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