package io.pawlowska.network.training;

import io.pawlowska.network.architecture.Connection;
import io.pawlowska.network.architecture.Layer;
import io.pawlowska.network.architecture.Neuron;
import io.pawlowska.network.data.Record;
import lombok.Builder;


public class BackPropagation extends Training {

    private int epochs;
    private double learningRate;

    @Builder
    private BackPropagation(int epochs, double learningRate) {
        super();
        this.epochs = epochs;
        this.learningRate = learningRate;
    }

    @Override
    public void perform() {

        while (epochs-- > 0) {

            timer.start();

            for (Record record : network.getDataSet().getTrainingSet()) {

                derivativeActivateOutputLayer(record);
                derivativeActivateHiddenLayers();
                fixConnectionWeights();
            }

            timer.stop();
            collectTrainingResultOnTrainingSet();
        }
    }

    private void derivativeActivateOutputLayer(Record record) {

        int i = 0;
        int[] expectedDecision = record.getMask();
        double[] outputDecision = network.predict(record.getData());

        for (Neuron neuron : network.getOutputLayer().getNeurons()) {

            double derivativeSignal = neuron.derivativeActivate();
            neuron.setGradient((expectedDecision[i] - outputDecision[i++]) * derivativeSignal);
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

            gradient *= neuron.derivativeActivate();
            neuron.setGradient(gradient);
        }
    }

    private void fixConnectionWeights() {

        Layer layer = network.getOutputLayer();

        do {
            fixConnectionWeights(layer);
            layer = network.getLayerBefore(layer);

        } while (network.isHiddenLayer(layer));
    }

    private void fixConnectionWeights(Layer layer) {

        for (Neuron neuron : layer.getNeurons()) {

            for (Connection connection : neuron.getInputConnections()) {
                connection.setWeight(connection.getWeight() + learningRate * neuron.getGradient() * connection.getNeuronFrom().getSignal());
            }
        }
    }
}