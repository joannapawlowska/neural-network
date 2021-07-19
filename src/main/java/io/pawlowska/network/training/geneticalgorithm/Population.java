package io.pawlowska.network.training.geneticalgorithm;

import java.util.ArrayList;

public class Population extends ArrayList<Individual> {

    public Individual getBestIndividual(){

        return this.stream()
                .sorted(Individual::compareTo)
                .findFirst()
                .get();
    }
}