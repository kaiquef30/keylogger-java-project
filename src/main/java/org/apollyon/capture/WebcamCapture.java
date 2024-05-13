package org.apollyon.capture;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WebcamCapture implements CaptureInterface {

    public static final String WEBCAM_CAPTURE_DIR = "webcam/";

    public static final long WEBCAM_INTERVAL_MS = 10000; // 10 seconds

    private final Webcam webcam;

    private final Logger logger = LoggerFactory.getLogger(WebcamCapture.class);

    public WebcamCapture() {
        webcam = Webcam.getDefault();
        if (webcam != null) {
            webcam.setViewSize(WebcamResolution.VGA.getSize());
        } else {
            throw new RuntimeException("No webcam found");
        }
    }

    @Override
    public void capture() {
        webcam.open();

        BufferedImage image = webcam.getImage();

        var webcamDir = new File(WEBCAM_CAPTURE_DIR);
        if (!webcamDir.exists()) {
            webcamDir.mkdir();
        }

        String webcamPath = WEBCAM_CAPTURE_DIR + "webcam_" + System.currentTimeMillis() + ".png";
        File webcamFile = new File(webcamPath);

        try {
            ImageIO.write(image, "png", webcamFile);
            logger.info("Webcam image captured: {}", webcamPath);
        } catch (IOException e) {
            logger.error("Error capturing webcam image", e);
        }

        webcam.close();
    }
}