package io.pawlowska.network.training;

import io.pawlowska.network.network.NeuralNetwork;
import lombok.Getter;

import java.nio.file.Path;

@Getter
public abstract class TrainingBuilder<T extends TrainingBuilder<T>> {

    private NeuralNetwork neuralNetwork;
    private Path writePath;
    private int epochs;

    public abstract Training build();

    public abstract T self();

    public T epochs(int epochs) {
        this.epochs = epochs;
        return this.self();
    }

    public T writePath(Path writePath) {
        this.writePath = writePath;
        return this.self();
    }

    public T neuralNetwork(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        return this.self();
    }

    protected void validate() {
        validateEpochs();
        validateWritePath();
        validateNeuralNetwork();
    }

    private void validateEpochs() {
        if (epochs < 0) {
            throw new IllegalArgumentException("Epoch number can not be less than 0");
        }
    }

    private void validateWritePath() {
        if (writePath == null) {
            throw new NullPointerException("Write path can not be null");
        }
    }

    private void validateNeuralNetwork() {
        if (neuralNetwork == null) {
            throw new NullPointerException("Neural network can not be null");
        }
    }
}