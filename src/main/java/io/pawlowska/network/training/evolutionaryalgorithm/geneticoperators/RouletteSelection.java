package io.pawlowska.network.training.evolutionaryalgorithm.geneticoperators;

import io.pawlowska.network.training.evolutionaryalgorithm.Individual;
import io.pawlowska.network.training.evolutionaryalgorithm.Population;
import io.pawlowska.network.utils.WeightedCollection;

import java.util.ArrayList;
import java.util.List;

public class RouletteSelection implements Selection {

    private WeightedCollection<Individual> weightedIndividuals;
    private int selectionSize;

    @Override
    public List<Individual> select(Population population) {

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

    private List<Individual> randomlySelectWeightedIndividuals() {

        List<Individual> selectedIndividuals = new ArrayList<>();

        while (selectionSize-- > 0) {
            selectedIndividuals.add(weightedIndividuals.next());
        }

        return selectedIndividuals;
    }
}