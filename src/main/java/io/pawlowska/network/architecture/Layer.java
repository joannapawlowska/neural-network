package io.pawlowska.network.architecture;

import io.pawlowska.network.functions.ActivationFunction;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Layer {

    private List<Neuron> neurons;

    public Layer(int size, ActivationFunction activationFunction) {
        neurons = new ArrayList<>(size);
        addNeuronsToLayer(size, activationFunction);
    }

    private void addNeuronsToLayer(int layerSize, ActivationFunction activationFunction) {

        for (int i = 0; i < layerSize; i++) {
            neurons.add(new Neuron(activationFunction));
        }
    }
}