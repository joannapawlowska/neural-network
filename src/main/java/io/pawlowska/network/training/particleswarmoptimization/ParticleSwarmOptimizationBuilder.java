package io.pawlowska.network.training.particleswarmoptimization;

import io.pawlowska.network.training.Training;
import io.pawlowska.network.training.TrainingBuilder;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PACKAGE)
public class ParticleSwarmOptimizationBuilder extends TrainingBuilder<ParticleSwarmOptimizationBuilder> {

    private int epochs;
    private int swarmSize;
    private double startVelocity;
    private double startPositionMinRange;
    private double startPositionMaxRange;

    /* Describes how much the previous velocity influence on current velocity. The smaller is
    factor value, the smaller is particle velocity hence particle will tend to local exploitation
    of search space.*/
    private double inertiaWeight;

    /* Represent the particle acceleration weight toward the swarm (global) best found solution */
    private double globalAcceleration;

    /* Represent the particle acceleration weight toward the personal (local) best found solution */
    private double localAcceleration;


    @Override
    public ParticleSwarmOptimizationBuilder self() {
        return this;
    }

    public ParticleSwarmOptimizationBuilder epochs(int epochs) {
        this.epochs = epochs;
        return this;
    }

    public ParticleSwarmOptimizationBuilder swarmSize(int swarmSize) {
        this.swarmSize = swarmSize;
        return this;
    }

    public ParticleSwarmOptimizationBuilder startVelocity(double startVelocity) {
        this.startVelocity = startVelocity;
        return this;
    }

    public ParticleSwarmOptimizationBuilder startPositionMinRange(double startPositionMinRange) {
        this.startPositionMinRange = startPositionMinRange;
        return this;
    }

    public ParticleSwarmOptimizationBuilder startPositionMaxRange(double startPositionMaxRange) {
        this.startPositionMaxRange = startPositionMaxRange;
        return this;
    }

    public ParticleSwarmOptimizationBuilder inertiaWeight(double inertiaWeight) {
        this.inertiaWeight = inertiaWeight;
        return this;
    }

    public ParticleSwarmOptimizationBuilder globalAcceleration(double globalAcceleration) {
        this.globalAcceleration = globalAcceleration;
        return this;
    }

    public ParticleSwarmOptimizationBuilder localAcceleration(double localAcceleration) {
        this.localAcceleration = localAcceleration;
        return this;
    }


    @Override
    public Training build() {
        validate();
        return new ParticleSwarmOptimization(this);
    }

    @Override
    protected void validate() {
        super.validate();
        validateStartPositionRange();
        validateSwarmSize();
        validateInertiaWeight();
        validateLocalAcceleration();
        validateGlobalAcceleration();
    }

    private void validateStartPositionRange() {
        if (startPositionMinRange > startPositionMaxRange) {
            throw new IllegalArgumentException("Gene min range can not be bigger than gene max range");
        }
    }

    private void validateSwarmSize() {
        if (swarmSize < 0) {
            throw new IllegalArgumentException("Swarm size can not be less than 0");
        }
    }

    private void validateInertiaWeight() {
        if (inertiaWeight < 0) {
            throw new IllegalArgumentException("Particle inertia weight size can not be less than 0");
        }
    }

    private void validateLocalAcceleration() {
        if (localAcceleration < 0) {
            throw new IllegalArgumentException("Local acceleration can not be less than 0");
        }
    }

    private void validateGlobalAcceleration() {
        if (globalAcceleration < 0) {
            throw new IllegalArgumentException("Global acceleration can not be less than 0");
        }
    }
}