package io.pawlowska.network.network;

import java.util.ArrayList;

public class Layer extends ArrayList<Neuron> {

    public Layer(int size) {

        for (int i = 0; i < size; i++) {
            add(new Neuron());
        }
    }

    public Neuron getNeuron(int index) {
        return get(index);
    }
}