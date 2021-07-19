package io.pawlowska.network.data;

import io.pawlowska.network.exceptions.InvalidDataSetException;
import io.pawlowska.network.training.utils.FileReaderAndWriter;
import lombok.Getter;

import java.nio.file.Path;
import java.util.List;


public class DataSetBuilder {

    @Getter private double[][] features;
    @Getter private String[] categories;
    @Getter private double trainingDataRatioToTestData;
    @Getter private Path path;


    public DataSetBuilder readFromFile(Path path) {
        this.path = path;
        return this;
    }

    public DataSetBuilder trainingDataRatioToTestData(double trainingDataRatioToTestData) {
        this.trainingDataRatioToTestData = trainingDataRatioToTestData;
        return this;
    }

    public DataSet build() {
        validate();
        createFeatureAndCategoriesTables();
        return new DataSet(this);
    }

    private void validate() {

        validateTrainingDataRatioToTestData();
        validatePath();
    }

    private void validateTrainingDataRatioToTestData() {

        if (trainingDataRatioToTestData <= 0 || trainingDataRatioToTestData > 1) {
            throw new IllegalArgumentException("Ratio must be from range (0, 1)");
        }
    }

    private void validatePath() {

        if (path == null) {
            throw new NullPointerException("Path can not be null");
        }
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