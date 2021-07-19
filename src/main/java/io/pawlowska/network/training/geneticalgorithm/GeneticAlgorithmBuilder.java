package io.pawlowska.network.training.geneticalgorithm;

import io.pawlowska.network.training.TrainingBuilder;
import io.pawlowska.network.training.geneticalgorithm.geneticoperators.Crossover;
import io.pawlowska.network.training.geneticalgorithm.geneticoperators.Mutation;
import io.pawlowska.network.training.geneticalgorithm.geneticoperators.Selection;
import io.pawlowska.network.training.geneticalgorithm.geneticoperators.Succession;
import lombok.Getter;

@Getter
public class GeneticAlgorithmBuilder extends TrainingBuilder<GeneticAlgorithmBuilder> {

    private int populationSize;
    private Mutation mutation;
    private Selection selection;
    private Crossover crossover;
    private Succession succession;
    private double geneStartMinRange;
    private double geneStartMaxRange;

    public GeneticAlgorithmBuilder self() {
        return this;
    }

    public GeneticAlgorithmBuilder populationSize(int populationSize) {
        this.populationSize = populationSize;
        return this;
    }

    public GeneticAlgorithmBuilder mutation(Mutation mutation) {
        this.mutation = mutation;
        return this;
    }

    public GeneticAlgorithmBuilder selection(Selection selection) {
        this.selection = selection;
        return this;
    }

    public GeneticAlgorithmBuilder crossover(Crossover crossover) {
        this.crossover = crossover;
        return this;
    }

    public GeneticAlgorithmBuilder succession(Succession succession) {
        this.succession = succession;
        return this;
    }

    public GeneticAlgorithmBuilder geneStartMinRange(double geneStartMinRange) {
        this.geneStartMinRange = geneStartMinRange;
        return this;
    }

    public GeneticAlgorithmBuilder geneStartMaxRange(double geneStartMaxRange) {
        this.geneStartMaxRange = geneStartMaxRange;
        return this;
    }

    @Override
    public GeneticAlgorithm build() {
        validate();
        return new GeneticAlgorithm(this);
    }

    @Override
    protected void validate() {
        super.validate();
        validateGeneRange();
        populationSize();
        validateSelection();
        validateMutation();
        validateCrossover();
        validateSuccession();
    }

    private void validateGeneRange() {
        if (geneStartMinRange > geneStartMaxRange) {
            throw new IllegalArgumentException("Gene min range can not be bigger than gene max range");
        }
    }

    private void populationSize() {
        if (populationSize < 2) {
            throw new IllegalArgumentException("Population size can not be less than 2");
        }
    }

    private void validateSelection() {
        if (selection == null) {
            throw new NullPointerException("Selection can not be null");
        }
    }

    private void validateMutation() {
        if (mutation == null) {
            throw new NullPointerException("Mutation can not be null");
        }
    }

    private void validateSuccession() {
        if (succession == null) {
            throw new NullPointerException("Succession can not be null");
        }
    }

    private void validateCrossover() {
        if (crossover == null) {
            throw new NullPointerException("Crossover can not be null");
        }
    }
}