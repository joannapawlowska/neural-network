package io.pawlowska.network.utils;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class WeightedCollection<T> {

    private NavigableMap<Double, T> map;
    private Random random;
    private double weightsSum;

    public WeightedCollection() {
        this.random = new Random();
        this.map = new TreeMap<>();
    }

    public void add(double weight, T object) {
        if (weight <= 0) return;
        weightsSum += weight;
        map.put(weightsSum, object);
    }

    public T next() {
        double value = random.nextDouble() * weightsSum;
        return map.ceilingEntry(value).getValue();
    }
}