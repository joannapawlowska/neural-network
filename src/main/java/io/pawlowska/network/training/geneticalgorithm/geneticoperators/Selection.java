package io.pawlowska.network.training.geneticalgorithm.geneticoperators;

import io.pawlowska.network.training.geneticalgorithm.Population;

public abstract class Selection {

    public abstract Population select(Population population);
}