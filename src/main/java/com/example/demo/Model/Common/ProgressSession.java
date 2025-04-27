package com.example.demo.Model.Common;

public class ProgressSession {
    private static ProgressDTO currentProgress;

    private static ProgressRestoreDTO currentRestoreProgress;

    public static synchronized void setProgress(ProgressDTO progress) {
        currentProgress = progress;
    }

    public static synchronized void setRestoreProgress(ProgressRestoreDTO restoreProgress) {
        currentRestoreProgress = restoreProgress;
    }

    public static synchronized ProgressDTO getCurrentProgress() {
        return currentProgress;
    }

    public static synchronized ProgressRestoreDTO getCurrentRestoreProgress() {
        return currentRestoreProgress;
    }

    public static synchronized void clearProgress() {
        currentProgress = null;
    }
    public static synchronized void clearRestoreProgress() {
        currentRestoreProgress = null;
    }
}
