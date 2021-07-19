package io.pawlowska.network.training.pruning;

import io.pawlowska.network.network.Connection;
import io.pawlowska.network.network.Layer;
import io.pawlowska.network.network.Neuron;
import io.pawlowska.network.training.HeuristicTraining;
import io.pawlowska.network.training.Training;

import java.util.List;

public class Pruning extends Training {

    private double removalThreshold;
    private int minLayerSize;
    private Training training;
    private double beforePrunedNetworkError;
    private Layer layerToRemoveFrom;
    private Neuron neuronToRemove;
    private int neuronIndex;

    public Pruning(PruningBuilder builder) {
        super(builder);
        removalThreshold = builder.getRemovalThreshold();
        minLayerSize = builder.getMinLayerSize();
        training = builder.getTraining();
    }

    @Override
    public void performOneEpochOfTraining() {

        if (existNeuronAppropriateToPrune()) {
            beforePrunedNetworkError = network.calculateErrorForTrainingSet();
            prune();
        }

        performEpochOfTrainingChangingWeights();

        if (shouldUndoPruning()) {
            undoPruning();
        }else if(pruned){
            if(training instanceof HeuristicTraining){
                adjustTrainingToNewNetworkArchitecture();
            }
        }
    }

    private boolean existNeuronAppropriateToPrune() {

        layerToRemoveFrom = null;
        neuronToRemove = null;
        pruned = false;
        Layer layer = network.getLayerAfter(network.getInputLayer());

        while (couldBePrunedFrom(layer)) {
            findNeuronToPrune(layer);
        }

        return isFoundNeuronToPrune();
    }

    private boolean couldBePrunedFrom(Layer layer) {
        return network.isHiddenLayer(layer) && layer.size() > minLayerSize;
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
        return neuron.getSignal() < removalThreshold;
    }

    private boolean isFoundNeuronToPrune() {
        return layerToRemoveFrom != null && neuronToRemove != null;
    }

    private boolean isMoreAppropriateToPrune(Neuron neuron) {
        return neuron.getSignal() < neuronToRemove.getSignal();
    }

    private void setToBePruned(Neuron neuron, Layer layer) {
        layerToRemoveFrom = layer;
        neuronToRemove = neuron;
        neuronIndex = layer.indexOf(neuron);
    }

    private void prune() {

        disconnectNeuron();
        layerToRemoveFrom.remove(neuronToRemove);
        pruned = true;
    }

    private void disconnectNeuron() {

        for (Connection c : neuronToRemove.getInputConnections()) {
            c.getNeuronFrom().getOutputConnections().remove(c);
        }

        for (Connection c : neuronToRemove.getOutputConnections()) {
            c.getNeuronTo().getInputConnections().remove(c);
        }
    }

    private void performEpochOfTrainingChangingWeights(){
        timer.start();
        training.performOneEpochOfTraining();
        timer.stop();
    }

    private boolean shouldUndoPruning() {
        return pruned && beforePrunedNetworkError < network.calculateErrorForTrainingSet();
    }

    private void undoPruning() {

        pruned = false;
        layerToRemoveFrom.add(neuronIndex - 1, neuronToRemove);
        connectNeuron();
        performEpochOfTrainingChangingWeights();
    }

    private void connectNeuron(){

        for (Connection c : neuronToRemove.getInputConnections()) {
            c.getNeuronFrom().getOutputConnections().add(c);
        }

        for (Connection c : neuronToRemove.getOutputConnections()) {
            c.getNeuronTo().getInputConnections().add(c);
        }
    }

    private void adjustTrainingToNewNetworkArchitecture(){

        List<Integer> toRemove = network.getNeuronConnectionsNumbers(neuronToRemove);
        ((HeuristicTraining) training).changeDimensionOfSolutions(toRemove);
    }
}