package com.bmosdev.ratemyphoto.photobackup;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.text.StrBuilder;

public class ComparisonStat {

    private int filesAdded;
    private int filesRemoved;
    private int filesModified;

    private int dirsAdded;
    private int dirsRemoved;

    private long dataToCopy;
    private long dataToRemove;

    public ComparisonStat() {
    }

    public ComparisonStat(int filesAdded, int filesRemoved, int filesModified, int dirsAdded, int dirsRemoved, long dataToCopy, long dataToRemove) {
        this.filesAdded = filesAdded;
        this.filesRemoved = filesRemoved;
        this.filesModified = filesModified;
        this.dirsAdded = dirsAdded;
        this.dirsRemoved = dirsRemoved;
        this.dataToCopy = dataToCopy;
        this.dataToRemove = dataToRemove;
    }

    public int getFilesAdded() {
        return filesAdded;
    }

    public void setFilesAdded(int filesAdded) {
        this.filesAdded = filesAdded;
    }

    public int getFilesRemoved() {
        return filesRemoved;
    }

    public void setFilesRemoved(int filesRemoved) {
        this.filesRemoved = filesRemoved;
    }

    public int getFilesModified() {
        return filesModified;
    }

    public void setFilesModified(int filesModified) {
        this.filesModified = filesModified;
    }

    public int getDirsAdded() {
        return dirsAdded;
    }

    public void setDirsAdded(int dirsAdded) {
        this.dirsAdded = dirsAdded;
    }

    public int getDirsRemoved() {
        return dirsRemoved;
    }

    public void setDirsRemoved(int dirsRemoved) {
        this.dirsRemoved = dirsRemoved;
    }

    public long getDataToCopy() {
        return dataToCopy;
    }

    public void setDataToCopy(long dataToCopy) {
        this.dataToCopy = dataToCopy;
    }

    public long getDataToRemove() {
        return dataToRemove;
    }

    public void setDataToRemove(long dataToRemove) {
        this.dataToRemove = dataToRemove;
    }

    public void fileAdded(long size) {
        filesAdded++;
        dataToCopy += size;
    }
    
    public void fileRemoved(long size) {
        filesRemoved++;
        dataToRemove += size;
    }
    
    public void fileModified(long oldSize, long newSize) {
        filesModified++;
        dataToCopy += newSize;
        dataToRemove += oldSize;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String toFormattedString() {
        StrBuilder sb = new StrBuilder();
        sb.append("files added:    ").appendln(filesAdded);
        sb.append("files removed:  ").appendln(filesRemoved);
        sb.append("files modified: ").appendln(filesModified);
        sb.append("dirs added:     ").appendln(dirsAdded);
        sb.append("dirs removed:   ").appendln(dirsRemoved);
        sb.append("data to copy:   ").appendln(FileUtils.byteCountToDisplaySize(dataToCopy));
        sb.append("data to remove: ").appendln(FileUtils.byteCountToDisplaySize(dataToRemove));
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ComparisonStat)) {
            return false;
        }
        ComparisonStat that = (ComparisonStat) obj;
        return EqualsBuilder.reflectionEquals(this, that);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
