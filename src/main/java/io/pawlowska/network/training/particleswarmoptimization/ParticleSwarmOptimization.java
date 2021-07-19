package io.pawlowska.network.training.particleswarmoptimization;

import io.pawlowska.network.training.HeuristicTraining;
import io.pawlowska.network.training.Training;
import lombok.Getter;

import java.util.Collections;
import java.util.List;


public class ParticleSwarmOptimization extends Training implements HeuristicTraining {

    @Getter
    private final int epochs;
    private final int swarmSize;
    private Swarm swarm;

    public ParticleSwarmOptimization(ParticleSwarmOptimizationBuilder builder) {

        super(builder);
        epochs = builder.getEpochs();
        swarmSize = builder.getSwarmSize();

        Particle.Parameters.setAll(
                builder.getInertiaWeight(), builder.getGlobalAcceleration(),
                builder.getLocalAcceleration(), builder.getStartPositionMinRange(),
                builder.getStartPositionMaxRange(), builder.getStartVelocity());

        fillSwarmWithParticles();
        setBestGlobalParticle();
    }

    private void fillSwarmWithParticles() {

        swarm = new Swarm();
        int positionSize = calculatePositionSize();
        fillSwarmWithParticles(positionSize);
    }

    private int calculatePositionSize() {
        return network.getConnectionsAmount();
    }

    private void fillSwarmWithParticles(int positionSize) {

        for (int i = 0; i < swarmSize; i++) {

            Particle particle = Particle.builder()
                    .positionSize(positionSize)
                    .build();

            double assessment = calculateAssessment(particle.getBestPosition());
            particle.setBestPositionAssessment(assessment);
            swarm.add(particle);
        }
    }

    private double calculateAssessment(List<Double> position) {

        network.fixConnectionsWeights(position);
        return network.calculateErrorForTrainingSet();
    }

    private void setBestGlobalParticle() {
        swarm.setBestGlobalParticle(swarm.get(0));
        swarm.fixBestGlobalParticle();
    }

    @Override
    public void performOneEpochOfTraining() {

        timer.start();

        for (Particle particle : swarm) {
            trainParticle(particle, swarm.getBestGlobalParticle());
        }

        timer.stop();

        swarm.fixBestGlobalParticle();
        network.fixConnectionsWeights(swarm.getBestGlobalParticle().getPosition());
    }

    private void trainParticle(Particle particle, Particle bestGlobalParticle) {

        particle.moveParticle(bestGlobalParticle);
        fixBestPosition(particle);
    }

    private void fixBestPosition(Particle particle) {

        double currentAssessment = calculateAssessment(particle.getPosition());

        if (currentAssessment < particle.getBestPositionAssessment()) {
            fixParticleBestPosition(particle, currentAssessment);
        }
    }

    public void fixParticleBestPosition(Particle particle, double error) {
        particle.setBestPositionAssessment(error);
        particle.setCurrentPositionAsBestLocal();
    }

    @Override
    public void changeDimensionOfSolutions(List<Integer> toRemoveCoordinates) {

        toRemoveCoordinates.stream()
                .sorted(Collections.reverseOrder())
                .forEach(index ->
                        swarm.forEach(particle -> {
                                    particle.getPosition().remove(index.intValue());
                                    particle.getVelocity().remove(index.intValue());
                                }
                        )
                );
    }
}