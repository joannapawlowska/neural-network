package io.pawlowska.network.training.backpropagation;

import io.pawlowska.network.network.Connection;
import io.pawlowska.network.network.Layer;
import io.pawlowska.network.network.Neuron;
import io.pawlowska.network.data.Record;
import io.pawlowska.network.training.Training;


public class BackPropagation extends Training {

    private double learningRate;

    public BackPropagation(BackPropagationBuilder builder) {
        super(builder);
        this.learningRate = builder.getLearningRate();
    }

    @Override
    public void performOneEpochOfTraining() {

        timer.start();

        for (Record record : network.getDataSet().getTrainingSet()) {

            derivativeActivateOutputLayer(record);
            derivativeActivateHiddenLayers();
            fixConnectionWeights();
        }

        timer.stop();
    }

    private void derivativeActivateOutputLayer(Record record) {

        int i = 0;
        int[] expectedDecision = record.getMask();
        double[] outputDecision = network.predict(record.getData());

        for (Neuron neuron : network.getOutputLayer()) {

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

        for (Neuron neuron : layer) {

            double gradient = neuron.getOutputConnections()
                    .stream()
                    .mapToDouble(c -> c.getNeuronTo().getGradient() * c.getWeight())
                    .sum();

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

        for (Neuron neuron : layer) {

            for (Connection connection : neuron.getInputConnections()) {
                connection.setWeight(connection.getWeight() + learningRate * neuron.getGradient() * connection.getNeuronFrom().getSignal());
            }
        }
    }
}