package com.ty.clientpro;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ty.calculateservicelib.CalculateService;
import com.ty.calculateservicelib.ICalculate;
import com.ty.calculateservicelib.IResultListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnBind;
    private Button mBtnUnbind;
    private Button mBtnAdd;
    private Button mBtnMinus;
    private Button mBtnMultiply;
    private Button mBtnDivide;
    //
    private TextView mTvInfo;
    //
    private IBinder.DeathRecipient mDeath;
    private ServiceConnection mConnection;
    private ICalculate mService;
    //
    private IResultListener.Stub mResultListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        setBtnEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //TODO
        // destroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_bind) {
            bind();
        } else if (v.getId() == R.id.btn_unbind) {
            unbind();
        } else if (v.getId() == R.id.btn_add) {
            try {
                mService.add(1, 1);
            } catch (RemoteException e) {
                mTvInfo.append("Cannot communicate with service for add calculation \n");
            }
        } else if (v.getId() == R.id.btn_minus) {
            try {
                mService.minus(2, 2);
            } catch (RemoteException e) {
                mTvInfo.append("Cannot communicate with service for minus calculation \n");
            }
        } else if (v.getId() == R.id.btn_multiply) {
            try {
                mService.multiply(3, 3);
            } catch (RemoteException e) {
                mTvInfo.append("Cannot communicate with service for multiplication calculation \n");
            }
        } else if (v.getId() == R.id.btn_divide) {
            try {
                mService.divide(4, 4);
            } catch (RemoteException e) {
                mTvInfo.append("Cannot communicate with service for division calculation \n");
            }
        }
    }

    private void init() {
        mBtnBind = findViewById(R.id.btn_bind);
        mBtnBind.setOnClickListener(this);
        //
        mBtnUnbind = findViewById(R.id.btn_unbind);
        mBtnUnbind.setOnClickListener(this);
        //
        mBtnAdd = findViewById(R.id.btn_add);
        mBtnAdd.setOnClickListener(this);
        //
        mBtnMinus = findViewById(R.id.btn_minus);
        mBtnMinus.setOnClickListener(this);
        //
        mBtnMultiply = findViewById(R.id.btn_multiply);
        mBtnMultiply.setOnClickListener(this);
        //
        mBtnDivide = findViewById(R.id.btn_divide);
        mBtnDivide.setOnClickListener(this);
        //
        mTvInfo = findViewById(R.id.tv_info);
        //
        mDeath = new IBinder.DeathRecipient() {
            @Override
            public void binderDied() {
                mTvInfo.append("Binder is dead \n");
                unbind();
            }
        };
        //
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mTvInfo.append("Service is connected \n");
                //
                mService = ICalculate.Stub.asInterface(service);
                try {
                    mService.registerResultListener(mResultListener);
                    mService.asBinder().linkToDeath(mDeath, 0);
                } catch (RemoteException e) {
                    mTvInfo.append("Cannot communicate with service for service connection \n");
                    unbind();
                    return;
                }
                //
                setBtnEnabled(true);
                try {
                    mTvInfo.append("Service Name: " + mService.getName() + "\n");
                } catch (RemoteException e) {
                    mTvInfo.append("Cannot communicate with service for getting service name \n");
                    unbind();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mTvInfo.append("Service is disconnected \n");
                unbind();
            }
        };
        //
        mResultListener = new IResultListener.Stub() {
            @Override
            public void onAdd(int result) throws RemoteException {
                mTvInfo.append(mBtnAdd.getText() + " = " + result + "\n");
            }

            @Override
            public void onMinus(int result) throws RemoteException {
                mTvInfo.append(mBtnMinus.getText() + " = " + result + "\n");
            }

            @Override
            public void onMultiply(int result) throws RemoteException {
                mTvInfo.append(mBtnMultiply.getText() + " = " + result + "\n");
            }

            @Override
            public void onDivide(int result) throws RemoteException {
                mTvInfo.append(mBtnDivide.getText() + " = " + result + "\n");
            }
        };
    }

    private void destroy() {
        unbind();
        mConnection = null;
        mDeath = null;
        mResultListener = null;
    }

    private void setBtnEnabled(boolean enabled) {
        mBtnAdd.setEnabled(enabled);
        mBtnMinus.setEnabled(enabled);
        mBtnMultiply.setEnabled(enabled);
        mBtnDivide.setEnabled(enabled);
    }

    private void bind() {
        if (mService == null) {
            Intent intent = new Intent(this, CalculateService.class);
            intent.putExtra("client_name", "ClientPro");
            intent.putExtra("service_name", "Service1");
            bindService(intent, mConnection, Service.BIND_AUTO_CREATE);
        }
    }

    private void unbind() {
        if (mService != null) {
            setBtnEnabled(false);
            //
            try {
                mService.unregisterResultListener(mResultListener);
            } catch (RemoteException e) {
                mTvInfo.append("Cannot communicate with service for unregistering result listener \n");
            }
            mService.asBinder().unlinkToDeath(mDeath, 0);
            mService = null;
            //
            unbindService(mConnection);
        }
    }
}