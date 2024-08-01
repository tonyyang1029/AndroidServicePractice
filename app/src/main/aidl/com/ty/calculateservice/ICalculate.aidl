// ICalculate.aidl
package com.ty.calculateservice;

// Declare any non-default types here with import statements

interface ICalculate {
    int add(int a, int b);
    int minus(int a, int b);
    int multiply(int a, int b);
    int divide(int a, int b);
}