package io.pawlowska.network.training.geneticalgorithm.geneticoperators;

import io.pawlowska.network.training.geneticalgorithm.Population;

import java.util.Random;

public abstract class Mutation {

    protected static final Random random = new Random();
    protected final double mutationProbability;

    public Mutation(double mutationProbability){
        this.mutationProbability = mutationProbability;
    }

    public abstract void mutate(Population evolutingPopulation);
}