package io.pawlowska.network.training.geneticalgorithm.geneticoperators;

import io.pawlowska.network.training.geneticalgorithm.Individual;
import io.pawlowska.network.training.geneticalgorithm.Population;
import io.pawlowska.network.training.utils.Pair;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TournamentSelection extends Selection {

    @Override
    public Population select(Population population) {

        int populationSize = population.size();
        Population selectedPopulation = new Population();

        while(populationSize-- > 0){

            Individual individual = performTournament(population);
            selectedPopulation.add(individual);
        }

        return selectedPopulation;
    }

    private Individual performTournament(Population population){

        Pair<Individual> pair = randomlySelectPairFrom(population);
        return selectBetterIndividual(pair);
    }

    private Pair<Individual> randomlySelectPairFrom(Population population) {

        Collections.shuffle(population);
        List<Individual> pair = population.stream()
                .limit(2)
                .collect(Collectors.toList());

        return new Pair<>(pair.get(0), pair.get(1));
    }

    private Individual selectBetterIndividual(Pair<Individual> pair){

        Individual first = pair.getFirst();
        Individual second = pair.getSecond();
        return first.compareTo(second) > 0 ? first : second;
    }
}
