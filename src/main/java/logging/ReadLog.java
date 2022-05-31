package logging;

import java.util.ArrayList;
import java.util.List;

public class ReadLog {

    private static List<ReadingError> errorList = new ArrayList<>();

    public static void addError(ReadingError error) {
        errorList.add(error);
    }

}
