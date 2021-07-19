package io.pawlowska.network.training;

import io.pawlowska.network.network.NeuralNetwork;
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
    protected boolean pruned;
    private int epochs;

    protected Training(TrainingBuilder<?> builder){

        pruned = false;
        network = builder.getNeuralNetwork();
        writePath = builder.getWritePath();
        epochs = builder.getEpochs();
        timer = new Timer();
        resultFormatter = new TrainingResultFormatter();
    }

    public abstract void performOneEpochOfTraining();

    public void perform() {

        resultFormatter.addResultOnValidatingSet(calculateResultsValidatingSet());

        while (epochs-- > 0) {

            performOneEpochOfTraining();
            resultFormatter.addResultOnTrainingSet(calculateResultsTrainingSet());
        }

        resultFormatter.addResultOnValidatingSet(calculateResultsValidatingSet());
        FileReaderAndWriter.writeToFile(resultFormatter.format(), writePath);
    }

    private TrainingResults calculateResultsTrainingSet() {

        double error = network.calculateErrorForTrainingSet();
        double accuracy = network.calculateAccuracyForTrainingSet();
        long time = timer.getTimeElapsed();
        return new TrainingResults(error, accuracy, time, pruned);
    }

    private TrainingResults calculateResultsValidatingSet() {

        double error = network.calculateErrorForValidatingSet();
        double accuracy = network.calculateAccuracyForValidatingSet();
        return new TrainingResults(error, accuracy);
    }
}