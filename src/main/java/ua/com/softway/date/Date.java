package ua.com.softway.date;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import lime.i18n.Resources;

// FIXME add fromUTC
public class Date implements Serializable, Comparable<Date> {
    public final static int JAN = 1;
    public final static int FEB = 2;
    public final static int MAR = 3;
    public final static int APR = 4;
    public final static int MAY = 5;
    public final static int JUN = 6;
    public final static int JUL = 7;
    public final static int AUG = 8;
    public final static int SEP = 9;
    public final static int OCT = 10;
    public final static int NOV = 11;
    public final static int DEC = 12;

    public final static int SUN = 1;
    public final static int MON = 2;
    public final static int TUE = 3;
    public final static int WED = 4;
    public final static int THU = 5;
    public final static int FRI = 6;
    public final static int SAT = 7;
    
    public static final int MILLIS_IN_SECOND = 1000;
    public static final int SECONDS_IN_MINUTE = 60;
    public static final int MINUTES_IN_HOUR = 60;
    public static final int HOURS_IN_DAY = 24;
    public static final int DAYS_IN_ORDINAR_YEAR = 365;
    
    public static final int MILLIS_IN_MINUTE = MILLIS_IN_SECOND * SECONDS_IN_MINUTE;
    public static final int SECONDS_IN_HOUR = SECONDS_IN_MINUTE * MINUTES_IN_HOUR;
    public static final int MILLIS_IN_HOUR = MILLIS_IN_SECOND * SECONDS_IN_HOUR;
    public static final int SECONDS_IN_DAY = SECONDS_IN_HOUR * HOURS_IN_DAY;
    public static final int SECONDS_IN_ORDINAR_YEAR = SECONDS_IN_DAY * DAYS_IN_ORDINAR_YEAR;
    public static final long MILLIS_IN_DAY = MILLIS_IN_SECOND * SECONDS_IN_DAY;
    public static final long MILLIS_IN_ORDINAR_YEAR = MILLIS_IN_SECOND * SECONDS_IN_ORDINAR_YEAR;

    private int day; // 24
    private int month; // 12
    private int year; // 2007
    private int hour; // 23
    private int minute; // 59
    private int second; // 59
    private int millis; // 999
    private TimeZone timeZone;
    
    public Date(int year, int month, int day, int hour, int minute, int second,
                int millis, TimeZone timeZone) {
        GregorianCalendar calendar = new GregorianCalendar(timeZone);
        calendar.clear();

        calendar.set(GregorianCalendar.YEAR, year);
        calendar.set(GregorianCalendar.MONTH, month - 1);
        calendar.set(GregorianCalendar.DAY_OF_MONTH, day);
        calendar.set(GregorianCalendar.HOUR_OF_DAY, hour);
        calendar.set(GregorianCalendar.MINUTE, minute);
        calendar.set(GregorianCalendar.SECOND, second);
        calendar.set(GregorianCalendar.MILLISECOND, millis);

        fromGregorianCalendar(calendar);
    }
    
    public Date(int year, int month, int day, int hour, int minute, int second,
            int millis) {
        this(year, month, day, hour, minute, second, millis, TimeZone.getDefault());
    }

    private void fromGregorianCalendar(Calendar calendar) {
        timeZone = calendar.getTimeZone();
        
        year = calendar.get(GregorianCalendar.YEAR);
        month = calendar.get(GregorianCalendar.MONTH) + 1;
        day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
        hour = calendar.get(GregorianCalendar.HOUR_OF_DAY);
        minute = calendar.get(GregorianCalendar.MINUTE);
        second = calendar.get(GregorianCalendar.SECOND);
        millis = calendar.get(GregorianCalendar.MILLISECOND);
    }

    public Date(int year, int month, int day, int hour, int minute, int second) {
        this(year, month, day, hour, minute, second, 0);
    }

    public Date(int year, int month, int day, int hour, int minute) {
        this(year, month, day, hour, minute, 0);
    }

    public Date(int year, int month, int day) {
        this(year, month, day, 0, 0, 0);
    }

    private Date(TimeZone timeZone) {
        fromGregorianCalendar(new GregorianCalendar(timeZone));
    }

