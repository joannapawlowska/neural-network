package io.pawlowska.network.utils;

import lombok.Getter;

public class TrainingResultCollector {

    @Getter
    private String results = "";

    public void addError(double error) {
        results += error + ",";
    }

    public void addAccuracy(double accuracy) {
        results += accuracy + ",";
    }

    public void addTime(long time) {
        results += time + ",";
    }

    public void addWeatherPruned(boolean pruned) {
        results += pruned + ",";
    }

    public void addText(String text) {
        results += text;
    }

    public void newLine() {
        results += "\n";
    }
}