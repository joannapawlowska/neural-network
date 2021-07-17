package io.pawlowska.network.training.geneticalgorithm.geneticoperators;

import io.pawlowska.network.training.geneticalgorithm.Population;

public class PartialReplacementSuccession extends Succession {

    private double replacementRatio;

    public PartialReplacementSuccession(double replacementRatio) {
        this.replacementRatio = replacementRatio;
    }

    @Override
    public void determineNextPopulation(Population population, Population evolvingPopulation) {

        int size = population.size();
        int fromOldSize = (int) (size * replacementRatio);
        int fromEvolvingSize = size - fromOldSize;

        var oldIndividuals = getBestIndividuals(population, fromOldSize);
        var newIndividuals = getBestIndividuals(evolvingPopulation, fromEvolvingSize);

        population.clear();
        population.addAll(oldIndividuals);
        population.addAll(newIndividuals);
    }
}
