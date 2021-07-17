package io.pawlowska.network.training.geneticalgorithm.geneticoperators;

import io.pawlowska.network.training.geneticalgorithm.Individual;
import io.pawlowska.network.training.geneticalgorithm.Population;
import io.pawlowska.network.utils.Pair;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public abstract class Crossover {

    protected static final Random random = new Random();
    protected Population recombinedPopulation;
    private final double crossoverProbability;

    public Crossover(double crossoverProbability){
        this.crossoverProbability = crossoverProbability;
    }

    protected abstract Individual crossPair(Pair<Individual> pair);

    public void cross(Population evolvingPopulation){

        recombinedPopulation = new Population();

        while (shouldBeRecombined(evolvingPopulation)) {

            Pair<Individual> pair = randomlySelectPairFrom(evolvingPopulation);

            if (shouldSelectedPairBeRecombined()) {
                Individual individual = crossPair(pair);
                recombinedPopulation.add(individual);
            }
        }

        evolvingPopulation.clear();
        evolvingPopulation.addAll(recombinedPopulation);
    }

    protected boolean shouldBeRecombined(Population evolvingPopulation) {
        return recombinedPopulation.size() != evolvingPopulation.size();
    }

    protected Pair<Individual> randomlySelectPairFrom(List<Individual> individuals) {
        List<Individual> pair = individuals.stream()
                .unordered()
                .limit(2)
                .collect(Collectors.toList());

        return new Pair<>(pair.get(0), pair.get(1));
    }

    protected boolean shouldSelectedPairBeRecombined() {
        return random.nextDouble() < crossoverProbability;
    }
}