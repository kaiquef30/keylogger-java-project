package org.apollyon.capture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ScreenshotCapture implements CaptureInterface {

    public static final String SCREENSHOT_DIR = "screenshots/";

    public static final long SCREENSHOT_INTERVAL_MS = 10000; // 10 seconds

    private final Robot robot;

    public ScreenshotCapture() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException("Error initializing Robot", e);
        }
    }


    @Override
    public void capture() {

        try {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle screenRect = new Rectangle(screenSize);
            BufferedImage capture = robot.createScreenCapture(screenRect);

            File screenshotDir = new File(SCREENSHOT_DIR);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdir();
            }

            String screenshotPath = SCREENSHOT_DIR + "screenshot_" + System.currentTimeMillis() + ".png";
            var screenshotFile = new File(screenshotPath);
            ImageIO.write(capture, "png", screenshotFile);

            System.out.println("Screenshot captured: " + screenshotPath);
        } catch (IOException e) {
            throw new RuntimeException("Error capturing screenshot", e);
        }
    }
}
