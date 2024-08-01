// IAsyncCalculate.aidl
package com.ty.asynccalculateservice;

// Declare any non-default types here with import statements
import com.ty.asynccalculateservice.ICalculateResultListener;


interface IAsyncCalculate {
    void registerOnCalculateResult(ICalculateResultListener listener);
    void unregisterOnCalculateResult(ICalculateResultListener listener);
    void add(int a, int b);
    void minus(int a, int b);
    void multiply(int a, int b);
    void divide(int a, int b);
}