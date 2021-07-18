package io.pawlowska.network.utils;

import io.pawlowska.network.architecture.Layer;
import io.pawlowska.network.architecture.Neuron;
import io.pawlowska.network.exceptions.MissingLayerException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConnectionsNumerator {

    private List<Layer> layers;
    private int neuronIndex;
    private int layerIndex;

    public ConnectionsNumerator(List<Layer> layers) {
        this.layers = layers;
    }

    public List<Integer> getNeuronConnectionsNumbers(Neuron neuron) {

        List<Integer> connectionsNumbers = new ArrayList<>();

        Layer layer = findLayerWhichNeuronBelongTo(neuron);
        neuronIndex = layer.indexOf(neuron);
        layerIndex = layers.indexOf(layer);

        connectionsNumbers.addAll(getNeuronOutputConnectionsNumbers());
        connectionsNumbers.addAll(getNeuronInputConnectionsNumbers());

        return shiftLeftToFitToListIndexes(connectionsNumbers);
    }

    private Layer findLayerWhichNeuronBelongTo(Neuron neuron) {

        Optional<Layer> layer = layers.stream()
                .filter(l -> l.contains(neuron))
                .findFirst();

        if(layer.isEmpty()){
            throw new MissingLayerException("Given neuron does not belong to any layer");
        }

        return layer.get();
    }

    private List<Integer> getNeuronOutputConnectionsNumbers() {

        List<Integer> outputConnectionNumbers = new ArrayList<>();

        if (!isOutputLayer()) {

            int number = 0;
            number += calculateNumberOfConnectionsBeforeLayer(layerIndex);
            number += calculateNumberOfConnectionsInLayerBeforeNeuron();
            outputConnectionNumbers = getNeuronOutputConnectionsNumbers(number);
        }

        return outputConnectionNumbers;
    }

    private boolean isOutputLayer() {
        return layerIndex == layers.size() - 1;
    }

    private int calculateNumberOfConnectionsBeforeLayer(int layerIndex) {

        int currentIndex = 0;
        int nextIndex = 1;
        int number = 0;

        while (currentIndex != layerIndex) {

            number += layers.get(currentIndex).size() * layers.get(nextIndex).size();
            currentIndex++;
            nextIndex++;
        }

        return number;
    }

    private int calculateNumberOfConnectionsInLayerBeforeNeuron() {
        return neuronIndex * layers.get(layerIndex + 1).size();
    }

    private List<Integer> getNeuronOutputConnectionsNumbers(int number) {

        List<Integer> outputConnectionNumbers = new ArrayList<>();
        int numberOfNeuronsFromNextLayer = layers.get(layerIndex + 1).size();

        while (numberOfNeuronsFromNextLayer-- > 0) {
            outputConnectionNumbers.add(++number);
        }

        return outputConnectionNumbers;
    }

    private List<Integer> getNeuronInputConnectionsNumbers() {

        List<Integer> inputConnectionNumbers = new ArrayList<>();

        if (!isInputLayer()) {

            int number = 0;
            number += calculateNumberOfConnectionsBeforeLayer(layerIndex - 1);
            number += calculateNumberOfConnectionsInLayerBeforeFirstToNeuron();
            inputConnectionNumbers = getNeuronInputConnectionsNumbers(number);
        }

        return inputConnectionNumbers;
    }

    private boolean isInputLayer() {
        return layerIndex == 0;
    }

    private int calculateNumberOfConnectionsInLayerBeforeFirstToNeuron() {
        return neuronIndex + 1;
    }

    private List<Integer> getNeuronInputConnectionsNumbers(int number) {

        List<Integer> inputConnectionNumbers = new ArrayList<>();
        int numberOfNeurons = layers.get(layerIndex).size();
        int numberOfNeuronsFromBeforeLayer = layers.get(layerIndex - 1).size();

        for (int i = 0; i < numberOfNeuronsFromBeforeLayer; i++) {
            inputConnectionNumbers.add(number);
            number += numberOfNeurons;
        }

        return inputConnectionNumbers;
    }

    private List<Integer> shiftLeftToFitToListIndexes(List<Integer> connectionsNumbers) {

        return connectionsNumbers.stream()
                .map(integer -> --integer)
                .collect(Collectors.toList());
    }
}