package io.pawlowska.network.utils;

import io.pawlowska.network.functions.ErrorFunction;
import io.pawlowska.network.functions.MeanSquareError;

public class ErrorCalculator {

    private static ErrorFunction errorFunction = new MeanSquareError();

    public static double calculateError(int[] expectedDecision, double[] outputDecision) {
        return errorFunction.calculate(expectedDecision, outputDecision);
    }
}