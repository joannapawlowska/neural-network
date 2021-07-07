package io.pawlowska.network.architecture;

import io.pawlowska.network.exceptions.NoSuchConnectionException;
import io.pawlowska.network.functions.ActivationFunction;
import io.pawlowska.network.functions.ActivationFunctionDerivative;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Neuron {

    private double signal;
    private double gradient;
    private List<Connection> inputConnections;
    private List<Connection> outputConnections;
    private ActivationFunction activationFunction;
    private ActivationFunctionDerivative activationFunctionDerivative;

    public Neuron() {
        inputConnections = new ArrayList<>();
        outputConnections = new ArrayList<>();
    }

    public void connectWithNextLayer(Layer nextLayer) {

        for (Neuron neuron : nextLayer.getNeurons()) {

            outputConnections.add(new Connection(this, neuron));
            neuron.getInputConnections().add(new Connection(this, neuron));
        }
    }

    public void activate() {
        signal = activationFunction.calculate(calculateInputSum());
    }

    public void derivativeActivate() {
        signal = activationFunctionDerivative.calculate(calculateInputSum());
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