package com.jomariabejo.motorph.utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TextReader {

    public static String readTextFile(String filePath) {
        StringBuilder content = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }
}
