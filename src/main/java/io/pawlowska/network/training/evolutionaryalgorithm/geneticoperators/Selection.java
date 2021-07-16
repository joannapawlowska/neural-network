package io.pawlowska.network.training.evolutionaryalgorithm.geneticoperators;

import io.pawlowska.network.training.evolutionaryalgorithm.Individual;
import io.pawlowska.network.training.evolutionaryalgorithm.Population;

import java.util.List;

public interface Selection {

    List<Individual> select(Population population);
}