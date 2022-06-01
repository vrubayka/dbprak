package logging;

import javax.swing.text.html.parser.Entity;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadLog {

    private static List<ReadingError> errorList = new ArrayList<>();
    private static List<ReadingError> duplicates = new ArrayList<>();

    public static void addError(ReadingError error) {
        errorList.add(error);
    }

    public static void addDuplicate(ReadingError duplicate) {
        duplicates.add(duplicate);
    }

    public static void writeLogToCSV() {

        FileWriter fileWriter = getFileWriter("Log_Duplicates");
        writeLog(fileWriter, duplicates);

        fileWriter = getFileWriter("Log_Errors");
        writeLog(fileWriter, errorList);

    }

    private static void writeLog(FileWriter fileWriter, List<ReadingError> list) {

        try {
            fileWriter.write("Entity;Entity ID;Attribute;Message\n");
            for (ReadingError errEntry : list) {
                fileWriter.write(errEntry + "\n");
            }
            fileWriter.write("Errors: " + list.size());
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Something went wrong writing, when writing in log file.");
            e.printStackTrace();
        }
    }

    private static FileWriter getFileWriter(String fileName) {
        File file = new File("src/main/resources/logs/" + fileName + ".csv");
        String absolutepath  = file.getAbsolutePath();
        String path = file.getPath();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating log file.");
                throw new RuntimeException(e);
            }
        }

        try {
            FileWriter fileWriter = new FileWriter(file, false);
            return fileWriter;
        } catch (IOException e) {
            System.err.println("Error creating FileWriter with log file.");
            throw new RuntimeException(e);
        }
    }

}
