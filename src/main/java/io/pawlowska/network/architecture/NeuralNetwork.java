package io.pawlowska.network.architecture;

import io.pawlowska.network.data.DataSet;
import io.pawlowska.network.data.Record;
import io.pawlowska.network.utils.NeuralNetworkValidator;
import io.pawlowska.network.exceptions.NoSuchLayerException;
import io.pawlowska.network.functions.ActivationFunction;
import io.pawlowska.network.training.Training;
import io.pawlowska.network.utils.DecisionComparator;
import io.pawlowska.network.utils.ErrorCalculator;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class NeuralNetwork {

    private Layer inputLayer;
    private List<Layer> layers;
    private Layer outputLayer;
    private ActivationFunction activationFunction;
    private DataSet dataSet;

    private NeuralNetwork(NeuralNetworkBuilder builder) {

        setLayers(
                builder.inputLayerSize,
                builder.hiddenLayerSizes,
                builder.outputLayerSize
        );

        activationFunction = builder.activationFunction;
        dataSet = builder.dataSet;

        connectNetwork();
    }

    private void setLayers(int inputLayerSize, List<Integer> hiddenLayerSizes, int outputLayerSize) {

        inputLayer = new Layer(inputLayerSize, activationFunction);
        outputLayer = new Layer(outputLayerSize, activationFunction);
        layers = new ArrayList<>();

        layers.add(inputLayer);

        for (Integer hiddenLayerSize : hiddenLayerSizes) {
            layers.add(new Layer(hiddenLayerSize, activationFunction));
        }

        layers.add(outputLayer);
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

        for (Neuron neuron : layerFrom.getNeurons()) {
            neuron.connectWithNextLayer(layerTo);
        }
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

    public void train(Training training) {
        training.perform(this, dataSet.getTrainingSet());
    }

    public double[] predict(double[] data) {

        activateInputLayer(data);
        activateHiddenAndOutputLayers();
        return getOutputDecision();
    }

    private void activateInputLayer(double[] data) {

        int i = 0;
        for (Neuron neuron : inputLayer.getNeurons()) {
            neuron.setSignal(data[i++]);
        }
    }

    private void activateHiddenAndOutputLayers() {

        Layer layer = inputLayer;

        do {
            layer = getLayerAfter(layer);
            for (Neuron neuron : layer.getNeurons()) {
                neuron.activate();
            }

        } while (hasLayerAfter(layer));
    }

    public boolean hasLayerAfter(Layer layer) {
        return layers.indexOf(layer) < layers.size() - 1;
    }

    private double[] getOutputDecision() {

        double[] outputDecision = new double[outputLayer.getNeurons().size()];
        int i = 0;

        for (Neuron neuron : outputLayer.getNeurons()) {
            outputDecision[i++] = neuron.getSignal();
        }

        return outputDecision;
    }

    public double calculateAccuracy(Record[] dataSet) {

        double correctDecision = 0;

        for (Record record : dataSet) {
            correctDecision += compareRecordMaskWithNetworkDecision(record);
        }

        return correctDecision / dataSet.length;
    }

    private int compareRecordMaskWithNetworkDecision(Record record) {

        int[] expectedDecision = record.getMask();
        double[] outputDecision = predict(record.getData());
        return DecisionComparator.compare(expectedDecision, outputDecision);
    }

    public double calculateError(Record[] dataSet) {

        double error = 0;

        for (Record record : dataSet) {
            error += calculateAverageErrorBetweenMaskAndNetworkDecision(record);
        }
        return error / dataSet.length;
    }

    private double calculateAverageErrorBetweenMaskAndNetworkDecision(Record record) {
        int[] expectedDecision = record.getMask();
        double[] outputDecision = predict(record.getData());
        return ErrorCalculator.calculateError(expectedDecision, outputDecision);
    }

    public boolean isHiddenLayer(Layer layer) {
        return layer != inputLayer && layer != outputLayer;
    }

    public static NeuralNetworkBuilder builder() {
        return new NeuralNetworkBuilder();
    }

    public static class NeuralNetworkBuilder {

        public int inputLayerSize;
        public int outputLayerSize;
        public List<Integer> hiddenLayerSizes;
        public ActivationFunction activationFunction;
        public DataSet dataSet;
        public Training training;

        public NeuralNetworkBuilder() {
            hiddenLayerSizes = new ArrayList<>();
        }

        public NeuralNetworkBuilder inputLayer(int size) {
            inputLayerSize = size;
            return this;
        }

        public NeuralNetworkBuilder outputLayer(int size) {
            outputLayerSize = size;
            return this;
        }

        public NeuralNetworkBuilder hiddenLayer(int size) {
            hiddenLayerSizes.add(size);
            return this;
        }

        public NeuralNetworkBuilder activationFunction(ActivationFunction activationFunction) {
            this.activationFunction = activationFunction;
            return this;
        }

        public NeuralNetworkBuilder training(Training training){
            this.training = training;
            return this;
        }

        public NeuralNetworkBuilder dataSet(DataSet dataSet){
            this.dataSet = dataSet;
            return this;
        }

        public NeuralNetwork build() {
            NeuralNetworkValidator.validate(this);
            return new NeuralNetwork(this);
        }
    }
}