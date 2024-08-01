// ICalculateResult.aidl
package com.ty.asynccalculateservice;

// Declare any non-default types here with import statements

interface ICalculateResultListener {
    void onAdd(int result);
    void onMinus(int result);
    void onMultiply(int result);
    void onDivide(int result);
}