package io.pawlowska.network.functions;

public class TanH implements ActivationFunction {

    private final double ALPHA;

    public TanH(double alpha) {
        this.ALPHA = alpha;
    }

    /* The limit of the tanh function as x approaches +infinity is 1 and as x approaches -infinity equals -1 */
    public double calculate(double x) {

        double y = (Math.exp(ALPHA * x) - Math.exp(-ALPHA * x)) / (Math.exp(ALPHA * x) + Math.exp(-ALPHA * x));

        if (Double.isNaN(y)) {
            if (x > 0) return 1;
            else return -1;
        }

        return y;
    }

    public double calculateDerivative(double x) {

        return (4 * ALPHA * Math.exp(-ALPHA * 2 * x) / Math.pow(Math.exp(-ALPHA * 2 * x) + 1, 2));
    }
}