package com.ty.calculateservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

public class Calculate2Service extends Service {
    private ICalculate.Stub mICalcuate;

    public Calculate2Service() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mICalcuate = new ICalculate.Stub() {
            @Override
            public int add(int a, int b) throws RemoteException {
                return (a + b);
            }

            @Override
            public int minus(int a, int b) throws RemoteException {
                return (a - b);
            }

            @Override
            public int multiply(int a, int b) throws RemoteException {
                return (a * b);
            }

            @Override
            public int divide(int a, int b) throws RemoteException {
                return (a / b);
            }
        };
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this, "A client is connected to service", Toast.LENGTH_SHORT).show();
        return mICalcuate;
    }
}