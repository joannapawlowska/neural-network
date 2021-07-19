package io.pawlowska.network.training.particleswarmoptimization;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Particle implements Comparable<Particle> {

    private static final Random random = new Random();
    private Parameters parameters;
    @Getter private List<Double> velocity;
    @Getter private List<Double> position;
    @Getter private List<Double> bestPosition;
    @Setter @Getter private double bestPositionAssessment;

    @Builder
    private Particle(int positionSize) {
        parameters = new Parameters();
        randomlyGeneratePosition(positionSize);
        generateVelocity(positionSize);
        setCurrentPositionAsBestLocal();
    }

    private void randomlyGeneratePosition(int positionSize) {

        position = new ArrayList<>();

        for (int i = 0; i < positionSize; i++) {
            position.add(randomlyGenerateCoordinate());
        }
    }

    private double randomlyGenerateCoordinate() {
        return Parameters.startMinRange + (Parameters.startMaxRange - Parameters.startMinRange) * random.nextDouble();
    }

    private void generateVelocity(int velocitySize) {

        this.velocity = new ArrayList<>();

        for (int i = 0; i < velocitySize; i++) {
            velocity.add(Parameters.startVelocity);
        }
    }

    public void setCurrentPositionAsBestLocal() {
        bestPosition = List.copyOf(position);
    }

    public void moveParticle(Particle bestGlobalParticle) {

        List<Double> bestGlobalPosition = bestGlobalParticle.getBestPosition();

        for (int coordinate = 0; coordinate < position.size(); coordinate++) {

            accelerate(coordinate, bestGlobalPosition);
            double currentPosition = position.get(coordinate);
            position.set(coordinate, currentPosition + velocity.get(coordinate));
        }
    }

    private void accelerate(int coordinate, List<Double> bestGlobalPosition) {

        double currentVelocity = velocity.get(coordinate);

        velocity.set(coordinate, Parameters.inertiaWeight * currentVelocity
                + Parameters.localAcceleration * parameters.random1 * (bestPosition.get(coordinate) - position.get(coordinate))
                + Parameters.globalAcceleration * parameters.random2 * (bestGlobalPosition.get(coordinate) - position.get(coordinate))
        );
    }

    @Override
    public int compareTo(Particle particle) {
        return Double.compare(getBestPositionAssessment(), particle.getBestPositionAssessment());
    }

    static class Parameters {

        private final double random1;
        private final double random2;
        static double inertiaWeight;
        static double globalAcceleration;
        static double localAcceleration;
        static double startMinRange;
        static double startMaxRange;
        static double startVelocity;

        public Parameters(){
            random1 = random.nextDouble();
            random2 = random.nextDouble();
        }

        public static void setAll(double inertiaWeight, double globalAcceleration, double localAcceleration,
                                  double startMinRange, double startMaxRange, double startVelocity) {
            Parameters.inertiaWeight = inertiaWeight;
            Parameters.localAcceleration = localAcceleration;
            Parameters.globalAcceleration = globalAcceleration;
            Parameters.startMinRange = startMinRange;
            Parameters.startMaxRange = startMaxRange;
            Parameters.startVelocity = startVelocity;
        }
    }
}