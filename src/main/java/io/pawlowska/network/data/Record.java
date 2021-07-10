package io.pawlowska.network.data;

import lombok.Getter;

@Getter
public class Record {

    double[] data;
    int[] mask;

    public Record(double[] data, int[] mask) {
        this.data = data;
        this.mask = mask;
    }
}