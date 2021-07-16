package io.pawlowska.network.training.evolutionaryalgorithm.geneticoperators;

import io.pawlowska.network.training.evolutionaryalgorithm.Individual;

import java.util.List;
import java.util.Random;

public class GaussianMutation implements Mutation {

    private static final Random random = new Random();
    private final double mutationProbability;

    public GaussianMutation(double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    @Override
    public void mutate(List<Individual> individuals) {

        for (Individual individual : individuals) {

            if (random.nextDouble() < mutationProbability) {
                mutateIndividual(individual);
            }
        }
    }

    private void mutateIndividual(Individual individual) {

        int genotypeSize = individual.getGenotype().size();

        for (int gene = 0; gene < genotypeSize; gene++) {
            individual.mutateGene(gene, random.nextGaussian());
        }
    }
}