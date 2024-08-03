package com.ty.androidservicepractice;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class P3_BindHello2ServiceActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnBind;
    private Button mBtnUnbind;
    private Button mBtnMsg1;
    private Button mBtnMsg2;
    //
    private ServiceConnection mSrvConn;
    private Hello2Service.Hello2Binder mService;
    private IBinder.DeathRecipient mDeathRecipient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_p3_bind_hello2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mBtnBind = findViewById(R.id.p3_btn_bind);
        mBtnBind.setOnClickListener(this);
        mBtnUnbind = findViewById(R.id.p3_btn_unbind);
        mBtnUnbind.setOnClickListener(this);
        mBtnMsg1 = findViewById(R.id.p3_btn_show_msg1);
        mBtnMsg1.setOnClickListener(this);
        mBtnMsg2 = findViewById(R.id.p3_btn_show_msg2);
        mBtnMsg2.setOnClickListener(this);
        //
        mDeathRecipient = new IBinder.DeathRecipient() {
            @Override
            public void binderDied() {
                if (mService != null) {
                    mService.unlinkToDeath(mDeathRecipient, 0);
                    mService = null;
                }
                bind();
            }
        };
        //
        mSrvConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mService = (Hello2Service.Hello2Binder) service;
                mService.linkToDeath(mDeathRecipient, 0);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                if (mService != null) {
                    mService.unlinkToDeath(mDeathRecipient, 0);
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
        mSrvConn = null;
        mDeathRecipient = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.p3_btn_bind) {
            bind();
        } else if (v.getId() == R.id.p3_btn_unbind) {
            unbind();
        } else if (v.getId() == R.id.p3_btn_show_msg1) {
            if (mService != null) {
                mService.showToast("I'm Hello2 service.");
            }
        } else if (v.getId() == R.id.p3_btn_show_msg2) {
            if (mService != null) {
                mService.showToast("I'm running in the background.");
            }
        }
    }

    private void bind() {
        if (mService == null) {
            bindService(new Intent(this, Hello2Service.class), mSrvConn, Service.BIND_AUTO_CREATE);
        }
    }

    private void unbind() {
        if (mService != null) {
            mService.unlinkToDeath(mDeathRecipient, 0);
            unbindService(mSrvConn);
            mService = null;
        }
    }
}