// ICalculate.aidl
package com.ty.calculateservicelib;

// Declare any non-default types here with import statements
import com.ty.calculateservicelib.IResultListener;

interface ICalculate {
    void registerResultListener(IResultListener listener);
    void unregisterResultListener(IResultListener listener);
    //
    void add(int a, int b);
    void minus(int a, int b);
    void multiply(int a, int b);
    void divide(int a, int b);
    //
    String getName();
}