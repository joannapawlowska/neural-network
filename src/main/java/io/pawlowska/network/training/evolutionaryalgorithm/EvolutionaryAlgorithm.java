package io.pawlowska.network.training.evolutionaryalgorithm;

import io.pawlowska.network.training.HeuristicTraining;
import io.pawlowska.network.training.Training;
import io.pawlowska.network.training.evolutionaryalgorithm.geneticoperators.Mutation;
import io.pawlowska.network.training.evolutionaryalgorithm.geneticoperators.Recombination;
import io.pawlowska.network.training.evolutionaryalgorithm.geneticoperators.Selection;
import io.pawlowska.network.training.evolutionaryalgorithm.geneticoperators.Succession;
import lombok.Builder;

import java.util.List;

public class EvolutionaryAlgorithm extends Training implements HeuristicTraining {

    private int populationSize;
    private Population population;
    private Mutation mutation;
    private Selection selection;
    private Recombination recombination;
    private Succession succession;
    private double geneStartMinRange;
    private double geneStartMaxRange;

    @Builder
    private EvolutionaryAlgorithm(int epochs, int populationSize, Recombination recombination,
                                  Selection selection, Mutation mutation, Succession succession,
                                  int geneStartMinRange, int geneStartMaxRange) {
        super(epochs);
        this.populationSize = populationSize;
        this.selection = selection;
        this.recombination = recombination;
        this.mutation = mutation;
        this.succession = succession;
        this.geneStartMinRange = geneStartMinRange;
        this.geneStartMaxRange = geneStartMaxRange;
    }

    @Override
    protected void prebuild(){
        fillPopulationWithIndividuals(geneStartMinRange, geneStartMaxRange);
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

            Individual individual = Individual.builder()
                    .genotypeSize(genotypeSize)
                    .geneStartMinRange(geneStartMinRange)
                    .geneStartMaxRange(geneStartMaxRange)
                    .build();


            double assessment = calculateAssessment(individual);
            individual.setAssessment(assessment);
            population.add(individual);
        }
    }

    private double calculateAssessment(Individual individual) {

        network.fixConnectionsWeights(individual.getGenotype());
        return network.calculateErrorForTrainingSet();
    }

    @Override
    public void performOneEpochOfTraining() {

        List<Individual> individuals;

        timer.start();

        individuals = selection.select(population);
        individuals = recombination.recombine(individuals);
        mutation.mutate(individuals);

        assesIndividuals(individuals);
        population.addAll(individuals);
        succession.determineNextPopulation(population, populationSize);

        timer.stop();
    }

    private void assesIndividuals(List<Individual> population) {
        population.forEach(this::assesIndividual);
    }

    private void assesIndividual(Individual individual) {
        individual.setAssessment(calculateAssessment(individual));
    }

    @Override
    public void changeDimensionOfSolutions(List<Integer> toRemoveCoordinates) {

        toRemoveCoordinates.forEach(index ->
                population.forEach(individual -> individual.getGenotype()
                        .remove(index.intValue())
                )
        );
    }
}