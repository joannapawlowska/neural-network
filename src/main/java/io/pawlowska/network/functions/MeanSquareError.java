package io.pawlowska.network.functions;

public class MeanSquareError implements ErrorFunction {

    public double calculate(int[] x, double[] y) {

        double average = 0;

        for (int i = 0; i < y.length; i++) {
            average += Math.pow(y[i] - x[i], 2);
        }

        return average / y.length;
    }
}