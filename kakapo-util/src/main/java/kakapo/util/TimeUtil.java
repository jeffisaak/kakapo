package kakapo.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class TimeUtil {

    /**
     * Obtain the current GMT timestamp in milliseconds since the epoch.
     *
     * @return The current GMT timestamp in milliseconds since the epoch.
     */
    public static long timestampInGMT() {
        return System.currentTimeMillis();
    }

    /**
     * Obtain a local timestamp in milliseconds since the epoch for a specified GMT timestamp.
     *
     * @param timestampInGmt A GMT timestamp in milliseconds since the epoch.
     * @return The converted local time zone timestamp in milliseconds since the epoch.
     */
    public static long timestampInZulu(long timestampInGmt) {
        return timestampInGmt + TimeZone.getDefault().getOffset(timestampInGmt);
    }

    /**
     * Obtain a local time zone timecode in YYYYMMddHHmmss format.
     *
     * @return A local time zone timecode in YYYYMMddHHmmss format.
     */
    public static String timecodeInZulu() {
        Calendar now = GregorianCalendar.getInstance();
        return new SimpleDateFormat("YYYYMMddHHmmss").format(now.getTime());
    }
}