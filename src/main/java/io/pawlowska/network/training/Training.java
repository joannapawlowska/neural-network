package io.pawlowska.network.training;

import io.pawlowska.network.architecture.NeuralNetwork;
import io.pawlowska.network.utils.Timer;
import io.pawlowska.network.utils.TrainingResultCollector;

public abstract class Training {

    protected Timer timer;
    protected TrainingResultCollector trainingResultCollector;
    protected NeuralNetwork network;

    protected Training(){
        this.timer = new Timer();
        this.trainingResultCollector = new TrainingResultCollector();
    }

    public void perform(NeuralNetwork network){
        this.network = network;
        collectTrainingResultOnValidatingSet();
        collectResultsInfoLineForTrainingSet();
    }

    protected void collectTrainingResultOnValidatingSet(){

        collectResultsInfoLineForValidatingSet();
        double error = network.calculateErrorForValidatingSet();
        double accuracy = network.calculateAccuracyForValidatingSet();

        trainingResultCollector.addError(error);
        trainingResultCollector.addAccuracy(accuracy);
        trainingResultCollector.newLine();
    }

    protected void collectResultsInfoLineForValidatingSet(){
        trainingResultCollector.addText("error,accuracy - validating set");
        trainingResultCollector.newLine();
    }

    protected void collectResultsInfoLineForTrainingSet(){
        trainingResultCollector.addText("error,accuracy,time(ms) - training set");
        trainingResultCollector.newLine();
    }

    protected void collectTrainingResultOnTrainingSet(){

        long time = timer.getTimeElapsed();
        double error = network.calculateErrorForTrainingSet();
        double accuracy = network.calculateAccuracyForTrainingSet();

        trainingResultCollector.addError(error);
        trainingResultCollector.addAccuracy(accuracy);
        trainingResultCollector.addTime(time);
        trainingResultCollector.newLine();
    }
}