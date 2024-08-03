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

public class P2_BindHelloServiceActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnBind;
    private Button mBtnUnbind;
    private Button mBtnSayHi;
    private Button mBtnSayBye;
    private TextView mTvInfo;
    private ServiceConnection mServiceConnection;
    private HelloService.HelloBinder mBinder;
    private IBinder.DeathRecipient mDeath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_p2_bind_hello);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mBtnBind = findViewById(R.id.p2_btn_bind);
        mBtnBind.setOnClickListener(this);
        mBtnUnbind = findViewById(R.id.p2_btn_unbind);
        mBtnUnbind.setOnClickListener(this);
        mBtnSayHi = findViewById(R.id.p2_btn_say_hi);
        mBtnSayHi.setOnClickListener(this);
        mBtnSayBye = findViewById(R.id.p2_btn_say_bye);
        mBtnSayBye.setOnClickListener(this);
        //
        mTvInfo = findViewById(R.id.p2_tv_info);
        mTvInfo.setText("");
        //
        mDeath = new IBinder.DeathRecipient() {
            @Override
            public void binderDied() {
                mTvInfo.append("Service is dead \n");
                unbind();
            }
        };
        //
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mTvInfo.append("Service is connected \n");
                mBinder = (HelloService.HelloBinder) service;
                mBinder.linkToDeath(mDeath, 0);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mTvInfo.append("Service is disconnected \n");
                unbind();
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbind();
        mServiceConnection = null;
        mDeath = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.p2_btn_bind) {
            bind();
        } else if (v.getId() == R.id.p2_btn_unbind) {
            unbind();
        } else if (v.getId() == R.id.p2_btn_say_hi) {
            if (mBinder != null) {
                mBinder.sayHi();
            }
        } else if (v.getId() == R.id.p2_btn_say_bye) {
            if (mBinder != null) {
                mBinder.sayBye();
            }
        }
    }

    private void bind() {
        if (mBinder == null) {
            bindService(new Intent(this, HelloService.class), mServiceConnection, Service.BIND_AUTO_CREATE);
        }
    }

    private void unbind() {
        if (mBinder != null) {
            mBinder.unlinkToDeath(mDeath, 0);
            unbindService(mServiceConnection);
            mBinder = null;
        }
    }
}