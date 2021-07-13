package io.pawlowska.network.architecture;

import io.pawlowska.network.exceptions.NoSuchConnectionException;
import io.pawlowska.network.functions.ActivationFunction;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public class Neuron {


    private static final Random random = new Random();

    private double signal;
    private double gradient;
    private List<Connection> inputConnections;
    private List<Connection> outputConnections;
    private ActivationFunction activationFunction;

    public Neuron(ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
        inputConnections = new ArrayList<>();
        outputConnections = new ArrayList<>();
    }

    public void connectWithNextLayer(Layer nextLayer) {

        double min = -1;
        double max = 1;

        for (Neuron neuron : nextLayer.getNeurons()) {

            Connection c = new Connection(this, neuron, min + (max - min) * random.nextDouble());
            outputConnections.add(c);
            neuron.getInputConnections().add(c);
        }
    }

    public void activate() {
        signal = activationFunction.calculate(calculateInputSum());
    }

    public double derivativeActivate() {
        return activationFunction.calculateDerivative(calculateInputSum());
    }

    public double calculateInputSum() {
        double sum = 0;

        for (Connection connection : inputConnections) {
            sum += connection.getNeuronFrom().getSignal() * connection.getWeight();
        }

        return sum;
    }

    public Connection getInputConnectionWith(Neuron neuron) throws NoSuchConnectionException {

        for (Connection connection : inputConnections) {
            if (connection.getNeuronFrom() == neuron)
                return connection;
        }

        throw new NoSuchConnectionException("There is no connection from the given neuron");
    }

    public Connection getOutputConnectionWith(Neuron neuron) throws NoSuchConnectionException {

        for (Connection connection : outputConnections) {
            if (connection.getNeuronTo() == neuron)
                return connection;
        }

        throw new NoSuchConnectionException("There is no connection to the given neuron");
    }

    public void fixOutputConnectionWithNeuron(Neuron neuronTo, double weight){
        Connection connection = getOutputConnectionWith(neuronTo);
        connection.setWeight(weight);
    }

    public void fixInputConnectionWithNeuron(Neuron neuronFrom, double weight){
        Connection connection = getInputConnectionWith(neuronFrom);
        connection.setWeight(weight);
    }
}