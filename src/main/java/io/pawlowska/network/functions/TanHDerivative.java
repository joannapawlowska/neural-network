package io.pawlowska.network.functions;

public class TanHDerivative implements ActivationFunctionDerivative {

    private final double ALPHA;

    public TanHDerivative(double alpha) {
        this.ALPHA = alpha;
    }

    public double calculate(double x) {

        return (4 * ALPHA * Math.exp(-ALPHA * 2 * x) / Math.pow(Math.exp(-ALPHA * 2 * x) + 1, 2));
    }
}