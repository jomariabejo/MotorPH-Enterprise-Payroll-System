package com.jomariabejo.motorph.utility;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CSVReaderUtility {
    /**
     * Reads all lines from a CSV file and returns them as a List of String arrays.
     *
     * @param filePath the path to the CSV file
     * @return a list of String arrays representing the CSV rows
     * @throws IOException  if an I/O error occurs
     * @throws CsvException if a CSV parsing error occurs
     */
    public static List<String[]> readAllLines(Path filePath) throws IOException, CsvException {
        List<String[]> records = new ArrayList<>();
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(filePath.toFile()))
                .withSkipLines(0)  // Skip header if needed
                .build()) {

            records = csvReader.readAll();
        }
        return records;
    }

    /**
     * Reads a CSV file line by line and returns them as a List of String arrays.
     *
     * @param filePath the path to the CSV file
     * @return a list of String arrays representing the CSV rows
     * @throws IOException  if an I/O error occurs
     * @throws CsvException if a CSV parsing error occurs
     */
    public static List<String[]> readLineByLine(Path filePath) throws IOException, CsvException {
        List<String[]> list = new ArrayList<>();
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(filePath.toFile()))
                .withSkipLines(0)  // Skip header if needed
                .build()) {

            String[] line;
            while ((line = csvReader.readNext()) != null) {
                list.add(line);
            }
        }
        return list;
    }

    /**
     * Checks if the CSV file exists at the given path.
     *
     * @param csvPath the path to the CSV file
     * @return true if the file exists, false otherwise
     */
    public static boolean checkIfExist(String csvPath) {
        Path path = Paths.get(csvPath);
        try {
            // Check if the file exists and is a regular file
            return Files.exists(path) && Files.isRegularFile(path);
        } catch (SecurityException e) {
            // Handle the case where the file cannot be accessed due to security restrictions
            System.err.println("Security exception: Unable to access the file.");
            return false;
        }
    }

    /**
     * Reads the header line of a CSV file and returns it as a List of Strings.
     *
     * @param filePath the path to the CSV file
     * @return a list of Strings representing the CSV headers
     * @throws IOException  if an I/O error occurs
     * @throws CsvException if a CSV parsing error occurs
     */
    public static ArrayList<String> readHeaders(Path filePath) throws IOException, CsvException {
        ArrayList<String> headers = new ArrayList<>();
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(filePath.toFile()))
                .withSkipLines(0)  // Read the header line
                .build()) {

            // Read the first line (header) if available
            String[] headerLine = csvReader.readNext();
            if (headerLine != null) {
                for (String header : headerLine) {
                    headers.add(header);
                }
            }
        }
        return headers;
    }

    // Default headers
    public static final ArrayList<String> DEFAULT_HEADERS = new ArrayList<>();

    static {
        String[] headersArray = {
        };

        for (String header : headersArray) {
            DEFAULT_HEADERS.add(header);
        }
    }
}
