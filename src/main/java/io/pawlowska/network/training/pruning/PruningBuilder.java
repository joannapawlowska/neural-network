package io.pawlowska.network.training.pruning;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PACKAGE)
public class PruningBuilder {

    private double removalThreshold;
    private int minLayerSize;

    public PruningBuilder removalThreshold(double removalThreshold) {
        this.removalThreshold = removalThreshold;
        return this;
    }

    public PruningBuilder minLayerSize(int minLayerSize) {
        this.minLayerSize = minLayerSize;
        return this;
    }

    public Pruning build() {
        validate();
        return new Pruning(this);
    }

    protected void validate() {
        validateRemovalThreshold();
        validateMinLayerSize();
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
}