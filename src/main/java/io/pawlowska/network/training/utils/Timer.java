package io.pawlowska.network.training.utils;

import java.time.Duration;
import java.time.Instant;

public class Timer {

    private Instant start;
    private Instant stop;

    public void start(){
        start = Instant.now();
    }

    public void stop(){
        stop = Instant.now();
    }

    public long getTimeElapsed(){
        return Duration.between(start, stop).toMillis();
    }
}