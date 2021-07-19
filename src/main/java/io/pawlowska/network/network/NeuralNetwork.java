package io.pawlowska.network.network;

import io.pawlowska.network.network.utils.ConnectionsNumerator;
import io.pawlowska.network.network.utils.DecisionComparator;
import io.pawlowska.network.network.utils.ErrorCalculator;
import io.pawlowska.network.data.DataSet;
import io.pawlowska.network.data.Record;
import io.pawlowska.network.exceptions.NoSuchLayerException;
import io.pawlowska.network.functions.ActivationFunction;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
public class NeuralNetwork {

    private final Layer inputLayer;
    private final List<Layer> layers;
    private final Layer outputLayer;
    private final DataSet dataSet;
    private final ConnectionsNumerator numerator;
    private final ErrorCalculator calculator;

    public NeuralNetwork(NeuralNetworkBuilder builder) {

        inputLayer = builder.getInputLayer();
        layers = builder.getLayers();
        outputLayer = builder.getOutputLayer();
        dataSet = builder.getDataSet();
        numerator = new ConnectionsNumerator(layers);
        calculator = new ErrorCalculator();

        enableNeuronActivation(builder.getActivationFunction());
        connectNetwork();
    }

    public static NeuralNetworkBuilder builder() {
        return new NeuralNetworkBuilder();
    }

    private void enableNeuronActivation(ActivationFunction function) {

        layers.stream()
                .flatMap(Collection::stream)
                .forEach(neuron -> neuron.setActivationFunction(function));
    }

    private void connectNetwork() {

        Layer layer = inputLayer;
        Layer nextLayer;

        while (layer != outputLayer) {

            nextLayer = getLayerAfter(layer);
            connectLayers(layer, nextLayer);
            layer = nextLayer;
        }
    }

    private void connectLayers(Layer layerFrom, Layer layerTo) {

        layerFrom.forEach(neuron -> neuron.connectWithNextLayer(layerTo));
    }

    public Layer getLayerBefore(Layer layer) {

        if (layer == inputLayer) {
            throw new NoSuchLayerException("Given layer is an input layer");
        } else {
            int index = layers.indexOf(layer) - 1;
            return layers.get(index);
        }
    }

    public Layer getLayerAfter(Layer layer) {

        if (layer == outputLayer) {
            throw new NoSuchLayerException("Given layer is an output layer");
        } else {
            int index = layers.indexOf(layer) + 1;
            return layers.get(index);
        }
    }

    public double[] predict(double[] data) {

        activateInputLayer(data);
        activateHiddenAndOutputLayers();
        return getOutputDecision();
    }

    private void activateInputLayer(double[] data) {

        int i = 0;
        for (Neuron neuron : inputLayer) {
            neuron.setSignal(data[i++]);
        }
    }

    private void activateHiddenAndOutputLayers() {

        Layer layer = inputLayer;

        do {
            layer = getLayerAfter(layer);
            layer.forEach(Neuron::activate);

        } while (hasLayerAfter(layer));
    }

    public boolean hasLayerAfter(Layer layer) {
        return layers.indexOf(layer) < layers.size() - 1;
    }

    private double[] getOutputDecision() {

        double[] outputDecision = new double[outputLayer.size()];
        int i = 0;

        for (Neuron neuron : outputLayer) {
            outputDecision[i++] = neuron.getSignal();
        }

        return outputDecision;
    }

    public double calculateAccuracyForTrainingSet() {
        return calculateAccuracy(dataSet.getTrainingSet());
    }

    public double calculateAccuracyForValidatingSet() {
        return calculateAccuracy(dataSet.getValidatingSet());
    }

    private double calculateAccuracy(Record[] dataSet) {

        double correctDecision = Arrays.stream(dataSet)
                .mapToDouble(this::compareRecordMaskWithNetworkDecision)
                .sum();

        return correctDecision / dataSet.length;
    }

    private int compareRecordMaskWithNetworkDecision(Record record) {

        int[] expectedDecision = record.getMask();
        double[] outputDecision = predict(record.getData());
        return DecisionComparator.compare(expectedDecision, outputDecision);
    }

    public double calculateErrorForTrainingSet() {
        return calculateError(dataSet.getTrainingSet());
    }

    public double calculateErrorForValidatingSet() {
        return calculateError(dataSet.getValidatingSet());
    }

    private double calculateError(Record[] dataSet) {

        double error = Arrays.stream(dataSet)
                .mapToDouble(this::calculateAverageErrorBetweenMaskAndNetworkDecision)
                .sum();

        return error / dataSet.length;
    }

    private double calculateAverageErrorBetweenMaskAndNetworkDecision(Record record) {
        int[] expectedDecision = record.getMask();
        double[] outputDecision = predict(record.getData());
        return calculator.calculateError(expectedDecision, outputDecision);
    }

    public boolean isHiddenLayer(Layer layer) {
        return layer != inputLayer && layer != outputLayer;
    }

    public int getConnectionsAmount() {

        int connectionsAmount = 1;

        for (int i = 0; i < layers.size() - 1; i++) {
            connectionsAmount += layers.get(i).size() * layers.get(i + 1).size();
        }

        return connectionsAmount;
    }

    public void fixConnectionsWeights(List<Double> weights) {

        int i = 0;
        Layer layer = inputLayer;
        Layer layerAfter;

        do {
            layerAfter = getLayerAfter(layer);

            for (Neuron neuron : layer) {
                for (Neuron neuronTo : layerAfter) {
                    neuron.fixOutputConnectionWithNeuron(neuronTo, weights.get(i++));
                }
            }

            layer = layerAfter;

        } while (hasLayerAfter(layer));
    }

    public List<Integer> getNeuronConnectionsNumbers(Neuron neuron) {
        return numerator.getNeuronConnectionsNumbers(neuron);
    }
}