package com.bmosdev.ratemyphoto.photobackup;

import org.apache.commons.lang3.text.StrBuilder;

import java.util.ArrayList;
import java.util.List;

public class DirectoryComparison extends AbstractComparison {
    
    private static final char PADDING_SYMBOL = ' ';
    
    private final List<DirectoryComparison> subdirs = new ArrayList<DirectoryComparison>();
    private final List<FileComparison> files = new ArrayList<FileComparison>();
    
    public DirectoryComparison(AbstractInfo actual, AbstractInfo previous, ComparisonResult comparisonResult) {
        super(actual, previous, comparisonResult);
    }
    
    public List<FileComparison> getFiles() {
        return files;
    }
    
    public List<DirectoryComparison> getSubdirs() {
        return subdirs;
    }
    
    public void addFile(FileComparison file) {
        files.add(file);
    }

    public void addSubdir(DirectoryComparison directory) {
        subdirs.add(directory);
    }
    
    public String toFormattedString() {
        StrBuilder sb = new StrBuilder();
        innerToFormattedString(this, sb, 0);
        return sb.toString();
    }
    
    private static void innerToFormattedString(DirectoryComparison comparation, StrBuilder sb, int padding) {
        if (comparation.getComparisonResult() == ComparisonResult.UNCHANGED) {
            return;
        }
        sb.appendPadding(padding, PADDING_SYMBOL)
                .append(comparationResultToSymbol(comparation.getComparisonResult()))
                .append("[").append(comparation.getName()).appendln("]");
        for (FileComparison file : comparation.getFiles()) {
            sb.appendPadding(padding + 1, PADDING_SYMBOL)
                    .append(comparationResultToSymbol(file.getComparisonResult()))
                    .appendln(file.getName());
        }
        for (DirectoryComparison subdir : comparation.getSubdirs()) {
            innerToFormattedString(subdir, sb, padding + 1);
        }
    }
    
    private static char comparationResultToSymbol(ComparisonResult comparisonResult) {
        switch (comparisonResult) {
            case ADDED:
                return '+';
            case REMOVED:
                return '-';
            case MODIFIED:
                return '*';
            case UNCHANGED:
                return '=';
            default: 
                throw new IllegalArgumentException("Unsupported result: " + comparisonResult);
        }
    }
    
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (!(obj instanceof DirectoryComparation)) {
//            return false;
//        }
//        DirectoryComparation that = (DirectoryComparation) obj;
//        return EqualsBuilder.reflectionEquals(this, that, "actual", "previous");
//    }
}
