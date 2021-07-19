package io.pawlowska.network.network.utils;

public class DecisionComparator {

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
