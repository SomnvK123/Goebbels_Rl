package Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class Filelog {
    private static final String TRANSACTION_LOG = "transaction.txt";
    private static final String ERROR_LOG = "error.txt";

    public static void logTransaction(String message) {
        logToFile(TRANSACTION_LOG, message);
    }

    public static void logError(String message) {
        logToFile(ERROR_LOG, message);
    }

    private static void logToFile(String fileName, String message) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)))) {
            out.println(LocalDateTime.now() + ": " + message);
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + fileName);
            e.printStackTrace();
        }
    }
}
