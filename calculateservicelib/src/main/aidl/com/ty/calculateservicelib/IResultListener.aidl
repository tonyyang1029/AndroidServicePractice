// IResultListener.aidl
package com.ty.calculateservicelib;

// Declare any non-default types here with import statements

interface IResultListener {
    void onAdd(int result);
    void onMinus(int result);
    void onMultiply(int result);
    void onDivide(int result);
}