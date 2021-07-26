package io.pawlowska.network.training;

import io.pawlowska.network.network.NeuralNetwork;
import io.pawlowska.network.training.pruning.Pruning;
import lombok.AccessLevel;
import lombok.Getter;

import java.nio.file.Path;

@Getter(AccessLevel.PACKAGE)
public abstract class TrainingBuilder<T extends TrainingBuilder<T>> {

    private NeuralNetwork neuralNetwork;
    private Path writePath;
    private int epochs;
    private Pruning pruning;

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

    public T pruning(Pruning pruning){
        this.pruning = pruning;
        return this.self();
    }

    protected void validate() {
        validateEpochs();
        validateWritePath();
        validateNeuralNetwork();
    }

    void validateEpochs() {
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