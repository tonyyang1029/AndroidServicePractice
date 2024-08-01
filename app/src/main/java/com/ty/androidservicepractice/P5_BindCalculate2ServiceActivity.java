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

import com.ty.calculateservice.Calculate2Service;
import com.ty.calculateservice.ICalculate;

public class P5_BindCalculate2ServiceActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnBind;
    private Button mBtnUnbind;
    private Button mBtnAdd;
    private Button mBtnMinus;
    private Button mBtnMultiply;
    private Button mBtnDivide;
    //
    private TextView mTvInfo;
    //
    private ICalculate mICalcuate;
    private ServiceConnection mSrvConnection;
    private IBinder.DeathRecipient mSrvDeath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_p5_bind_calculate2_service);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mBtnBind = findViewById(R.id.p5_btn_bind);
        mBtnBind.setOnClickListener(this);
        mBtnUnbind = findViewById(R.id.p5_btn_unbind);
        mBtnUnbind.setOnClickListener(this);
        mBtnAdd = findViewById(R.id.p5_btn_add);
        mBtnAdd.setOnClickListener(this);
        mBtnMinus = findViewById(R.id.p5_btn_minus);
        mBtnMinus.setOnClickListener(this);
        mBtnMultiply = findViewById(R.id.p5_btn_multiply);
        mBtnMultiply.setOnClickListener(this);
        mBtnDivide = findViewById(R.id.p5_btn_divide);
        mBtnDivide.setOnClickListener(this);
        //
        mTvInfo = findViewById(R.id.p5_tv_info);
        //
        mSrvDeath = new IBinder.DeathRecipient() {
            @Override
            public void binderDied() {
                mTvInfo.append("Binder is dead \n");
                if (mICalcuate != null) {
                    mICalcuate.asBinder().unlinkToDeath(mSrvDeath, 0);
                    mICalcuate = null;
                }
                bind();
            }
        };
        //
        mSrvConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mTvInfo.append("Service is connected \n");
                mICalcuate = ICalculate.Stub.asInterface(service);
                try {
                    mICalcuate.asBinder().linkToDeath(mSrvDeath, 0);
                } catch (RemoteException e) {
                    mTvInfo.append("Binder is dead \n");
                    if (mICalcuate != null) {
                        mICalcuate.asBinder().unlinkToDeath(mSrvDeath, 0);
                        mICalcuate = null;
                    }
                    bind();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mTvInfo.append("Service is disconnected \n");
                if (mICalcuate != null) {
                    mICalcuate.asBinder().unlinkToDeath(mSrvDeath, 0);
                    mICalcuate = null;
                }
                bind();
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
        if (v.getId() == R.id.p5_btn_bind) {
            bind();
        } else if (v.getId() == R.id.p5_btn_unbind) {
            unbind();
        } else if (v.getId() == R.id.p5_btn_add) {
            if (mICalcuate != null) {
                try {
                    mTvInfo.append(mBtnAdd.getText() + " = " + mICalcuate.add(1, 2) + " \n");
                } catch (RemoteException e) {
                    mTvInfo.append("Add is failed \n");
                }
            }
        } else if (v.getId() == R.id.p5_btn_minus) {
            if (mICalcuate != null) {
                try {
                    mTvInfo.append(mBtnMinus.getText() + " = " + mICalcuate.minus(5, 2) + " \n");
                } catch (RemoteException e) {
                    mTvInfo.append("Minus is failed \n");
                }
            }
        } else if (v.getId() == R.id.p5_btn_multiply) {
            if (mICalcuate != null) {
                try {
                    mTvInfo.append(mBtnMultiply.getText() + " = " + mICalcuate.multiply(3, 8) + " \n");
                } catch (RemoteException e) {
                    mTvInfo.append("Multiply is failed \n");
                }
            }
        } else if (v.getId() == R.id.p5_btn_divide) {
            if (mICalcuate != null) {
                try {
                    mTvInfo.append(mBtnDivide.getText() + " = " + mICalcuate.divide(16, 4) + " \n");
                } catch (RemoteException e) {
                    mTvInfo.append("Divide is failed \n");
                }
            }
        }
    }

    private void bind() {
        if (mICalcuate == null) {
            bindService(new Intent(this, Calculate2Service.class), mSrvConnection, Service.BIND_AUTO_CREATE);
        }
    }

    private void unbind() {
        if (mICalcuate != null) {
            unbindService(mSrvConnection);
            mICalcuate.asBinder().unlinkToDeath(mSrvDeath, 0);
            mICalcuate = null;
        }
    }
}