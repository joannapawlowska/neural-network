package io.pawlowska.network.network;

import io.pawlowska.network.data.DataSetCopier;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetworkCopier {

    private NeuralNetworkBuilder builder;
    private NeuralNetwork network;
    private NeuralNetwork copied;

    private NeuralNetworkCopier() {
        builder = new NeuralNetworkBuilder();
    }

    public static NeuralNetwork copy(NeuralNetwork neuralNetwork) {
        return new NeuralNetworkCopier().copyNetwork(neuralNetwork);
    }

    private NeuralNetwork copyNetwork(NeuralNetwork neuralNetwork) {
        this.network = neuralNetwork;
        copyActivationFunction();
        copyDataSet();
        copyLayers();
        copied = builder.build();
        copyNeuronsParams();
        copyConnectionsWeights();
        return copied;
    }

    private void copyActivationFunction() {
        builder.activationFunction(network.getInputLayer()
                .getNeuron(0)
                .getActivationFunction()
                .copy());
    }

    private void copyDataSet() {
        builder.dataSet(
                DataSetCopier.copy(network.getDataSet())
        );
    }

    private void copyLayers() {

        List<Layer> layers = network.getLayers();

        for (Layer layer : layers) {

            if (layer == network.getInputLayer()) {
                builder.inputLayer(new Layer(layer.size()));

            } else if (layer == network.getOutputLayer()) {
                builder.outputLayer(new Layer(layer.size()));

            } else {
                builder.hiddenLayer(new Layer(layer.size()));
            }
        }
    }

    private void copyNeuronsParams() {

        List<Layer> layersToClone = network.getLayers();
        List<Layer> layers = copied.getLayers();

        for (int i = 0; i < layersToClone.size(); i++) {

            for (int j = 0; j < layersToClone.get(i).size(); j++) {
                copyParams(layers.get(i).getNeuron(j), layersToClone.get(i).getNeuron(j));
            }
        }
    }

    private void copyParams(Neuron neuron, Neuron neuronToClone) {
        neuron.setGradient(neuronToClone.getGradient());
        neuron.setSignal(neuronToClone.getSignal());
    }

    public void copyConnectionsWeights() {

        List<Double> weights = new ArrayList<>();
        Layer layer = network.getInputLayer();
        Layer layerAfter;

        do {
            layerAfter = network.getLayerAfter(layer);

            for (Neuron neuron : layer) {
                for (Neuron neuronTo : layerAfter) {
                    double weight = neuron.getOutputConnectionWith(neuronTo).getWeight();
                    weights.add(weight);
                }
            }

            layer = layerAfter;

        } while (network.hasLayerAfter(layer));

        copied.fixConnectionsWeights(weights);
    }
}