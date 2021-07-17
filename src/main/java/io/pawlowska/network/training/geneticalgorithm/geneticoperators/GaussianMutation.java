package io.pawlowska.network.training.geneticalgorithm.geneticoperators;

import io.pawlowska.network.training.geneticalgorithm.Individual;
import io.pawlowska.network.training.geneticalgorithm.Population;

public class GaussianMutation extends Mutation {

    public GaussianMutation(double mutationProbability) {
        super(mutationProbability);
    }

    @Override
    public void mutate(Population evolvedPopulation) {

        for (Individual individual : evolvedPopulation) {

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