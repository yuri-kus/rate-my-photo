package com.bmosdev.ratemyphoto.photobackup;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public abstract class AbstractComparison {
    
    private final String name;
    private final AbstractInfo actual;
    private final AbstractInfo previous;
    private final ComparisonResult comparisonResult;
    
    public AbstractComparison(AbstractInfo actual, AbstractInfo previous, ComparisonResult comparisonResult) {
        this.name = actual != null ? actual.getName() : previous != null ? previous.getName() : "<undef>";
        this.actual = actual;
        this.previous = previous;
        this.comparisonResult = comparisonResult;
    }
    
    public String getName() {
        return name;
    }

    public AbstractInfo getActual() {
        return actual;
    }
    
    public AbstractInfo getPrevious() {
        return previous;
    }
    
    public ComparisonResult getComparisonResult() {
        return comparisonResult;
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
        if (!(obj instanceof AbstractComparison)) {
            return false;
        }
        AbstractComparison that = (AbstractComparison) obj;
        return EqualsBuilder.reflectionEquals(this, that);
    }
    
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
