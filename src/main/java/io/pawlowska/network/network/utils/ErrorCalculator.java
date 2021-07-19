package io.pawlowska.network.network.utils;

import io.pawlowska.network.functions.ErrorFunction;
import io.pawlowska.network.functions.MeanSquareError;

public class ErrorCalculator {

    private ErrorFunction errorFunction;

    public ErrorCalculator(){
        errorFunction = new MeanSquareError();
    }

    public double calculateError(int[] expectedDecision, double[] outputDecision) {
        return errorFunction.calculate(expectedDecision, outputDecision);
    }
}