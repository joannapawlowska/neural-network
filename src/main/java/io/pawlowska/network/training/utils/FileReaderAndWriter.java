package io.pawlowska.network.training.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileReaderAndWriter {

    public static void writeToFile(String data, Path path) {

        try {
            Files.writeString(path, data);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static List<String> readFromFile(Path path) {

        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}