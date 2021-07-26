package io.pawlowska.network.training.pruning;

import io.pawlowska.network.network.Connection;
import io.pawlowska.network.network.Layer;
import io.pawlowska.network.network.NeuralNetwork;
import io.pawlowska.network.network.Neuron;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class Pruning {

    private double removalThreshold;
    private int minLayerSize;
    private double beforePrunedNetworkError;
    private Layer layerToRemoveFrom;
    private Neuron neuronToRemove;
    private int neuronIndex;
    @Getter private boolean pruned;
    @Setter private NeuralNetwork network;

    public Pruning(PruningBuilder builder) {
        removalThreshold = builder.getRemovalThreshold();
        minLayerSize = builder.getMinLayerSize();
    }

    public boolean existNeuronAppropriateToPrune() {

        layerToRemoveFrom = null;
        neuronToRemove = null;
        pruned = false;
        Layer layer = network.getLayerAfter(network.getInputLayer());

        while (network.isHiddenLayer(layer)) {

            if (couldBePrunedFrom(layer)) {
                findNeuronToPrune(layer);
            }
            layer = network.getLayerAfter(layer);
        }

        return isFoundNeuronToPrune();
    }

    private boolean couldBePrunedFrom(Layer layer) {
        return layer.size() > minLayerSize;
    }

    private void findNeuronToPrune(Layer layer) {

        for (Neuron neuron : layer) {

            if (couldBePruned(neuron)) {

                if (!isFoundNeuronToPrune() || isMoreAppropriateToPrune(neuron)) {
                    setToBePruned(neuron, layer);
                }
            }
        }
    }

    private boolean couldBePruned(Neuron neuron) {
        return Math.abs(neuron.getSignal()) < removalThreshold;
    }

    private boolean isFoundNeuronToPrune() {
        return layerToRemoveFrom != null && neuronToRemove != null;
    }

    private boolean isMoreAppropriateToPrune(Neuron neuron) {
        return Math.abs(neuron.getSignal()) < Math.abs(neuronToRemove.getSignal());
    }

    private void setToBePruned(Neuron neuron, Layer layer) {
        layerToRemoveFrom = layer;
        neuronToRemove = neuron;
        neuronIndex = layer.indexOf(neuron);
    }

    public void prune() {

        calculateBeforePrunedNetworkError();
        disconnectNeuron();
        layerToRemoveFrom.remove(neuronToRemove);
        pruned = true;
    }

    private void calculateBeforePrunedNetworkError() {
        beforePrunedNetworkError = network.calculateErrorForTrainingSet();
    }

    private void disconnectNeuron() {

        for (Connection c : neuronToRemove.getInputConnections()) {
            c.getNeuronFrom().getOutputConnections().remove(c);
        }

        for (Connection c : neuronToRemove.getOutputConnections()) {
            c.getNeuronTo().getInputConnections().remove(c);
        }
    }


    public boolean shouldUndoPruning() {
        return pruned && beforePrunedNetworkError < network.calculateErrorForTrainingSet();
    }

    public void undoPruning() {

        pruned = false;
        layerToRemoveFrom.add(neuronIndex, neuronToRemove);
        connectNeuron();
    }

    private void connectNeuron() {

        for (Connection c : neuronToRemove.getInputConnections()) {
            c.getNeuronFrom().getOutputConnections().add(c);
        }

        for (Connection c : neuronToRemove.getOutputConnections()) {
            c.getNeuronTo().getInputConnections().add(c);
        }
    }

    public List<Integer> getRemovedNeuronConnectionNumbers() {
        return network.getNeuronConnectionsNumbers(neuronToRemove);
    }
}