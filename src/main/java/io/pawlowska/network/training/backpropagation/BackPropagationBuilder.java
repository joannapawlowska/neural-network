package io.pawlowska.network.training.backpropagation;

import io.pawlowska.network.training.Training;
import io.pawlowska.network.training.TrainingBuilder;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PACKAGE)
public class BackPropagationBuilder extends TrainingBuilder<BackPropagationBuilder> {

    private double learningRate;

    @Override
    public BackPropagationBuilder self() {
        return this;
    }

    public BackPropagationBuilder learningRate(double learningRate) {
        this.learningRate = learningRate;
        return this;
    }

    @Override
    public Training build() {
        validate();
        return new BackPropagation(this);
    }

    @Override
    protected void validate() {
        super.validate();
        validateLearningRate();
    }

    public void validateLearningRate() {
        if (learningRate < 0 || learningRate > 1) {
            throw new IllegalArgumentException("Learning rate must be from range (0, 1)");
        }
    }
}