package io.pawlowska.network.training.evolutionaryalgorithm;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Individual implements Comparable<Individual> {

    private static final Random random = new Random();
    @Getter private List<Double> genotype;
    @Getter @Setter private double assessment;

    public Individual() {
        genotype = new ArrayList<>();
    }

    @Builder
    public Individual(int genotypeSize, double geneStartMinRange, double geneStartMaxRange) {
        randomlyGenerateGenotype(genotypeSize, geneStartMinRange, geneStartMaxRange);
    }

    private void randomlyGenerateGenotype(int genotypeSize, double min, double max) {

        genotype = new ArrayList<>();

        for (int i = 0; i < genotypeSize; i++) {
            genotype.add(randomlyGenerateGene(min, max));
        }
    }

    private double randomlyGenerateGene(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }

    public void addGene(Double gene) {
        genotype.add(gene);
    }

    public void mutateGene(int gene, double mutation) {
        double value = genotype.get(gene);
        genotype.set(gene, value + mutation);
    }

    @Override
    public int compareTo(Individual individual) {
        return Double.compare(getAssessment(), individual.getAssessment());
    }
}