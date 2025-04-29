package com.example.demo.Model.SettingsManagement;

public class ProgressSession {
    private static ProgressDTO currentProgress;

    public static synchronized void setProgress(ProgressDTO progress) {
        currentProgress = progress;
    }

    public static synchronized ProgressDTO getCurrentProgress() {
        return currentProgress;
    }

    public static synchronized void clearProgress() {
        currentProgress = null;
    }

}
