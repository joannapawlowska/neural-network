package io.pawlowska.network.utils;

import io.pawlowska.network.architecture.Layer;
import io.pawlowska.network.architecture.NeuralNetwork;
import io.pawlowska.network.architecture.NeuralNetworkBuilder;
import io.pawlowska.network.architecture.Neuron;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

class ConnectionsNumeratorTest {

    private NeuralNetworkBuilder builder;

    @BeforeEach
    public void init() {

        builder = spy(NeuralNetworkBuilder.class);
        doReturn(new NeuralNetwork(builder))
                .when(builder)
                .build();
    }

    @Test
    public void shouldFindConnectionNumbersForNeuronFromHiddenLayer() {

        NeuralNetwork network = builder
                .inputLayer(new Layer(2))
                .hiddenLayer(new Layer(4))
                .outputLayer(new Layer(3))
                .build();

        Neuron neuron = network
                .getLayers()
                .get(1)
                .getNeuron(2);

        ConnectionsNumerator numerator = new ConnectionsNumerator(network.getLayers());
        assertEquals(List.of(2, 6, 14, 15, 16), numerator.getNeuronConnectionsNumbers(neuron));
    }

    @Test
    public void shouldFindOnlyOutputConnectionNumbersForNeuronFromInputLayer() {

        NeuralNetwork network = builder
                .inputLayer(new Layer(3))
                .hiddenLayer(new Layer(5))
                .hiddenLayer(new Layer(2))
                .outputLayer(new Layer(3))
                .build();

        Neuron neuron = network
                .getLayers()
                .get(0)
                .getNeuron(2);

        ConnectionsNumerator numerator = new ConnectionsNumerator(network.getLayers());
        assertEquals(List.of(10, 11, 12, 13, 14), numerator.getNeuronConnectionsNumbers(neuron));
    }

    @Test
    public void shouldFindOnlyInputConnectionNumbersForNeuronFromOutputLayer() {

        NeuralNetwork network = builder
                .inputLayer(new Layer(2))
                .hiddenLayer(new Layer(3))
                .outputLayer(new Layer(6))
                .build();

        Neuron neuron = network
                .getLayers()
                .get(2)
                .getNeuron(5);

        ConnectionsNumerator numerator = new ConnectionsNumerator(network.getLayers());
        assertEquals(List.of(11, 17, 23), numerator.getNeuronConnectionsNumbers(neuron));
    }

    private void assertEquals(List<Integer> given, List<Integer> found){

        Assertions.assertAll(
                () -> Assertions.assertEquals(found.size(), given.size()),
                () -> Assertions.assertTrue(given.containsAll(found)),
                () -> Assertions.assertTrue(found.containsAll(given))
        );
    }
}