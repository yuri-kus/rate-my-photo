package ua.com.softway.date;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Much alike SimpleDateFormat but allow concurrent access
 */
public class DatePattern {
    private final String pattern;
    private final Locale locale;

    public DatePattern(String pattern) {
        this.pattern = pattern;
        this.locale = Locale.getDefault();
    }
    
    public DatePattern(String pattern, String locale) {
        this.pattern = pattern;
        this.locale = new Locale(locale);
    }
    
//    public Date parse(String s) throws ParseException {
//        return parse(s, TimeZone.getDefault().getID());
//    }
    
    public String format(Date date) {
        SimpleDateFormat df = getSimpleDateFormat(pattern, locale, date.getTimeZone());
        
        synchronized (df) {
            return df.format(date.toJavaDate());
        }
    }
    
    public Date parse(String s, TimeZone timeZone) throws ParseException {
        SimpleDateFormat df = getSimpleDateFormat(pattern, locale, timeZone);
        
        synchronized (df) {
            return Date.fromJavaDate(df.parse(s), df.getTimeZone());
        }
    }
    
    public boolean isValid(String s) {
        return isValid(s, TimeZone.getDefault());
    }
    
    public boolean isValid(String s, TimeZone timeZone) {
        SimpleDateFormat df = getSimpleDateFormat(pattern, locale, timeZone);
        
        synchronized (df) {
            ParsePosition p = new ParsePosition(0);
            df.parse(s, p);
            return p.getIndex() == s.length();
        }
    }
    
    private static Map<String, SimpleDateFormat> cache = new HashMap<String, SimpleDateFormat>();
    
    private static SimpleDateFormat getSimpleDateFormat(String pattern, Locale locale, TimeZone timeZone) {
        String key = pattern + locale.getLanguage() + timeZone.getID();
        
        SimpleDateFormat df;
        
        synchronized (cache) {
            df = cache.get(key);
            
            if (df != null) return df;
        }
        
        df = new SimpleDateFormat(pattern, locale);
        df.setLenient(false);
        df.setTimeZone(timeZone);
        
        synchronized (cache) {
            cache.put(key, df);
        }
        
        return df;
    }
}
