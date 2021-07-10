package io.pawlowska.network.functions;

public class Bipolar implements ActivationFunction {

    private final double ALPHA;

    public Bipolar(double alpha) {
        this.ALPHA = alpha;
    }

    /* The limit of the bipolar function as x approaches +infinity is 1 and as x approaches -infinity equals -1 */
    public double calculate(double x) {

        double y = (1 - Math.exp(-ALPHA * x)) / (1 + Math.exp(-ALPHA * x));

        if (Double.isNaN(y)) {
            if (x > 0) return 1;
            else return -1;
        }

        return y;
    }

    public double calculateDerivative(double x) {

        return (2 * ALPHA * Math.exp(-ALPHA * x)) / (Math.pow(1 + Math.exp(-ALPHA * x), 2));
    }
}