    // TODO reimplement
    public long getUTC() {
        return toJavaCalendar().getTimeInMillis();
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public int getMillis() {
        return millis;
    }
    
    public TimeZone getTimeZone() {
        return timeZone;
    }

    public boolean sameDay(Date d) {
        return year == d.getYear() && month == d.getMonth()
                && day == d.getDay();
    }

    public boolean after(Date d) {
        return compareTo(d) > 0;
    }

    public boolean before(Date d) {
        return compareTo(d) < 0;
    }

    /**
     * Return difference days
     */
    public int differenceInDays(Date d) {
        return (int) Math.round((getUTC() - d.getUTC()) / (1.0 * MILLIS_IN_DAY));
    }

    public Date addDays(int days) {
        return new Date(year, month, day + days, hour, minute, second, millis, timeZone);
    }

    public Date addMonthes(int monthes) {
        return new Date(year, month + monthes, day, hour, minute, second, millis, timeZone);
    }

    public int getDayOfWeek() {
        Calendar cal = toJavaCalendar();

        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public int getWeekOfYear() {
        Calendar cal = toJavaCalendar();

        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    public Calendar toJavaCalendar() {
        GregorianCalendar calendar = new GregorianCalendar(timeZone);

        calendar.clear();
        calendar.set(GregorianCalendar.YEAR, year);
        calendar.set(GregorianCalendar.MONTH, month - 1);
        calendar.set(GregorianCalendar.DAY_OF_MONTH, day);
        calendar.set(GregorianCalendar.HOUR_OF_DAY, hour);
        calendar.set(GregorianCalendar.MINUTE, minute);
        calendar.set(GregorianCalendar.SECOND, second);
        calendar.set(GregorianCalendar.MILLISECOND, millis);

        return calendar;
    }

    public Date getPreviousWorkingDay() {
        Date date = yesterday();

        while (date.getDayOfWeek() == Date.SUN
                || date.getDayOfWeek() == Date.SAT) {
            date = date.yesterday();
        }

        return date;
    }
    
    public static Date today(TimeZone timeZone) {
        return new Date(timeZone).date();
    }
    
    public static Date today() {
        return today(TimeZone.getDefault());
    }

    public static Date now(TimeZone timeZone) {
        return new Date(timeZone);
    }
    
    public static Date now() {
        return now(TimeZone.getDefault());
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + day;
        result = prime * result + hour;
        result = prime * result + millis;
        result = prime * result + minute;
        result = prime * result + month;
        result = prime * result + second;
        result = prime * result + year;
        return result;
    }

    @SuppressWarnings({"RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Date))
            return false;
        final Date other = (Date) obj;
        if (day != other.day)
            return false;
        if (hour != other.hour)
            return false;
        if (millis != other.millis)
            return false;
        if (minute != other.minute)
            return false;
        if (month != other.month)
            return false;
        if (second != other.second)
            return false;
        if (year != other.year)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("%04d.%02d.%02d %02d:%02d:%02d.%03d", year, month,
                day, hour, minute, second, millis);
        //        return String.format("%04d.%02d.%02d %02d:%02d:%02d.%03d", getYear(), getMonth(), getDay(), getHour(), getMinute(), getSecond(), getMillis());
    }

    public String getDayName() {
        return getDay() + " "
                + Resources.getText("date.daynamemonth." + getMonth());
    }

    public String getMonthName() {
        return Resources.getText("date.month." + getMonth());
    }

    public int compareTo(Date o) {
        if (year > o.year)
            return 1;
        if (year < o.year)
            return -1;

        if (month > o.month)
            return 1;
        if (month < o.month)
            return -1;

        if (day > o.day)
            return 1;
        if (day < o.day)
            return -1;

        if (hour > o.hour)
            return 1;
        if (hour < o.hour)
            return -1;

        if (minute > o.minute)
            return 1;
        if (minute < o.minute)
            return -1;

        if (second > o.second)
            return 1;
        if (second < o.second)
            return -1;

        if (millis > o.millis)
            return 1;
        if (millis < o.millis)
            return -1;

        return 0;
    }

    public java.util.Date toJavaDate() {
        return toJavaCalendar().getTime();
    }

    public static Date fromJavaDate(java.util.Date date, TimeZone timeZone) {
        Date d = new Date(timeZone);

        GregorianCalendar calendar = new GregorianCalendar(timeZone);
        calendar.setTime(date);

        d.fromGregorianCalendar(calendar);

        return d;
    }
    
    public static Date fromJavaDate(java.util.Date date) {
        return fromJavaDate(date, TimeZone.getDefault());
    }

    public Date yearAfter() {
        return new Date(year + 1, month, day, hour, minute, second, millis, timeZone);
    }

    public Date yearAgo() {
        return new Date(year - 1, month, day, hour, minute, second, millis, timeZone);
    }

    public Date weekAgo() {
        return new Date(year, month, day - 7, hour, minute, second, millis, timeZone);
    }

    public Date weekAfter() {
        return new Date(year, month, day + 7, hour, minute, second, millis, timeZone);
    }

    public Date monthAfter() {
        return new Date(year, month + 1, day, 0, 0, 0, 0, timeZone);
    }

    public Date yesterday() {
        return new Date(year, month, day - 1, hour, minute, second, millis, timeZone);
    }

    public Date tomorrow() {
        return new Date(year, month, day + 1, hour, minute, second, millis, timeZone);
    }

    public Date monthAgo() {
        return new Date(year, month - 1, day, hour, minute, second, millis, timeZone);
    }

    public Date daysAgo(int i) {
        return new Date(year, month, day - i, hour, minute, second, millis, timeZone);
    }

    public Date daysAfter(int i) {
        return new Date(year, month, day + i, hour, minute, second, millis, timeZone);
    }

    public Date hourAfter() {
        return new Date(year, month, day, hour + 1, minute, second, millis, timeZone);
    }

    public Date hourAgo() {
        return new Date(year, month, day, hour - 1, minute, second, millis, timeZone);
    }

    public Date hoursAfter(int i) {
        return new Date(year, month, day, hour + i, minute, second, millis, timeZone);
    }

    public Date hoursAgo(int i) {
        return new Date(year, month, day, hour - i, minute, second, millis, timeZone);
    }

    public Date minutesAfter(int i) {
        return new Date(year, month, day, hour, minute + i, second, millis, timeZone);
    }

    public Date minutesAgo(int i) {
        return new Date(year, month, day, hour, minute - i, second, millis, timeZone);
    }

    public Date secondsAfter(int i) {
        return new Date(year, month, day, hour, minute, second + i, millis, timeZone);
    }

    public Date secondsAgo(int i) {
        return new Date(year, month, day, hour, minute, second - i, millis, timeZone);
    }

    public Date date() {
        return new Date(year, month, day, 0, 0, 0, 0, timeZone);
    }

    /**
     * @param begin inclusive
     * @param end   inclusive
     * @return
     */
    public boolean between(Date begin, Date end) {
        return !begin.after(this) && !end.before(this);
    }
    
    /**
     * In calendary day of week with number 1 corresponds to SUNDAY, but in Ukraine, Russia and so on the first day of
     * week is MONDAY so sometimes it is necessary to retrieve day of week in usual for these countries format
     */
    public int getMondayBasedDayOfWeek() {
        int result = (getDayOfWeek() + 6) % 7;
        return result != 0 ? result : result + 7;
    }

    public Date getDayEnd() {
        return fromJavaDate(new java.util.Date(date().tomorrow().getUTC() - 1), timeZone);
    }

    public Date addSeconds(int seconds) {
        return new Date(getYear(), getMonth(), getDay(),
                getHour(), getMinute(), getSecond() + seconds, getMillis(), timeZone);
    }

    public Date addMinutes(int minutes) {
        return new Date(getYear(), getMonth(), getDay(),
                getHour(), getMinute() + minutes, getSecond(), getMillis(), timeZone);
    }

    public Date addMilliseconds(long milliseconds) {
        return Date.fromJavaDate(new java.util.Date(getUTC() + milliseconds), timeZone);
    }

    public static boolean equalsUpToSecond(Date one, Date two) {
        return Math.abs(one.getUTC() - two.getUTC()) < 1000;
    }

    public static int differenceInSeconds(long fromMillis, long toMillis) {
        return (int) ((toMillis - fromMillis) / 1000);
    }

    public static int differenceInSeconds(Date from, Date to) {
        return differenceInSeconds(from.getUTC(), to.getUTC());
    }

    public static int differenceInSeconds(long fromMillis, Date to) {
        return differenceInSeconds(fromMillis, to.getUTC());
    }

    public static int differenceInSeconds(Date from, long toMillis) {
        return differenceInSeconds(from.getUTC(), toMillis);
    }
    
    public static String differenceToString(Date first, Date second) {
        long delta = differenceInSeconds(first, second);
        int seconds = (int) (Math.abs(delta) % 60);
        int minutes = (int) ((Math.abs(delta) / 60) % 60);
        int hours = (int) (((Math.abs(delta) / 60) / 60) % 60);
        
        StringBuilder sb = new StringBuilder();
        sb.append(delta < 0 ? "-" : "");
        if (hours > 0) {
            sb.append(hours).append(":");
        }
        if (minutes > 9 || hours == 0) {
            sb.append(minutes);
        } else {
            sb.append("0").append(minutes);
        }
        sb.append(":");
        if (seconds > 9) {
            sb.append(seconds);
        } else {
            sb.append("0").append(seconds);
        }
        return sb.toString();
    }

    public Date getNearestDayMoment(int momentHours, int momentMinutes) {
        Date result = new Date(getYear(), getMonth(), getDay(), momentHours, momentMinutes, 0, 0, timeZone);
        
        if (after(result)) {
            return result.tomorrow();
        } else {
            return result;
        }
    }
}
