package io.pawlowska.network.data;

import lombok.Getter;

@Getter
public class DataSet {

    Record [] dataSet;
    Record [] trainingSet;
    Record [] validatingSet;

}