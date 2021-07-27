package io.pawlowska.network.functions;

public interface ActivationFunction {

    double calculate(double x);
    double calculateDerivative(double x);
    ActivationFunction copy();
}