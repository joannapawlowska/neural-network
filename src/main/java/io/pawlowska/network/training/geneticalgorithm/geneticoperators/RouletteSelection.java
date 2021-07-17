package io.pawlowska.network.training.geneticalgorithm.geneticoperators;

import io.pawlowska.network.training.geneticalgorithm.Individual;
import io.pawlowska.network.training.geneticalgorithm.Population;
import io.pawlowska.network.utils.WeightedCollection;

public class RouletteSelection extends Selection {

    private WeightedCollection<Individual> weightedIndividuals;
    private int selectionSize;

    @Override
    public Population select(Population population) {

        weightedIndividuals = new WeightedCollection<>();
        selectionSize = population.size() / 2;
        fillCollectionWithWeightedIndividuals(population);
        return randomlySelectWeightedIndividuals();
    }

    private void fillCollectionWithWeightedIndividuals(Population population) {

        double assessmentsSum = calculateAssessmentsSum(population);
        population.forEach(i -> weightedIndividuals.add(i.getAssessment() / assessmentsSum, i));
    }

    private double calculateAssessmentsSum(Population population) {

        return population.stream()
                .mapToDouble(Individual::getAssessment)
                .sum();
    }

    private Population randomlySelectWeightedIndividuals() {

        Population selectedPopulation = new Population();

        while (selectionSize-- > 0) {
            selectedPopulation.add(weightedIndividuals.next());
        }

        return selectedPopulation;
    }
}