package io.pawlowska.network.training.evolutionaryalgorithm.geneticoperators;

import io.pawlowska.network.training.evolutionaryalgorithm.Individual;
import io.pawlowska.network.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class AverageRecombination implements Recombination {

    private static final Random random = new Random();
    private final double recombinationProbability;
    private List<Individual> recombinedIndividuals;
    private int iteration;

    public AverageRecombination(double recombinationProbability) {
        this.recombinationProbability = recombinationProbability;
    }

    @Override
    public List<Individual> recombine(List<Individual> individuals) {

        recombinedIndividuals = new ArrayList<>();
        iteration = 0;

        while (shouldBeRecombined(individuals)) {

            iteration++;
            Pair<Individual> pair = randomlySelectPairFrom(individuals);

            if (shouldSelectedPairBeRecombined()) {
                Individual individual = recombinePair(pair);
                recombinedIndividuals.add(individual);
            }
        }

        return recombinedIndividuals;
    }


    private boolean shouldBeRecombined(List<Individual> individuals) {
        return recombinedIndividuals.size() != individuals.size()
                && iteration < individuals.size() * 10;
    }

    private Pair<Individual> randomlySelectPairFrom(List<Individual> individuals) {
        List<Individual> pair = individuals.stream()
                .unordered()
                .limit(2)
                .collect(Collectors.toList());

        return new Pair<>(pair.get(0), pair.get(1));
    }

    private boolean shouldSelectedPairBeRecombined() {
        return random.nextDouble() < recombinationProbability;
    }

    private Individual recombinePair(Pair<Individual> pair) {

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