package io.pawlowska.network.functions;

public class Unipolar implements ActivationFunction{

    private final double ALPHA;

    public Unipolar(double alpha) {
        this.ALPHA = alpha;
    }

    /* The limit of the unipolar function as x approaches +infinity is 1 and as x approaches -infinity equals 0 */
    public double calculate(double x) {

        double y = 1 / (1 + Math.exp(-ALPHA * x));

        if (Double.isNaN(y)) {
            if (x > 0) return 1;
            else return 0;
        }

        return y;
    }

    public double calculateDerivative(double x) {

        return (ALPHA * Math.exp(-ALPHA * x)) / ((1 + Math.exp(-ALPHA * x)) * (1 + Math.exp(-ALPHA * x)));
    }
}
