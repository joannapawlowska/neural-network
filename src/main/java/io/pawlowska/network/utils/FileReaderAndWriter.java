package io.pawlowska.network.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileReaderAndWriter {

    public static void writeToFile(String data) {

    }

    public static List<String> readFromFile(Path path) {

        try{
            return Files.readAllLines(path);
        }catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}