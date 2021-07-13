package io.pawlowska.network.utils;

public class DecisionComparator {

    private final static double EPSILON = 0.00001;

    public static int compare(int[] expectedDecision, double[] outputDecision) {

        int numberOfCorrectOutputs = 0;

        for (int i = 0; i < outputDecision.length; i++) {

            if (expectedDecision[i] == Math.round(outputDecision[i])) {
                numberOfCorrectOutputs++;
            }
        }

        return numberOfCorrectOutputs == outputDecision.length ? 1 : 0;
    }
}
