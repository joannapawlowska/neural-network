package io.pawlowska.network.functions;

public class BipolarDerivative implements ActivationFunctionDerivative {

    private final double ALPHA;

    public BipolarDerivative(double alpha) {
        this.ALPHA = alpha;
    }

    public double calculate(double x) {

        return (2 * ALPHA * Math.exp(-ALPHA * x)) / (Math.pow(1 + Math.exp(-ALPHA * x), 2));
    }
}