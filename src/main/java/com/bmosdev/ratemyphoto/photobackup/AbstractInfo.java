package com.bmosdev.ratemyphoto.photobackup;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.File;

public abstract class AbstractInfo {
    
    private final String name;
    private final File path;
    
    public AbstractInfo(File path) {
        this.name = path.getName();
        this.path = path;
    }
    
    public String getName() {
        return name;
    }
    
    public File getPath() {
        return path;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AbstractInfo)) {
            return false;
        }
        AbstractInfo that = (AbstractInfo) obj;
        return EqualsBuilder.reflectionEquals(this, that);
    }
    
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
