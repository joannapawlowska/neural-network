package io.pawlowska.network.training.geneticalgorithm.geneticoperators;

import io.pawlowska.network.training.geneticalgorithm.Individual;
import io.pawlowska.network.utils.Pair;

import java.util.List;

public class UniformCrossover extends Crossover {

    public UniformCrossover(double crossoverProbability) {
        super(crossoverProbability);
    }

    @Override
    protected Individual crossPair(Pair<Individual> pair) {

        List<Double> genotype1 = pair.getFirst().getGenotype();
        List<Double> genotype2 = pair.getSecond().getGenotype();
        Individual recombinedIndividual = new Individual();
        List<Double> genotype;

        for (int gene = 0; gene < genotype1.size(); gene++) {

            genotype = randomlySelectGenotype(genotype1, genotype2);
            recombinedIndividual.addGene(genotype.get(gene));
        }

        return recombinedIndividual;
    }

    private List<Double> randomlySelectGenotype(List<Double> genotype1, List<Double> genotype2) {
        return random.nextDouble() >= 0.5 ? genotype1 : genotype2;
    }
}