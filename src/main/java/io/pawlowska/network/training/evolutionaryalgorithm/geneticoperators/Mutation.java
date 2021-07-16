package io.pawlowska.network.training.evolutionaryalgorithm.geneticoperators;

import io.pawlowska.network.training.evolutionaryalgorithm.Individual;

import java.util.List;

public interface Mutation {

    void mutate(List<Individual> individuals);
}
