package io.pawlowska.network.training.geneticalgorithm.geneticoperators;

import io.pawlowska.network.training.geneticalgorithm.Individual;
import io.pawlowska.network.training.geneticalgorithm.Population;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Succession {

    public abstract void determineNextPopulation(Population population, Population evolutingPopulation);

    protected List<Individual> getBestIndividuals(Population population, int size) {

        return population.stream()
                .sorted(Individual::compareTo)
                .limit(size)
                .collect(Collectors.toList());
    }
}