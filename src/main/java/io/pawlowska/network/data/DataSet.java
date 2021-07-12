package io.pawlowska.network.data;

import io.pawlowska.network.exceptions.InvalidDataSetException;
import io.pawlowska.network.utils.FileReaderAndWriter;
import lombok.Getter;

import java.nio.file.Path;
import java.util.*;

@Getter
public class DataSet {

    private Record[] dataSet;
    private Record[] trainingSet;
    private Record[] validatingSet;
    private Map<String, int[]> maskByCategory;
    private int featureNumber;

    private DataSet(DataSetBuilder builder) {

        this.featureNumber = builder.features[0].length;
        createMasksByCategory(builder.categories);
        normalize(builder.features);
        createDataSet(builder);
        shuffleDataSet();
        splitToTrainingAndValidatingSet(builder.trainingDataRatioToTestData);
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

    private void createDataSet(DataSetBuilder builder) {

        double[][] features = builder.features;
        String[] labels = builder.categories;
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

    public static DataSetBuilder builder() {
        return new DataSetBuilder();
    }

    public static class DataSetBuilder {

        private double[][] features;
        private String[] categories;
        private double trainingDataRatioToTestData = 0.7;
        private Path path;

        public DataSetBuilder readFromFile(Path path) {
            this.path = path;
            return this;
        }

        public DataSetBuilder trainingDataRatioToTestData(double trainingDataRatioToTestData) {
            if (trainingDataRatioToTestData <= 0 || trainingDataRatioToTestData > 1) {
                throw new IllegalArgumentException("Ratio must be from range (0, 1)");
            }
            this.trainingDataRatioToTestData = trainingDataRatioToTestData;
            return this;
        }

        public DataSet build() {
            createFeatureAndCategoriesTables();
            return new DataSet(this);
        }


        private void createFeatureAndCategoriesTables() {

            List<String> lines = FileReaderAndWriter.readFromFile(path);
            String[] line = lines.get(0).split(",");

            features = new double[lines.size()][line.length - 1];
            categories = new String[lines.size()];

            fillFeatureAndCategoriesTables(lines);
        }

        private void fillFeatureAndCategoriesTables(List<String> lines) {

            String[] line;

            for (int i = 0; i < lines.size(); i++) {

                line = lines.get(i).split(",");

                for (int j = 0; j < line.length; j++) {

                    if (isCategory(line, j)) {
                        categories[i] = line[j];
                    } else {
                        features[i][j] = parseFeature(line[j]);
                    }
                }
            }
        }

        private boolean isCategory(String[] line, int i) {
            return i == line.length - 1;
        }

        private double parseFeature(String value) {

            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                throw new InvalidDataSetException("Could not parse data: " + value);
            }
        }
    }
}