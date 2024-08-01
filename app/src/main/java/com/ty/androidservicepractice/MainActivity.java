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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnP1;
    private Button mBtnP2;
    private Button mBtnP3;
    private Button mBtnP4;
    private Button mBtnP5;
    private Button mBtnP6;

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

        mBtnP1 = findViewById(R.id.main_btn_p1);
        mBtnP1.setOnClickListener(this);
        mBtnP2 = findViewById(R.id.main_btn_p2);
        mBtnP2.setOnClickListener(this);
        mBtnP3 = findViewById(R.id.main_btn_p3);
        mBtnP3.setOnClickListener(this);
        mBtnP4 = findViewById(R.id.main_btn_p4);
        mBtnP4.setOnClickListener(this);
        mBtnP5 = findViewById(R.id.main_btn_p5);
        mBtnP5.setOnClickListener(this);
        mBtnP6 = findViewById(R.id.main_btn_p6);
        mBtnP6.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.main_btn_p1) {
            startActivity(new Intent(this, P1_StartHelloServiceActivity.class));
        } else if (v.getId() == R.id.main_btn_p2) {
            startActivity(new Intent(this, P2_BindHelloServiceActivity.class));
        } else if (v.getId() == R.id.main_btn_p3) {
            startActivity(new Intent(this, P3_BindHello2ServiceActivity.class));
        } else if (v.getId() == R.id.main_btn_p4) {
            startActivity(new Intent(this, P4_BindCalculateServiceActivity.class));
        } else if (v.getId() == R.id.main_btn_p5) {
            startActivity(new Intent(this, P5_BindCalculate2ServiceActivity.class));
        } else if (v.getId() == R.id.main_btn_p6) {
            startActivity(new Intent(this, P6_BindAsyncCalculateServiceActivity.class));
        }
    }
}