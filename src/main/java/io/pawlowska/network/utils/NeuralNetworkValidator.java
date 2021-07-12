package io.pawlowska.network.utils;

import io.pawlowska.network.architecture.NeuralNetwork;
import io.pawlowska.network.exceptions.MissingLayerException;

public class NeuralNetworkValidator {

    public static void validate(NeuralNetwork.NeuralNetworkBuilder builder) {

        if (builder.inputLayerSize == 0 || builder.outputLayerSize == 0) {
            throw new MissingLayerException("Neither input nor output layer size can be 0");
        }

        if (builder.dataSet == null) {
            throw new NullPointerException("Data set can not be empty");
        }

        if (builder.dataSet.getMaskByCategory().size() != builder.outputLayerSize) {
            throw new IllegalArgumentException("Output layer size must be equal number of data categories");
        }

        if (builder.dataSet.getFeatureNumber() != builder.inputLayerSize) {
            throw new IllegalArgumentException("Input layer size must be equal number of feature columns");
        }

        if (builder.activationFunction == null) {
            throw new NullPointerException("Activation function can not be null");
        }
    }
}