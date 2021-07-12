package io.pawlowska.network.training;

import io.pawlowska.network.architecture.Connection;
import io.pawlowska.network.architecture.Layer;
import io.pawlowska.network.architecture.NeuralNetwork;
import io.pawlowska.network.architecture.Neuron;
import io.pawlowska.network.data.Record;
import io.pawlowska.network.utils.FileReaderAndWriter;
import lombok.Builder;


public class BackPropagation extends Training {

    private int epochs;
    private double ETA;

    @Builder
    private BackPropagation(int epochs, double ETA) {
        super();
        this.epochs = epochs;
        this.ETA = ETA;
    }

    @Override
    public void perform(NeuralNetwork network) {

        super.perform(network);

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

        collectTrainingResultOnValidatingSet();
        FileReaderAndWriter.writeToFile(trainingResultCollector.getResults(), network.getWritePath());
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