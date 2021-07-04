package io.pawlowska.network.architecture;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Layer {

    private List<Neuron> neurons;

    public Layer(int size) {
        neurons = new ArrayList<>(size);
        addNeuronsToLayer(size);
    }

    private void addNeuronsToLayer(int layerSize) {

        for (int i = 0; i < layerSize; i++) {
            neurons.add(new Neuron());
        }
    }

}