package logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadLog {

    private static List<ReadingError> errorList = new ArrayList<>();

    public static void addError(ReadingError error) {
        errorList.add(error);
    }

    public static void printLog() {
        File file = new File("src/resources/logs/log.csv");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating log file.");
                throw new RuntimeException(e);
            }
        }

        try {
            FileWriter fileWriter = new FileWriter(file, false);
        } catch (IOException e) {
            System.err.println("Error creating FileWriter with log file.");
            throw new RuntimeException(e);
        }



    }

}
