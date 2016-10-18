package com.bmosdev.ratemyphoto.web;

import java.util.*;

public class Template {
    protected String path;
    protected Map<String, Object> context = new HashMap<String, Object>();
    private String contentType = "text/html";

    public Template(String path, Object... context) {
        this.path = path;

        if (context.length % 2 != 0) throw new IllegalArgumentException("context.length should be even");

        for (int i = 0; i < context.length / 2; i++) {
            if (!(context[2 * i] instanceof String))
                throw new IllegalArgumentException(2 * i + "-th element in context should be a String");

            String name = (String) context[2 * i];
            Object value = context[2 * i + 1];

            this.context.put(name, value);
        }
    }

    public Template(String path, List<?> context) {
        this.path = path;

        if (context.size() % 2 != 0) throw new IllegalArgumentException("context.size() should be even");

        for (int i = 0; i < context.size() / 2; i++) {
            if (!(context.get(2 * i) instanceof String))
                throw new IllegalArgumentException(2 * i + "-th element in context should be a String");

            String name = (String) context.get(2 * i);
            Object value = context.get(2 * i + 1);

            this.context.put(name, value);
        }
    }

    public String getPath() {
        return path;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Template)) return false;
        Template that = (Template) obj;
        return this.path.equals(that.path);
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @Override
    public String toString() {
        return "Template{" + path + "}";
    }
}
