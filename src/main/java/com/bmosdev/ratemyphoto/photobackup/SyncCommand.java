package com.bmosdev.ratemyphoto.photobackup;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.File;

public final class SyncCommand {

    private final Action action;
    private final File source;
    private final File target;
    
    public SyncCommand(Action action, File source, File target) {
        this.action = action;
        this.source = source;
        this.target = target;
    }

    public SyncCommand(Action action, File target) {
        this(action, null, target);
    }

    public Action getAction() {
        return action;
    }

    public File getSource() {
        return source;
    }
    
    public File getTarget() {
        return target;
    }
    
    public enum Action {
        COPY_FILE, MAKE_DIR, REMOVE_DIR, REMOVE_FILE
    }
    
    @Override
    public String toString() {
        switch (action) {
            case COPY_FILE:
                return "copy '" + source + "' to '" + target + "'";
            case MAKE_DIR:
                return "make directory '" + target + "'";
            case REMOVE_DIR:
                return "remove directory '" + target + "'";
            case REMOVE_FILE:
                return "remove file '" + target + "'";
            default: 
                throw new IllegalArgumentException("Unsupported action: " + action);
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SyncCommand)) {
            return false;
        }
        SyncCommand that = (SyncCommand) obj;
        return EqualsBuilder.reflectionEquals(this, that);
    }
    
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
