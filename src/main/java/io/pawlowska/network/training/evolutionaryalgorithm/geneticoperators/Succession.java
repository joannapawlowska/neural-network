package io.pawlowska.network.training.evolutionaryalgorithm.geneticoperators;

import io.pawlowska.network.training.evolutionaryalgorithm.Population;

public interface Succession {

    void determineNextPopulation(Population population, int size);
}