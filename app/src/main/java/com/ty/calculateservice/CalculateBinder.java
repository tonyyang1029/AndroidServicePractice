package com.ty.calculateservice;

import android.os.Binder;

public class CalculateBinder extends Binder {
    public int add(int a, int b) {
        return a + b;
    }

    public int minus(int a, int b) {
        return a - b;
    }

    public int multiply(int a, int b ) {
        return a * b;
    }

    public int divide(int a, int b ) {
        return a / b;
    }
}
