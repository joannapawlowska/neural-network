package io.pawlowska.network.network;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Connection {

    private final Neuron neuronFrom;
    private final Neuron neuronTo;
    @Setter private double weight;

    public Connection(Neuron neuronFrom, Neuron neuronTo, double weight) {
        this.neuronFrom = neuronFrom;
        this.neuronTo = neuronTo;
        this.weight = weight;
    }
}