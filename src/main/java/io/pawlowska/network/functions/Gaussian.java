package io.pawlowska.network.functions;

public class Gaussian implements ActivationFunction {

    private final double ALPHA;

    public Gaussian(double alpha) {
        this.ALPHA = alpha;
    }

    /* The limit of the unipolar function as x approaches +infinity or -infinity equals 0 */
    public double calculate(double x) {

        double y = Math.exp(-(ALPHA * ALPHA * x * x));

        if (Double.isNaN(y)) {
            return 0;
        }

        return y;
    }

    public double calculateDerivative(double x) {

        return -2 * ALPHA * x * Math.exp(-ALPHA * ALPHA * x * x);
    }
}