package io.pawlowska.network.training;

import io.pawlowska.network.network.NeuralNetwork;
import io.pawlowska.network.training.pruning.Pruning;
import io.pawlowska.network.training.utils.FileReaderAndWriter;
import io.pawlowska.network.training.utils.Timer;
import io.pawlowska.network.training.utils.TrainingResultFormatter;
import io.pawlowska.network.training.utils.TrainingResults;

import java.nio.file.Path;

public abstract class Training {

    private final TrainingResultFormatter resultFormatter;
    protected final Timer timer;
    protected NeuralNetwork network;
    private Path writePath;
    private Pruning pruning;
    private int epochs;

    protected Training(TrainingBuilder<?> builder) {

        network = builder.getNeuralNetwork();
        writePath = builder.getWritePath();
        epochs = builder.getEpochs();
        pruning = builder.getPruning();
        timer = new Timer();
        resultFormatter = new TrainingResultFormatter();
    }

    public abstract void performOneEpochOfTraining();

    public void perform() {

        resultFormatter.addResultOnValidatingSet(calculateResultsValidatingSet());

        if (shouldPerformWithPruning()) {
            performWithPruning();
        } else {
            performWithoutPruning();
        }

        resultFormatter.addResultOnValidatingSet(calculateResultsValidatingSet());
        FileReaderAndWriter.writeToFile(resultFormatter.format(), writePath);
    }

    private TrainingResults calculateResultsValidatingSet() {

        double error = network.calculateErrorForValidatingSet();
        double accuracy = network.calculateAccuracyForValidatingSet();
        return new TrainingResults(error, accuracy);
    }

    private boolean shouldPerformWithPruning() {
        return pruning != null;
    }

    private TrainingResults calculateResultsTrainingSet() {

        double error = network.calculateErrorForTrainingSet();
        double accuracy = network.calculateAccuracyForTrainingSet();
        long time = timer.getTimeElapsed();
        boolean pruned = pruning != null && pruning.isPruned();
        return new TrainingResults(error, accuracy, time, pruned);
    }

    private void performWithoutPruning() {

        while (epochs-- > 0) {

            performOneEpochOfTraining();
            resultFormatter.addResultOnTrainingSet(calculateResultsTrainingSet());
        }
    }

    private void performWithPruning() {

        pruning.setNetwork(network);

        while (epochs-- > 0) {
            System.out.println(epochs);

            if (pruning.existNeuronAppropriateToPrune()) {
                pruning.prune();
            }

            performOneEpochOfTraining();

            if (pruning.shouldUndoPruning()) {
                pruning.undoPruning();
                performOneEpochOfTraining();

            } else if (pruning.isPruned() && isHeuristicTraining()) {
                var toRemove = pruning.getRemovedNeuronConnectionNumbers();
                ((HeuristicTraining) this).changeDimensionOfSolutions(toRemove);
            }

            resultFormatter.addResultOnTrainingSet(calculateResultsTrainingSet());
        }
    }

    private boolean isHeuristicTraining() {
        return this instanceof HeuristicTraining;
    }
}