package io.pawlowska.network.training.geneticalgorithm.geneticoperators;

import io.pawlowska.network.training.geneticalgorithm.Individual;
import io.pawlowska.network.utils.Pair;

import java.util.List;

public class AverageCrossover extends Crossover {

    public AverageCrossover(double crossoverProbability) {
        super(crossoverProbability);
    }

    @Override
    protected Individual crossPair(Pair<Individual> pair) {

        List<Double> genotype1 = pair.getFirst().getGenotype();
        List<Double> genotype2 = pair.getSecond().getGenotype();
        Individual recombinedIndividual = new Individual();

        for (int gene = 0; gene < genotype1.size(); gene++) {

            recombinedIndividual.addGene(
                    genotype1.get(gene) + random.nextDouble() * (genotype2.get(gene) - genotype1.get(gene))
            );
        }

        return recombinedIndividual;
    }
}