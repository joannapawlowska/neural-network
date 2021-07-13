package io.pawlowska.network.training.particleswarmoptimization;

import io.pawlowska.network.architecture.Layer;
import io.pawlowska.network.architecture.NeuralNetwork;
import io.pawlowska.network.architecture.Neuron;
import io.pawlowska.network.training.Training;
import lombok.Builder;


public class ParticleSwarmOptimization extends Training {

    @Builder.Default
    private int epochs = 1000;

    @Builder.Default
    private int swarmSize = 10;

    @Builder.Default
    private int particleStartVelocity = 5;

    @Builder.Default
    private double particleMinRange = -1;

    @Builder.Default
    private double particleMaxRange = 1;

    /* Describes how much the previous velocity influence on current velocity. The smaller is
    factor value, the smaller is particle velocity hence particle will tend to local exploitation
    of search space.*/
    @Builder.Default
    private double particleInertiaWeight = 0.5;

    /* Represent the particle acceleration weight toward the swarm (global) best found solution */
    @Builder.Default
    private double particleGlobalAcceleration = 0.7;

    /* Represent the particle acceleration weight toward the personal (local) best found solution */
    @Builder.Default
    private double particleLocalAcceleration = 0.3;

    private Swarm swarm;

    @Builder
    private ParticleSwarmOptimization(int epochs, int swarmSize, int particleStartVelocity,
                                     double particleMinRange, double particleMaxRange,
                                     double particleInertiaWeight, double particleGlobalAcceleration,
                                     double particleLocalAcceleration) {
        super();
        this.swarm = new Swarm(swarmSize);
        fillSwarmWithParticles();
        setBestGlobalParticle();
    }

    @Override
    public void perform() {

        while (epochs-- > 0) {

            timer.start();
            trainSwarm();
            timer.stop();

            fixConnectionsWeights(swarm.getBestGlobalParticle().getPosition());
            collectTrainingResultOnTrainingSet();
        }
    }

    private void fillSwarmWithParticles() {

        int particleLength = calculateParticleLength(network);
        addParticlesToSwarm(particleLength);
    }

    private int calculateParticleLength(NeuralNetwork network) {

        int connectionsAmount = 1;

        Layer layer = network.getInputLayer();
        Layer layerAfter;

        do {
            layerAfter = network.getLayerAfter(layer);
            connectionsAmount += layer.getNeurons().size() * layerAfter.getNeurons().size();
            layer = layerAfter;

        } while (network.hasLayerAfter(layer));

        return connectionsAmount;
    }

    private void addParticlesToSwarm(int particleLength) {

        Particle[] particles = swarm.getParticles();

        for (int i = 0; i < swarmSize; i++) {

            particles[i] = Particle.builder(particleLength)
                    .startVelocity(particleStartVelocity)
                    .rangeMax(particleMaxRange)
                    .rangeMin(particleMinRange)
                    .globalAcceleration(particleGlobalAcceleration)
                    .localAcceleration(particleLocalAcceleration)
                    .inertiaWeight(particleInertiaWeight)
                    .build();

            calculateBestLocalPositionValue(particles[i]);
        }
    }

    private void calculateBestLocalPositionValue(Particle particle){

        double[] position;
        double error;

        position = particle.getPosition();
        fixConnectionsWeights(position);

        error = network.calculateErrorForTrainingSet();
        particle.setBestLocalPositionValue(error);
    }

    private void setBestGlobalParticle() {
        swarm.setBestGlobalParticle(swarm.getParticles()[0]);
        swarm.fixBestGlobalParticle();
    }

    private void trainSwarm() {

        Particle bestGlobalParticle = swarm.getBestGlobalParticle();

        for (Particle particle : swarm.getParticles()) {
            trainParticle(particle, bestGlobalParticle);
        }

        swarm.fixBestGlobalParticle();
    }

    private void trainParticle(Particle particle, Particle bestGlobalParticle) {

        particle.moveParticle(bestGlobalParticle);
        fixBestLocalPosition(particle);
    }

    private void fixBestLocalPosition(Particle particle) {

        double[] vector = particle.getPosition();
        fixConnectionsWeights(vector);
        double error = network.calculateErrorForTrainingSet();

        if (error < particle.getBestLocalPositionValue()) {
            fixParticleBestPosition(particle, error);
        }
    }

    public void fixConnectionsWeights(double [] weights){

        int i = 0;
        Layer layer = network.getInputLayer();
        Layer layerAfter;

        do {
            layerAfter = network.getLayerAfter(layer);

            for(Neuron neuron : layer.getNeurons()){
                for(Neuron neuronTo : layerAfter.getNeurons()) {
                    neuron.fixOutputConnectionWithNeuron(neuronTo, weights[i++]);
                }
            }

            layer = layerAfter;

        } while (network.hasLayerAfter(layer));
    }

    public void fixParticleBestPosition(Particle particle, double error) {
        particle.setBestLocalPositionValue(error);
        particle.setCurrentPositionAsBestLocal();
    }

    public static class ParticleSwarmOptimizationBuilder {

        private int epochs;
        private int swarmSize;
        private double particleInertiaWeight;
        private double particleGlobalAcceleration;
        private double particleLocalAcceleration;

        public ParticleSwarmOptimizationBuilder epochs(int epochs) {
            this.epochs = epochs;
            verifyEpochs();
            return this;
        }

        public ParticleSwarmOptimizationBuilder swarmSize(int swarmSize) {
            this.swarmSize = swarmSize;
            verifySwarmSize();
            return this;
        }

        public ParticleSwarmOptimizationBuilder particleInertiaWeight(int particleInertiaWeight) {
            this.particleInertiaWeight = particleInertiaWeight;
            verifyParticleInertiaWeight();
            return this;
        }

        public ParticleSwarmOptimizationBuilder particleLocalAcceleration(int particleLocalAcceleration) {
            this.particleLocalAcceleration = particleLocalAcceleration;
            verifyLocalAcceleration();
            return this;
        }

        public ParticleSwarmOptimizationBuilder particleGlobalAcceleration(int particleGlobalAcceleration) {
            this.particleGlobalAcceleration = particleGlobalAcceleration;
            verifyGlobalAcceleration();
            return this;
        }

        private void verifyEpochs() {
            if (epochs < 0) {
                throw new IllegalArgumentException("Epoch number can not be less than 0");
            }
        }

        private void verifySwarmSize() {
            if (swarmSize < 0) {
                throw new IllegalArgumentException("Swarm size can not be less than 0");
            }
        }

        private void verifyParticleInertiaWeight() {
            if (particleInertiaWeight < 0) {
                throw new IllegalArgumentException("Particle inertia weight size can not be less than 0");
            }
        }

        private void verifyLocalAcceleration() {
            if (particleLocalAcceleration < 0) {
                throw new IllegalArgumentException("Local acceleration can not be less than 0");
            }
        }

        private void verifyGlobalAcceleration() {
            if (particleGlobalAcceleration < 0) {
                throw new IllegalArgumentException("Global acceleration can not be less than 0");
            }
        }
    }
}