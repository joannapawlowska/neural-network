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
            throw new IllegalArgumentException("Number of type of feature categories must be equal output layer size");
        }

        if (builder.dataSet.getFeatureNumber() != builder.inputLayerSize) {
            throw new IllegalArgumentException("Number of feature columns must be equal input layer size");
        }

        if (builder.training == null) {
            throw new NullPointerException("Training can not be null");
        }

        if (builder.activationFunction == null) {
            throw new NullPointerException("Activation function can not be null");
        }
    }
}