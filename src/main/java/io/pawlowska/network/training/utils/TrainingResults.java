package io.pawlowska.network.training.utils;

import lombok.*;

@Getter
@AllArgsConstructor
public class TrainingResults {

    private double error;
    private double accuracy;
    private long time;
    private boolean pruned;

    public TrainingResults(double error, double accuracy){
        this.error = error;
        this.accuracy = accuracy;
    }
}