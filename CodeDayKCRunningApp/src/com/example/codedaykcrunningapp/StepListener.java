package com.example.codedaykcrunningapp;

/**
 * Interface implemented by classes that can handle notifications about steps.
 * These classes can be passed to StepDetector.
 * @author Levente Bagi
 */
public interface StepListener {
    public void onStep();
    public void passValue();
}