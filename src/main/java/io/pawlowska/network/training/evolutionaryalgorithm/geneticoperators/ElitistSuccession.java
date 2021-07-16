package io.pawlowska.network.training.evolutionaryalgorithm.geneticoperators;

import io.pawlowska.network.training.evolutionaryalgorithm.Individual;
import io.pawlowska.network.training.evolutionaryalgorithm.Population;

import java.util.List;
import java.util.stream.Collectors;

public class ElitistSuccession implements Succession {

    @Override
    public void determineNextPopulation(Population population, int size) {

        List<Individual> individuals = population.stream()
                .sorted(Individual::compareTo)
                .limit(size)
                .collect(Collectors.toList());

        population.clear();
        population.addAll(individuals);
    }
}