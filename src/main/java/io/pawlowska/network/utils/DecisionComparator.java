package io.pawlowska.network.utils;

public class DecisionComparator {

    private final static double EPSILON = 0.000001d;

    public static int compare(int[] expectedDecision, double[] outputDecision) {

        int numberOfCorrectOutputs = 0;

        for (int i = 0; i < outputDecision.length; i++) {

            if (Math.abs(expectedDecision[i] - outputDecision[i]) < EPSILON) {
                numberOfCorrectOutputs++;
            }
        }

        return numberOfCorrectOutputs == outputDecision.length ? 1 : 0;
    }
}
