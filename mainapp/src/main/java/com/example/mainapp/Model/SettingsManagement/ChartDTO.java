package com.example.mainapp.Model.SettingsManagement;

import java.time.LocalDate;

public class ChartDTO {

    private LocalDate date;

    public int getBackupCount() {
        return backupCount;
    }

    public void setBackupCount(int backupCount) {
        this.backupCount = backupCount;
    }

    public int getRestoreCount() {
        return restoreCount;
    }

    public void setRestoreCount(int restoreCount) {
        this.restoreCount = restoreCount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    private int backupCount;
    private int restoreCount;

    public ChartDTO(LocalDate date, int backupCount, int restoreCount) {
        this.date = date;
        this.backupCount = backupCount;
        this.restoreCount = restoreCount;
    }

}
