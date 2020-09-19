package de.ebuchner.vocab.model.practice;

import de.ebuchner.vocab.config.preferences.StringMapValue;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class PracticeStatisticsValue extends StringMapValue {

    static final long EMPTY_TIMESTAMP = 0;
    private final static String REF_ID_KEY = "REF_ID";
    private final static String TIMESTAMP_KEY = "TIMESTAMP";
    private final static String USAGE_KEY = "USAGE";

    public String getRefId() {
        return getStringMap().get(REF_ID_KEY);
    }

    public void setRefId(String refId) {
        getStringMap().put(REF_ID_KEY, refId);
    }

    public long getTimestamp() {
        String timestampValue = getStringMap().get(TIMESTAMP_KEY);
        if (timestampValue == null)
            return EMPTY_TIMESTAMP;
        return Long.parseLong(timestampValue);
    }

    public void setTimestamp(long timestamp) {
        getStringMap().put(TIMESTAMP_KEY, String.valueOf(timestamp));
    }

    public int getUsageCount() {
        String usageCountValue = getStringMap().get(USAGE_KEY);
        if (usageCountValue == null)
            return 0;
        return Integer.parseInt(usageCountValue);
    }

    public void setUsageCount(int usageCount) {
        getStringMap().put(USAGE_KEY, String.valueOf(usageCount));
    }

    public void incrementUsage() {
        long lastTimestamp = getTimestamp();
        if (isToday(lastTimestamp))
            return;

        setUsageCount(getUsageCount() + 1);
        setTimestamp(System.currentTimeMillis());
    }

    private boolean isToday(long lastTimestamp) {
        if (lastTimestamp == EMPTY_TIMESTAMP)
            return false;

        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return lastTimestamp > calendar.getTimeInMillis();
    }

    public PracticeStatisticsValue createCopy() {
        PracticeStatisticsValue copy = new PracticeStatisticsValue();
        copy.setRefId(getRefId());
        copy.setTimestamp(getTimestamp());
        copy.setUsageCount(getUsageCount());
        return copy;
    }
}
