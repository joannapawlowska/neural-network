package io.pawlowska.network.training.geneticalgorithm.geneticoperators;

import io.pawlowska.network.training.geneticalgorithm.Individual;
import io.pawlowska.network.training.geneticalgorithm.Population;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

class SuccessionTest {

    Succession s = new ElitismSuccession();

    @Test
    public void shouldSortIndividualsByAssessment() {

        Population population = new Population();
        Individual individual1 = new Individual();
        Individual individual2 = new Individual();
        Individual individual3 = new Individual();

        individual1.setAssessment(0.1);
        individual2.setAssessment(0.3);
        individual3.setAssessment(0.9);

        Collections.shuffle(population);
        population.addAll(List.of(individual1, individual2, individual3));
        List<Individual> sorted = s.getBestIndividuals(population, 3);

        Assertions.assertAll(
                () -> Assertions.assertEquals(sorted.get(0).getAssessment(), 0.1),
                () -> Assertions.assertEquals(sorted.get(1).getAssessment(), 0.3),
                () -> Assertions.assertEquals(sorted.get(2).getAssessment(), 0.9)
        );
    }
}