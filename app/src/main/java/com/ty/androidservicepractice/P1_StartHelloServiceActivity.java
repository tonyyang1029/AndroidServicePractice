package com.ty.androidservicepractice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class P1_StartHelloServiceActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnStart;
    private Button mBtnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_p1_start_hello);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mBtnStart = findViewById(R.id.p1_btn_start);
        mBtnStart.setOnClickListener(this);
        mBtnStop = findViewById(R.id.p1_btn_stop);
        mBtnStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.p1_btn_start) {
            startService(new Intent(this, HelloService.class));
        } else if (v.getId() == R.id.p1_btn_stop) {
            stopService(new Intent(this, HelloService.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, HelloService.class));
    }
}