package utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    protected static final Logger logger = LogManager.getLogger();

    public static String getCsvData(String filePath, int columnNumber, int rowNumber) {
        String column;
        List<String> list = new ArrayList<>();
        try {
            Reader in = new FileReader(filePath);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
            for (CSVRecord record : records) {
                column = record.get(columnNumber);
                list.add(column);
            }
        } catch (IOException e) {
            logger.error("Cannot parse file");
        }
        return list.get(rowNumber);
    }
}
