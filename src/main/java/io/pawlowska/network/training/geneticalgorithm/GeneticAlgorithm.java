package io.pawlowska.network.training.geneticalgorithm;

import io.pawlowska.network.training.HeuristicTraining;
import io.pawlowska.network.training.Training;
import io.pawlowska.network.training.geneticalgorithm.geneticoperators.Crossover;
import io.pawlowska.network.training.geneticalgorithm.geneticoperators.Mutation;
import io.pawlowska.network.training.geneticalgorithm.geneticoperators.Selection;
import io.pawlowska.network.training.geneticalgorithm.geneticoperators.Succession;

import java.util.Collections;
import java.util.List;

public class GeneticAlgorithm extends Training implements HeuristicTraining {

    private final int populationSize;
    private final Mutation mutation;
    private final Selection selection;
    private final Crossover crossover;
    private final Succession succession;
    private Population population;

    public GeneticAlgorithm(GeneticAlgorithmBuilder builder) {

        super(builder);
        this.populationSize = builder.getPopulationSize();
        this.selection = builder.getSelection();
        this.crossover = builder.getCrossover();
        this.mutation = builder.getMutation();
        this.succession = builder.getSuccession();

        fillPopulationWithIndividuals(builder.getGeneStartMinRange(), builder.getGeneStartMaxRange());
    }

    private void fillPopulationWithIndividuals(double geneStartMinRange, double geneStartMaxRange) {

        this.population = new Population();
        int genotypeSize = calculateGenotypeSize();
        fillPopulationWithIndividuals(genotypeSize, geneStartMinRange, geneStartMaxRange);
    }

    private int calculateGenotypeSize() {
        return network.getConnectionsAmount();
    }

    private void fillPopulationWithIndividuals(int genotypeSize, double geneStartMinRange, double geneStartMaxRange) {

        for (int i = 0; i < populationSize; i++) {

            Individual individual = new Individual(genotypeSize, geneStartMinRange, geneStartMaxRange);
            individual.setAssessment(calculateAssessment(individual));
            population.add(individual);
        }
    }

    private double calculateAssessment(Individual individual) {

        network.fixConnectionsWeights(individual.getGenotype());
        return network.calculateErrorForTrainingSet();
    }

    @Override
    public void performOneEpochOfTraining() {

        timer.start();

        var evolutingPopulation = selection.select(population);
        crossover.cross(evolutingPopulation);
        mutation.mutate(evolutingPopulation);
        assesPopulation(evolutingPopulation);
        succession.determineNextPopulation(population, evolutingPopulation);

        timer.stop();

        network.fixConnectionsWeights(population.getBestIndividual().getGenotype());
    }

    private void assesPopulation(Population population) {
        population.forEach(this::assesIndividual);
    }

    private void assesIndividual(Individual individual) {
        individual.setAssessment(calculateAssessment(individual));
    }

    @Override
    public void changeDimensionOfSolutions(List<Integer> toRemoveCoordinates) {

        toRemoveCoordinates.stream()
                .sorted(Collections.reverseOrder())
                .forEach(index ->
                        population.forEach(individual -> individual.getGenotype()
                                .remove(index.intValue())
                        )
                );
    }
}