package io.pawlowska.network.training.heuristic;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Random;

public class Particle {

    private static final Random random = new Random();

    private double inertiaWeight;
    private double globalAcceleration;
    private double localAcceleration;
    private double rangeMin;
    private double rangeMax;

    @Getter
    private double[] bestLocalPosition;
    @Getter
    private double[] position;
    private double[] velocity;
    private double random1;
    private double random2;

    @Setter
    @Getter
    private double bestLocalPositionValue;

    private Particle(int length, double startVelocity, double inertiaWeight,
                     double globalAcceleration, double localAcceleration,
                     double rangeMin, double rangeMax) {

        this.velocity = new double[length];
        this.position = new double[length];
        this.bestLocalPosition = new double[length];
        this.inertiaWeight = inertiaWeight;
        this.globalAcceleration = globalAcceleration;
        this.localAcceleration = localAcceleration;
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
        this.random1 = random.nextDouble();
        this.random2 = random.nextDouble();
        randomlyGeneratePosition();
        setParticleVelocity(startVelocity);
        setCurrentPositionAsBestLocal();
    }

    private void randomlyGeneratePosition() {

        for (int i = 0; i < position.length; i++) {
            position[i] = generateRandomlyCoordinateValue();
        }
    }

    private double generateRandomlyCoordinateValue() {
        return rangeMin + (rangeMax - rangeMin) * random.nextDouble();
    }

    private void setParticleVelocity(double startVelocity) {
        Arrays.fill(position, startVelocity);
    }

    public void setCurrentPositionAsBestLocal() {
        System.arraycopy(position, 0, bestLocalPosition, 0, position.length);
    }

    public void moveParticle(Particle bestGlobalParticle) {

        double[] bestGlobalVector = bestGlobalParticle.getBestLocalPosition();

        for (int i = 0; i < position.length; i++) {

            velocity[i] = inertiaWeight * velocity[i]
                    + localAcceleration * random1 * (bestLocalPosition[i] - position[i])
                    + globalAcceleration * random2 * (bestGlobalVector[i] - position[i]);

            position[i] += velocity[i];
        }
    }

    public static ParticleBuilder builder(int length) {
        return new ParticleBuilder(length);
    }

    public static class ParticleBuilder {

        private final int length;
        private double inertiaWeight = 0.5;
        private double rangeMin = -1;
        private double rangeMax = 1;
        private double startVelocity = 5;
        private double globalAcceleration = 0.7;
        private double localAcceleration = 0.3;

        public ParticleBuilder(int length) {
            this.length = length;
        }

        public ParticleBuilder inertiaWeight(double inertiaWeight) {
            this.inertiaWeight = inertiaWeight;
            return this;
        }

        public ParticleBuilder startVelocity(double startVelocity) {
            this.startVelocity = startVelocity;
            return this;
        }

        public ParticleBuilder rangeMin(double rangeMin) {
            this.rangeMin = rangeMin;
            return this;
        }

        public ParticleBuilder rangeMax(double rangeMax) {
            this.rangeMax = rangeMax;
            return this;
        }

        public ParticleBuilder localAcceleration(double localAcceleration) {
            this.localAcceleration = localAcceleration;
            return this;
        }

        public ParticleBuilder globalAcceleration(double globalAcceleration) {
            this.globalAcceleration = globalAcceleration;
            return this;
        }

        public Particle build() {

            return new Particle(
                    length, startVelocity, inertiaWeight,
                    globalAcceleration, localAcceleration,
                    rangeMin, rangeMax);
        }
    }
}