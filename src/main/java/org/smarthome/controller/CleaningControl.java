package org.smarthome.controller;

import org.smarthome.domain.cleaning.Vacuum;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CleaningControl {

    private final Vacuum vacuum;
    private ExecutorService cleaningExecutor;

    public CleaningControl(Vacuum vacuum) {
        this.vacuum = vacuum;
        this.cleaningExecutor = Executors.newCachedThreadPool();
    }

    public void startCleaning() {
        if (vacuum != null) {
            cleaningExecutor.submit(() -> {
                try {
                    vacuum.clean();
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }

    public void stopCleaning() {
        if (vacuum != null) {
            if (vacuum.isCleaning()) {
                shutdownAndReset();
            }

            cleaningExecutor.submit(() -> {
                try {
                    vacuum.stop();
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }

    private void shutdownAndReset() {
        if (!cleaningExecutor.isShutdown()) {
            cleaningExecutor.shutdownNow();
            cleaningExecutor = Executors.newCachedThreadPool();
        }
    }

}
