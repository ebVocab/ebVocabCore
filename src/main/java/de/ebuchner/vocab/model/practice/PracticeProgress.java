package de.ebuchner.vocab.model.practice;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PracticeProgress {

    public static final PracticeProgress EMPTY = new PracticeProgress();
    private boolean empty;
    private int numberOfLessons;
    private int totalNumberOfEntries;
    private String ratioCurrentOfTotalText;
    private double ratioCurrentOfTotal;
    private int currentPosition;
    private boolean repetitionMode;
    private int usageCount;
    private long lastUsage;
    private String lastUsageText;
    private boolean showUsage;

    public PracticeProgress() {
        empty = true;
    }

    public PracticeProgress(
            int numberOfLessons,
            int totalNumberOfEntries,
            int currentPosition, // 0-based
            boolean repetitionMode,
            int usageCount,
            long lastUsage,
            boolean showUsage
    ) {
        this.repetitionMode = repetitionMode;

        if (totalNumberOfEntries < 0)
            throw new IllegalArgumentException("totalNumberOfEntries must be ge 0");

        if (currentPosition < 0 || currentPosition >= totalNumberOfEntries)
            throw new IllegalArgumentException("currentPosition must be in range 0.." +
                    (totalNumberOfEntries - 1) + ", found " + currentPosition);

        if (numberOfLessons <= 0)
            throw new IllegalArgumentException("numberOfLessons must be ge 0");

        this.numberOfLessons = numberOfLessons;
        this.totalNumberOfEntries = totalNumberOfEntries;
        this.currentPosition = currentPosition;

        this.ratioCurrentOfTotal = (double) (currentPosition + 1) / (double) totalNumberOfEntries;
        NumberFormat ratioFormatter = new DecimalFormat("#0.0%");
        ratioCurrentOfTotalText = ratioFormatter.format(ratioCurrentOfTotal);

        this.showUsage = showUsage;
        this.usageCount = usageCount;
        this.lastUsage = lastUsage;
        if (this.showUsage) {
            SimpleDateFormat usageDateFormatter = new SimpleDateFormat("dd.MM.yy");
            this.lastUsageText = usageDateFormatter.format(new Date(lastUsage));
        }
    }

    public boolean isEmpty() {
        return empty;
    }

    public int getNumberOfLessons() {
        return numberOfLessons;
    }

    public int getTotalNumberOfEntries() {
        return totalNumberOfEntries;
    }

    public String getRatioCurrentOfTotalText() {
        return ratioCurrentOfTotalText;
    }

    public double getRatioCurrentOfTotal() {
        return ratioCurrentOfTotal;
    }

    // 0-based
    public int getCurrentPosition() {
        return currentPosition;
    }

    public boolean isRepetitionMode() {
        return repetitionMode;
    }

    public int getUsageCount() {
        return usageCount;
    }

    public long getLastUsage() {
        return lastUsage;
    }

    public String getLastUsageText() {
        return lastUsageText;
    }

    public boolean isShowUsage() {
        return showUsage;
    }
}
