package io.pawlowska.network.training;

import io.pawlowska.network.architecture.*;
import io.pawlowska.network.data.Record;
import lombok.Builder;

public class BackPropagation implements Training {

    @Builder.Default
    private int epochs = 1000;

    @Builder.Default
    private double ETA = 0.01;

    private NeuralNetwork network;
    private Record[] trainSet;

    @Builder
    public BackPropagation(int epochs, double ETA) {
        this.epochs = epochs;
        this.ETA = ETA;
    }

    public void perform(NeuralNetwork network, Record[] trainSet) {

        while (epochs-- != 0) {

            for (Record record : trainSet) {

                derivativeActivateOutputLayer(record);
                derivativeActivateHiddenLayers();
                fixConnectionWeights();
            }
        }
    }

    private void derivativeActivateOutputLayer(Record record) {

        int i = 0;
        int[] expectedDecision = record.getMask();
        double[] outputDecision = network.predict(record.getData());

        for (Neuron neuron : network.getOutputLayer().getNeurons()) {

            neuron.derivativeActivate();
            neuron.setGradient((expectedDecision[i] - outputDecision[i++]) * neuron.getSignal());
        }
    }

    private void derivativeActivateHiddenLayers() {

        Layer layer = network.getLayerBefore(network.getOutputLayer());

        while (network.isHiddenLayer(layer)) {

            activateHiddenLayer(layer);
            layer = network.getLayerBefore(layer);
        }
    }

    private void activateHiddenLayer(Layer layer) {

        for (Neuron neuron : layer.getNeurons()) {

            int gradient = 0;

            for (Connection connection : neuron.getOutputConnections()) {
                gradient += connection.getNeuronTo().getGradient() * connection.getWeight();
            }

            neuron.derivativeActivate();
            neuron.setGradient(gradient * neuron.getSignal());
        }
    }

    private void fixConnectionWeights() {

        Layer layer = network.getLayerBefore(network.getOutputLayer());

        while (network.isHiddenLayer(layer)) {

            fixConnectionWeights(layer);
            layer = network.getLayerBefore(layer);
        }
    }

    private void fixConnectionWeights(Layer layer) {

        for (Neuron neuron : layer.getNeurons()) {

            for (Connection connection : neuron.getInputConnections()) {
                connection.setWeight(connection.getWeight() + ETA * neuron.getGradient() * connection.getNeuronFrom().getSignal());
            }
        }
    }
}