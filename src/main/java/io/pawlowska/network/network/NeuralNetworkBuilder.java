package io.pawlowska.network.network;

import io.pawlowska.network.data.DataSet;
import io.pawlowska.network.exceptions.IllegalNetworkArchitectureException;
import io.pawlowska.network.exceptions.MissingActivationFunctionException;
import io.pawlowska.network.exceptions.MissingDataSetException;
import io.pawlowska.network.exceptions.MissingLayerException;
import io.pawlowska.network.functions.ActivationFunction;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class NeuralNetworkBuilder {

    private Layer inputLayer;
    private Layer outputLayer;
    private List<Layer> layers;
    private ActivationFunction activationFunction;
    private DataSet dataSet;

    public NeuralNetworkBuilder() {
        layers = new ArrayList<>();
    }

    public NeuralNetworkBuilder inputLayer(Layer inputLayer) {
        this.inputLayer = inputLayer;
        layers.add(0, inputLayer);
        return this;
    }

    public NeuralNetworkBuilder outputLayer(Layer outputLayer) {
        this.outputLayer = outputLayer;
        layers.add(outputLayer);
        return this;
    }

    public NeuralNetworkBuilder hiddenLayer(Layer hiddenLayer) {

        int index;

        if(layers.size() == 0){
            index = 0;
        }else if(outputLayer != null){
            index = layers.size() - 1;
        }else{
            index = layers.size();
        }

        layers.add(index, hiddenLayer);
        return this;
    }

    public NeuralNetworkBuilder activationFunction(ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
        return this;
    }

    public NeuralNetworkBuilder dataSet(DataSet dataSet) {
        this.dataSet = dataSet;
        return this;
    }

    public NeuralNetwork build() {
        validate();
        return new NeuralNetwork(this);
    }

    private void validate() {

        validateInputLayer();
        validateOutputLayer();
        validateActivationFunction();
        validateDataSet();
        validateArchitectureAndDataSetCompatibility();
    }

    private void validateInputLayer() {

        if (inputLayer == null || inputLayer.size() == 0) {
            throw new MissingLayerException("Input layer size can not be 0");
        }
    }

    private void validateOutputLayer() {

        if (outputLayer == null || outputLayer.size() == 0) {
            throw new MissingLayerException("Output layer size can not be 0");
        }
    }

    private void validateActivationFunction() {

        if (activationFunction == null) {
            throw new MissingActivationFunctionException("Activation function can not be null");
        }
    }

    private void validateDataSet() {

        if (dataSet == null) {
            throw new MissingDataSetException("Data set can not be null");
        }
    }

    private void validateArchitectureAndDataSetCompatibility() {

        if (dataSet.getMaskByCategory().size() != outputLayer.size()) {
            throw new IllegalNetworkArchitectureException("Output layer size must be equal number of data categories");
        }

        if (dataSet.getFeatureNumber() != inputLayer.size()) {
            throw new IllegalNetworkArchitectureException("Input layer size must be equal number of feature columns");
        }
    }
}
