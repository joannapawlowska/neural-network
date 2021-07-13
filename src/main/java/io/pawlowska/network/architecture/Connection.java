package io.pawlowska.network.architecture;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Connection {

    private Neuron neuronFrom;
    private Neuron neuronTo;
    private double weight;

    public Connection(Neuron neuronFrom, Neuron neuronTo, double weight) {
        this.neuronFrom = neuronFrom;
        this.neuronTo = neuronTo;
        this.weight = weight;
    }
}