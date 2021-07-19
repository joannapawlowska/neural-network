package io.pawlowska.network.training.utils;

import lombok.Getter;

import java.util.Arrays;

public class TrainingResultFormatter {

    private String results = "";
    @Getter private String formattedResults = "";

    private String addError(double error) {
        return error + ",";
    }

    private String addAccuracy(double accuracy) {
        return accuracy + ",";
    }

    private String addTime(long time) {
        return time + ",";
    }

    private String addWeatherPruned(boolean pruned) {
        return pruned + ",";
    }

    private String addText(String text) {
        return text;
    }

    private String newLine() {
        return "\n";
    }

    public void addResultOnTrainingSet(TrainingResults results) {
        this.results += addError(results.getError());
        this.results += addAccuracy(results.getAccuracy());
        this.results+= addTime(results.getTime());
        this.results += addWeatherPruned(results.isPruned());
        this.results += newLine();
    }

    public void addResultOnValidatingSet(TrainingResults results) {
        this.results += addError(results.getError());
        this.results += addAccuracy(results.getAccuracy());
        this.results += newLine();
    }

    public String format(){

        String [] split = results.split("\n");
        formatResultsValidatingSet(split[0]);
        formatResultsTrainingSet(split);
        formatResultsValidatingSet(split[split.length - 1]);

        return formattedResults;
    }

    private void formatResultsValidatingSet(String result) {
        formattedResults += addText("error, accuracy - validating set");
        formattedResults += newLine();
        formattedResults += addText(result);
        formattedResults += newLine();
    }

    private void formatResultsTrainingSet(String [] results) {
        formattedResults += addText("error, accuracy, time(ms), pruned - training set");
        formattedResults += newLine();
        formattedResults += addText(String.join("\n", Arrays.copyOfRange(results, 1, results.length - 1)));
        formattedResults += newLine();
    }
}