package io.pawlowska.network.training.evolutionaryalgorithm.geneticoperators;

import io.pawlowska.network.training.evolutionaryalgorithm.Individual;

import java.util.List;

public interface Recombination {

    List<Individual> recombine(List<Individual> individuals);
}