package io.pawlowska.network.training.pruning;

import io.pawlowska.network.training.Training;
import io.pawlowska.network.training.TrainingBuilder;
import lombok.Getter;

@Getter
public class PruningBuilder extends TrainingBuilder<PruningBuilder> {

    private double removalThreshold;
    private int minLayerSize;
    private Training training;

    @Override
    public PruningBuilder self() {
        return this;
    }

    public PruningBuilder removalThreshold(double removalThreshold) {
        this.removalThreshold = removalThreshold;
        return this;
    }

    public PruningBuilder minLayerSize(int minLayerSize) {
        this.minLayerSize = minLayerSize;
        return this;
    }

    public PruningBuilder training(Training training) {
        this.training = training;
        return this;
    }

    @Override
    public Training build() {
        validate();
        return new Pruning(this);
    }

    @Override
    protected void validate() {
        super.validate();
        validateRemovalThreshold();
        validateMinLayerSize();
        validateTraining();
    }

    private void validateRemovalThreshold() {
        if (removalThreshold < 0) {
            throw new IllegalArgumentException("Minimal layer size can not be less than 1");
        }
    }

    private void validateMinLayerSize() {
        if (minLayerSize < 1) {
            throw new IllegalArgumentException("Minimal layer size can not be less than 1");
        }
    }

    private void validateTraining() {
        if (training == null) {
            throw new NullPointerException("Training can not be null");
        }
    }
}