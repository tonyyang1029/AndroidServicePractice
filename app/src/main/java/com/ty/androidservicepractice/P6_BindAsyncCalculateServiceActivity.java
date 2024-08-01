package com.ty.androidservicepractice;

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

import com.ty.asynccalculateservice.AsyncCalculateService;
import com.ty.asynccalculateservice.IAsyncCalculate;
import com.ty.asynccalculateservice.ICalculateResultListener;

public class P6_BindAsyncCalculateServiceActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnBind;
    private Button mBtnUnbind;
    private Button mBtnAdd;
    private Button mBtnMinus;
    private Button mBtnMultiply;
    private Button mBtnDivide;
    //
    private TextView mInfo;
    //
    private IAsyncCalculate mService;
    //
    private ServiceConnection mServiceConnection;
    private IBinder.DeathRecipient mDeathRecipient;
    //
    private ICalculateResultListener mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_p6_bind_calculate_async_service);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mBtnBind = findViewById(R.id.p6_btn_bind);
        mBtnBind.setOnClickListener(this);
        mBtnUnbind = findViewById(R.id.p6_btn_unbind);
        mBtnUnbind.setOnClickListener(this);
        mBtnAdd = findViewById(R.id.p6_btn_add);
        mBtnAdd.setOnClickListener(this);
        mBtnAdd.setEnabled(false);
        mBtnMinus = findViewById(R.id.p6_btn_minus);
        mBtnMinus.setOnClickListener(this);
        mBtnMinus.setEnabled(false);
        mBtnMultiply = findViewById(R.id.p6_btn_multiply);
        mBtnMultiply.setOnClickListener(this);
        mBtnMultiply.setEnabled(false);
        mBtnDivide = findViewById(R.id.p6_btn_divide);
        mBtnDivide.setOnClickListener(this);
        mBtnDivide.setEnabled(false);
        //
        mInfo = findViewById(R.id.p6_tv_info);
        //
        mDeathRecipient = new IBinder.DeathRecipient() {
            @Override
            public void binderDied() {
                rebind();
            }
        };
        //
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mInfo.append("Service is connected \n");
                //
                mService = IAsyncCalculate.Stub.asInterface(service);
                try {
                    mService.asBinder().linkToDeath(mDeathRecipient, 0);
                    mService.registerOnCalculateResult(mResult);
                } catch (RemoteException e) {
                    rebind();
                    return;
                }
                //
                mBtnAdd.setEnabled(true);
                mBtnMinus.setEnabled(true);
                mBtnMultiply.setEnabled(true);
                mBtnDivide.setEnabled(true);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mInfo.append("Service is disconnected \n");
                rebind();
            }
        };
        //
        mResult = new ICalculateResultListener.Stub() {
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbind();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.p6_btn_bind) {
            bind();
        } else if (v.getId() == R.id.p6_btn_unbind) {
            unbind();
        } else if (v.getId() == R.id.p6_btn_add) {
            try {
                mService.add(4, 7);
            } catch (RemoteException e) {
                rebind();
            }
        } else if (v.getId() == R.id.p6_btn_minus) {
            try {
                mService.minus(12, 5);
            } catch (RemoteException e) {
                rebind();
            }
        } else if (v.getId() == R.id.p6_btn_multiply) {
            try {
                mService.multiply(6, 9);
            } catch (RemoteException e) {
                rebind();
            }
        } else if (v.getId() == R.id.p6_btn_divide) {
            try {
                mService.divide(27, 3);
            } catch (RemoteException e) {
                rebind();
            }
        }
    }

    private void bind() {
        if (mService == null) {
            bindService(new Intent(this, AsyncCalculateService.class), mServiceConnection, BIND_AUTO_CREATE);
        }
    }

    private void rebind() {
        mInfo.append("Binder is dead and attempt to re-bind ... \n");
        if (mService != null) {
            try {
                mService.unregisterOnCalculateResult(mResult);
            } catch (RemoteException e) {
                mInfo.append("Cannot unregister listener \n");
            }
            mService.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mService = null;
        }
        bind();
    }

    private void unbind() {
        if (mService != null) {
            mBtnAdd.setEnabled(false);
            mBtnMinus.setEnabled(false);
            mBtnMultiply.setEnabled(false);
            mBtnDivide.setEnabled(false);
            //
            try {
                mService.unregisterOnCalculateResult(mResult);
            } catch (RemoteException e) {
                mInfo.append("Cannot unregister listener \n");
            }
            mService.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mService = null;
            unbindService(mServiceConnection);
        }
    }
}