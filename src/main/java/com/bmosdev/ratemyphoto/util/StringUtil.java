package com.bmosdev.ratemyphoto.util;

import lime.util.Base64;
import lime.util.Strings;
import net.sf.junidecode.Junidecode;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Igor Kuzminskyy
 */
public class StringUtil {

    private static final String DOTS = "...";

    public static String concat(Object... elements) {
        StringBuilder builder = new StringBuilder();
        for (Object str : elements) {
            builder.append(str);
        }
        return builder.toString();
    }

    public static void concat(StringBuilder builder, Object... elements) {
        for (Object str : elements) {
            builder.append(str);
        }
    }

    public static String nonNull(String nullableString) {
        return nullableString == null ? "" : nullableString;
    }

    public static String emptyToNull(String str) {
        return "".equals(str) ? null : str;
    }

    public static String toCP1251(String s) {
        if (s == null) return null;

        try {
            return new String(s.getBytes("latin1"), "cp1251");
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unexpected UnsupportedEncodingException", e);
        }
    }

    public static byte[] getLatin1Bytes(String str) {
        try {
            return str.getBytes("latin1");
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unexpected UnsupportedEncodingException", e);
        }
    }

    public static String fromLatin1ToUtf8(String s) {
        if (s == null) return null;

        try {
            return new String(s.getBytes("latin1"), "UTF8");
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unexpected UnsupportedEncodingException", e);
        }
    }

    public static String concatAsPhrase(String[] words, String separator, int fromIndex, int howMany) {
        fromIndex = Math.max(fromIndex, 0);
        howMany = Math.min(words.length, howMany);
        StringBuilder result = new StringBuilder();
        for (int i = fromIndex; i < howMany; i++) {
            result.append(words[i]);
            if (i != howMany - 1) {
                result.append(separator);
            }
        }
        return result.toString();
    }

    public static String concatAsPhrase(String[] words, String separator) {
        return concatAsPhrase(words, separator, 0, words.length);
    }

    public static String concatAsPhrase(Collection<?> words, String separator, int fromIndex, int howMany) {
        fromIndex = Math.max(fromIndex, 0);
        howMany = Math.min(words.size(), howMany);
        StringBuilder result = new StringBuilder();
        Iterator<?> wordIterator = words.iterator();
        for (int i = fromIndex; i < howMany; i++) {
            result.append(wordIterator.next());
            if (i != howMany - 1) {
                result.append(separator);
            }
        }
        return result.toString();
    }

    public static String concatAsPhrase(Collection<?> words, String separator) {
        return concatAsPhrase(words, separator, 0, words.size());
    }

    /**
     * This convenience method removes trailing & leading spaces & also 'jams' all spaces inside string (replaces space
     * ranges with 1 space each)
     */
    public static String jam(String string) {
        String[] decomposed = string.trim().split("\\s+");
        return concatAsPhrase(decomposed, " ", 0, decomposed.length);
    }

    /**
     * This function is very similar to jam but it removes ALL whitespaces instead of replacing whitespaces in the middle
     * with 1 space
     */
    public static String collapse(String string) {
        String[] decomposed = string.trim().split("\\s+");
        return concatAsPhrase(decomposed, "", 0, decomposed.length);
    }

    public static boolean isUppercase(String string) {
        return string.toUpperCase().equals(string);
    }

    public static String[] splitToParts(String str, String delimiter, int desiredPartCount) {
        List<String> result = new ArrayList<String>();
        int prevIndex = 0;
        for (int i = 0; i < desiredPartCount - 1; i++) {
            int index = str.indexOf(delimiter, prevIndex);
            if (index < 0) {
                throw new IllegalArgumentException("string [" + str + "] cannot be splitted on " + desiredPartCount + " parts using ["
                        + delimiter + "] delimiter");
            }
            result.add(str.substring(prevIndex, index));
            prevIndex = index + delimiter.length();
        }
        result.add(str.substring(prevIndex));
        if (result.size() < desiredPartCount) {
            throw new IllegalArgumentException("string [" + str + "] cannot be splitted on " + desiredPartCount + " parts using ["
                    + delimiter + "] delimiter");
        }
        return result.toArray(new String[result.size()]);
    }

    public static String[] splitToPartsByMaxLength(String str, int maxPartLength, String overflowMark) {
        boolean firstPart = true;
        List<String> result = new ArrayList<String>();
        while (true) {
            int maxLength = maxPartLength - (firstPart ? 0 : overflowMark.length());
            if (str.length() <= maxLength) {
                result.add((firstPart ? "" : overflowMark) + str);
                break;
            } else {
                maxLength -= overflowMark.length();
                if (maxLength <= 0) {
                    throw new IllegalArgumentException("overflowMark [" + overflowMark + "] is too long for maxLength=" + maxLength);
                }
                String part = (firstPart ? "" : overflowMark) + str.substring(0, maxLength) + overflowMark;
                result.add(part);
                str = str.substring(maxLength);
                firstPart = false;
            }
        }
        return result.toArray(new String[result.size()]);
    }

    public static String quoteRegexpSpecialCharacters(String str) {
        return str.replaceAll("([\\\\\\.\\^\\$\\+\\*\\?\\[\\]\\{\\}\\(\\)])", "\\\\$1");
    }

    public static String safeTrim(String str) {
        if (str != null) {
            return str.trim();
        } else {
            return  str;
        }
    }

    public static String compress(String sourceString) throws IOException {
        if (Strings.isBlank(sourceString)) {
            return sourceString;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(sourceString.getBytes("UTF-8"));
        gzip.close();
        String result = String.valueOf(Base64.encode(out.toByteArray()));
        return result;
    }

    public static String decompress(String compressedString) throws IOException {
        if (Strings.isBlank(compressedString)) {
            return compressedString;
        }
        ByteArrayInputStream in = new ByteArrayInputStream(Base64.decode(compressedString));
        GZIPInputStream gzip = new GZIPInputStream(in);
        InputStreamReader reader = new InputStreamReader(gzip, "UTF-8");
        StringWriter writer = new StringWriter();
        char[] buffer = new char[10240];
        for (int length = 0; (length = reader.read(buffer)) > 0;) {
            writer.write(buffer, 0, length);
        }
        gzip.close();
        String result = writer.toString();
        return result;
    }

    public static String shortenString(String str, int maxLength) {
        if (Strings.isBlank(str) || str.length() <= maxLength || maxLength < DOTS.length()) {
            return str;
        }
        int firstPart = (int) Math.round(1d * (maxLength - DOTS.length()) / 2);
        int lastPartIndex = str.length() - (maxLength - firstPart - DOTS.length());
        return str.substring(0, firstPart) + DOTS + str.substring(lastPartIndex);
    }

    public static String transliterate(String str) {
        return str == null ? null : Junidecode.unidecode(str);
    }

    public static byte[] toUtf8Bytes(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    public static byte[] readByteArrayFromStringRepresentation(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() % 2 != 0) {
            throw new IllegalArgumentException("Invalid input string format: " + str);
        }
        byte[] result = new byte[str.length() / 2];
        for (int i = 0; i < result.length; i++) {
            result[i] = Integer.decode("0X" + str.charAt(i * 2) + str.charAt(i * 2 + 1)).byteValue();
        }
        return result;
    }

    public static String byteArrayToStringRepresentation(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
