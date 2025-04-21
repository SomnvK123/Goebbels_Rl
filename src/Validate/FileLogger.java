package Validate;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileLogger {
    private static final String TRANSACTION_LOG = "Transaction.txt";
    private static final String ERROR_LOG = "Error.txt";

    private static void logToFile(String fileName, String message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = LocalDateTime.now().format(formatter);
        String logMessage = "[" + timestamp + "] " + message;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(logMessage);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("[" + timestamp + "] Error writing log to: " + fileName + ":" + e.getMessage());
        }
    }

    public static void transactionLog(String message) {
        logToFile(TRANSACTION_LOG, message);
    }

    public static void errorLog(String message) {
        logToFile(ERROR_LOG, message);
    }
}
