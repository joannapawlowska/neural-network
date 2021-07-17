package io.pawlowska.network.training.geneticalgorithm.geneticoperators;

import io.pawlowska.network.training.geneticalgorithm.Individual;
import io.pawlowska.network.training.geneticalgorithm.Population;

import java.util.List;

public class ElitismSuccession extends Succession {

    @Override
    public void determineNextPopulation(Population population, Population evolutingPopulation) {

        int size = population.size();
        population.addAll(evolutingPopulation);

        List<Individual> individuals = getBestIndividuals(population, size);

        population.clear();
        population.addAll(individuals);
    }
}