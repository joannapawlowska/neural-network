package io.pawlowska.network.training;

import io.pawlowska.network.architecture.NeuralNetwork;
import io.pawlowska.network.data.Record;

public interface Training {

    void perform(NeuralNetwork network, Record[] trainSet);
}