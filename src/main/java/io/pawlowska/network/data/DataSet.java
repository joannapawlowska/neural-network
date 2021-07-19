package io.pawlowska.network.data;

import lombok.Getter;

import java.util.*;

@Getter
public class DataSet {

    private Record[] dataSet;
    private Record[] trainingSet;
    private Record[] validatingSet;
    private Map<String, int[]> maskByCategory;
    private int featureNumber;

    public DataSet(DataSetBuilder builder) {

        featureNumber = builder.getFeatures()[0].length;
        createMasksByCategory(builder.getCategories());
        normalize(builder.getFeatures());
        createDataSet(builder.getCategories(), builder.getFeatures());
        shuffleDataSet();
        splitToTrainingAndValidatingSet(builder.getTrainingDataRatioToTestData());
    }

    public static DataSetBuilder builder() {
        return new DataSetBuilder();
    }

    private void createMasksByCategory(String[] labels) {

        Set<String> labelsSet = new HashSet<>(Arrays.asList(labels));
        int maskLength = labelsSet.size();
        maskByCategory = new HashMap<>();

        for (String label : labelsSet) {

            int[] mask = new int[maskLength];
            int bit = maskByCategory.size();
            mask[bit] = 1;
            maskByCategory.putIfAbsent(label, mask);
        }
    }

    private void normalize(double[][] features) {

        int columnAmount = features[0].length;

        for (int feature = 0; feature < columnAmount; feature++) {

            double min = findMinValueFormFeatureColumn(feature, features);
            double max = findMaxValueFormFeatureColumn(feature, features);
            normalizeFeatureColumn(min, max, features, feature);
        }
    }

    private double findMinValueFormFeatureColumn(int feature, double[][] features) {

        double min = features[0][feature];

        for (int i = 1; i < features.length; i++) {

            if (min > features[i][feature]) {
                min = features[i][feature];
            }
        }
        return min;
    }

    private double findMaxValueFormFeatureColumn(int feature, double[][] features) {

        double max = features[0][feature];

        for (int i = 1; i < features.length; i++) {

            if (max < features[i][feature]) {
                max = features[i][feature];
            }
        }
        return max;
    }

    private void normalizeFeatureColumn(double min, double max, double[][] features, int feature) {

        int maxRange = 1;
        int minRange = 0;

        for (int i = 0; i < features.length; i++) {
            features[i][feature] = (features[i][feature] - min) / (max - min) * (maxRange - minRange) + minRange;
        }
    }

    private void createDataSet(String[] labels, double[][] features) {

        dataSet = new Record[features.length];

        for (int i = 0; i < features.length; i++) {
            dataSet[i] = new Record(features[i], maskByCategory.get(labels[i]));
        }
    }

    private void shuffleDataSet() {

        List<Record> records = Arrays.asList(dataSet);
        Collections.shuffle(records);
        records.toArray(dataSet);
    }

    private void splitToTrainingAndValidatingSet(double trainingDataRatioToTestData) {

        int trainAmount = (int) (dataSet.length * trainingDataRatioToTestData);
        trainingSet = Arrays.copyOfRange(dataSet, 0, trainAmount);
        validatingSet = Arrays.copyOfRange(dataSet, trainAmount, dataSet.length);
    }
}