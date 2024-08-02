package com.ty.androidservicepractice;

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

public class P7_BindCalculateServiceLibActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnBind;
    private Button mBtnUnbind;
    private Button mBtnAdd;
    private Button mBtnMinus;
    private Button mBtnMultiply;
    private Button mBtnDivide;
    //
    private TextView mInfo;
    //
    private ICalculate mService;
    private IResultListener.Stub mResultListener;
    //
    private ServiceConnection mConnection;
    private IBinder.DeathRecipient mDeath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_p7_bind_calculate_service_lib);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //
        initWidgets();
        initServiceComponents();
        setButtonsEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //
        unbind();
        mResultListener = null;
        mConnection = null;
        mDeath = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.p7_btn_bind) {
            bind();
        } else if (v.getId() == R.id.p7_btn_unbind) {
            unbind();
        } else if (v.getId() == R.id.p7_btn_add) {
            try {
                mService.add(8, 13);
            } catch (RemoteException e) {
                rebind();
            }
        } else if (v.getId() == R.id.p7_btn_minus) {
            try {
                mService.minus(21, 8);
            } catch (RemoteException e) {
                rebind();
            }
        } else if (v.getId() == R.id.p7_btn_multiply) {
            try {
                mService.multiply(12, 5);
            } catch (RemoteException e) {
                rebind();
            }
        } else if (v.getId() == R.id.p7_btn_divide) {
            try {
                mService.divide(54, 9);
            } catch (RemoteException e) {
                rebind();
            }
        }
    }

    private void bind() {
        if (mService == null) {
            bindService(new Intent(this, CalculateService.class), mConnection, Service.BIND_AUTO_CREATE);
        }
    }

    private void rebind() {
        setButtonsEnabled(false);
        //
        if (mService != null) {
            try {
                mService.unregisterResultListener(mResultListener);
            } catch (RemoteException e) {
                mInfo.append("It's failed to unregister listener \n");
            }
            mService.asBinder().unlinkToDeath(mDeath, 0);
            mService = null;
        }
        //
        bind();
    }

    private void unbind() {
        if (mService != null) {
            setButtonsEnabled(false);
            //
            try {
                mService.unregisterResultListener(mResultListener);
            } catch (RemoteException e) {
                mInfo.append("It's failed to unregister listener \n");
            }
            mService.asBinder().unlinkToDeath(mDeath, 0);
            mService = null;
            //
            unbindService(mConnection);
        }
    }

    private void initWidgets() {
        mBtnBind = findViewById(R.id.p7_btn_bind);
        mBtnBind.setOnClickListener(this);
        mBtnUnbind = findViewById(R.id.p7_btn_unbind);
        mBtnUnbind.setOnClickListener(this);
        mBtnAdd = findViewById(R.id.p7_btn_add);
        mBtnAdd.setOnClickListener(this);
        mBtnMinus = findViewById(R.id.p7_btn_minus);
        mBtnMinus.setOnClickListener(this);
        mBtnMultiply = findViewById(R.id.p7_btn_multiply);
        mBtnMultiply.setOnClickListener(this);
        mBtnDivide = findViewById(R.id.p7_btn_divide);
        mBtnDivide.setOnClickListener(this);
        //
        mInfo = findViewById(R.id.p7_tv_info);
        mInfo.setText("");
    }

    private void setButtonsEnabled(boolean enabled) {
        mBtnAdd.setEnabled(enabled);
        mBtnMinus.setEnabled(enabled);
        mBtnMultiply.setEnabled(enabled);
        mBtnDivide.setEnabled(enabled);
    }

    private void initServiceComponents() {
        mResultListener = new IResultListener.Stub() {
            @Override
            public void onAdd(int result) throws RemoteException {
                mInfo.append(mBtnAdd.getText() + " = " + result + "\n");
            }

            @Override
            public void onMinus(int result) throws RemoteException {
                mInfo.append(mBtnMinus.getText() + " = " + result + "\n");
            }

            @Override
            public void onMultiply(int result) throws RemoteException {
                mInfo.append(mBtnMultiply.getText() + " = " + result + "\n");
            }

            @Override
            public void onDivide(int result) throws RemoteException {
                mInfo.append(mBtnDivide.getText() + " = " + result + "\n");
            }
        };
        //
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mInfo.setText("Service is connected \n");
                mService = ICalculate.Stub.asInterface(service);
                try {
                    mService.registerResultListener(mResultListener);
                    mService.asBinder().linkToDeath(mDeath, 0);
                } catch (RemoteException e) {
                    mInfo.append("It's failed to link death recipient to service \n");
                    rebind();
                    return;
                }
                setButtonsEnabled(true);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mInfo.append("Service is disconnected \n");
                rebind();
            }
        };
        //
        mDeath = new IBinder.DeathRecipient() {
            @Override
            public void binderDied() {
                mInfo.append("Binder is died \n");
                rebind();
            }
        };
    }
}