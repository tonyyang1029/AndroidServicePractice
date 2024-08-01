package com.ty.androidservicepractice;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ty.calculateservice.CalculateBinder;
import com.ty.calculateservice.CalculateService;

public class P4_BindCalculateServiceActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnBind;
    private Button mBtnUnbind;
    private Button mBtnAdd;
    private Button mBtnMinus;
    private Button mBtnMultiply;
    private Button mBtnDivide;
    //
    private TextView mTvInfo;
    //
    private ServiceConnection mSrvConnection;
    private IBinder.DeathRecipient mSrvDeath;
    private CalculateBinder mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_p4_bind_calculate_service);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mBtnBind = findViewById(R.id.p4_btn_bind);
        mBtnBind.setOnClickListener(this);
        mBtnUnbind = findViewById(R.id.p4_btn_unbind);
        mBtnUnbind.setOnClickListener(this);
        mBtnAdd = findViewById(R.id.p4_btn_add);
        mBtnAdd.setOnClickListener(this);
        mBtnMinus = findViewById(R.id.p4_btn_minus);
        mBtnMinus.setOnClickListener(this);
        mBtnMultiply = findViewById(R.id.p4_btn_multiply);
        mBtnMultiply.setOnClickListener(this);
        mBtnDivide = findViewById(R.id.p4_btn_divide);
        mBtnDivide.setOnClickListener(this);
        //
        mTvInfo = findViewById(R.id.p4_tv_info);
        //
        mSrvDeath = new IBinder.DeathRecipient() {
            @Override
            public void binderDied() {
                mTvInfo.append("Binder is dead \n");
                if (mService != null) {
                    mService.unlinkToDeath(mSrvDeath, 0);
                    mService = null;
                }
                bind();
            }
        };
        //
        mSrvConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mTvInfo.append("Service is connected \n");
                mService = (CalculateBinder) service;
                mService.linkToDeath(mSrvDeath, 0);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mTvInfo.append("Service is disconnected \n");
                if (mService != null) {
                    mService.unlinkToDeath(mSrvDeath, 0);
                    mService = null;
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
        if (v.getId() == R.id.p4_btn_bind) {
            bind();
        } else if (v.getId() == R.id.p4_btn_unbind) {
            unbind();
        } else if (v.getId() == R.id.p4_btn_add) {
            if (mService != null) {
                mTvInfo.append(mBtnAdd.getText() + " = " + mService.add(1, 2) + " \n");
            }
        } else if (v.getId() == R.id.p4_btn_minus) {
            if (mService != null) {
                mTvInfo.append(mBtnMinus.getText() + " = " + mService.minus(10, 6) + " \n");
            }
        } else if (v.getId() == R.id.p4_btn_multiply) {
            if (mService != null) {
                mTvInfo.append(mBtnMultiply.getText() + " = " + mService.multiply(3, 6) + " \n");
            }
        } else if (v.getId() == R.id.p4_btn_divide) {
            if (mService != null) {
                mTvInfo.append(mBtnDivide.getText() + " = " + mService.divide(9, 3) + " \n");
            }
        }
    }

    private void bind() {
        if (mService == null) {
            bindService(new Intent(this, CalculateService.class), mSrvConnection, Service.BIND_AUTO_CREATE);
        }
    }

    private void unbind() {
        if (mService != null) {
            unbindService(mSrvConnection);
            mService.unlinkToDeath(mSrvDeath, 0);
            mService = null;
        }
    }
}