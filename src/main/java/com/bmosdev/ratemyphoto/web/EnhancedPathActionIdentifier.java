package com.bmosdev.ratemyphoto.web;

import lime.web.*;
import lime.web.urls.*;

import org.apache.commons.lang3.builder.*;

public class EnhancedPathActionIdentifier extends PathActionIdentifier {
    private final String template;
    private final ActionPath actionPath;
    private final boolean exact;

    public EnhancedPathActionIdentifier(String template, ActionPath actionPath) {
        super(template, actionPath, true);
        this.actionPath = actionPath;
        this.exact = true;
        this.template = template;
    }

    public EnhancedPathActionIdentifier(String template, ActionPath actionPath, boolean exact) {
        super(template, actionPath, exact);
        this.actionPath = actionPath;
        this.exact = exact;
        this.template = template;
    }


    public String getTemplate() {
        return template;
    }

    public ActionPath getActionPath() {
        return actionPath;
    }

    public boolean isExact() {
        return exact;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that);
    }
}
