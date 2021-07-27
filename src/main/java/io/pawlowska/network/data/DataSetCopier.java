package io.pawlowska.network.data;

import java.util.Map;

public class DataSetCopier {

    private DataSet dataSet;
    private DataSet copied;

    public static DataSet copy(DataSet dataSet) {
        return new DataSetCopier().copyDataSet(dataSet);
    }

    private DataSet copyDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
        copied = new DataSet();
        copy();
        return copied;
    }

    private void copy() {
        copyTrainingSet();
        copyValidatingSet();
        copyMaskByCategory();
        copyFeatureNumber();
    }

    private void copyTrainingSet() {
        Record[] trainingSet = copySet(dataSet.getTrainingSet());
        copied.setTrainingSet(trainingSet);
    }

    private void copyValidatingSet() {
        Record[] validatingSet = copySet(dataSet.getValidatingSet());
        copied.setValidatingSet(validatingSet);
    }

    private Record[] copySet(Record[] set) {

        Record[] copiedSet = new Record[set.length];
        int i = 0;

        for (Record record : set) {
            copiedSet[i++] = new Record(record.getData().clone(), record.getMask().clone());
        }

        return copiedSet;
    }

    private void copyMaskByCategory() {
        copied.setMaskByCategory(
                Map.copyOf(dataSet.getMaskByCategory())
        );
    }

    private void copyFeatureNumber() {
        copied.setFeatureNumber(dataSet.getFeatureNumber());
    }
}