package com.bmosdev.ratemyphoto.web;

import java.lang.reflect.*;
import java.util.*;

import lime.util.*;
import lime.web.*;

/**
 * @author Igor Kuzminskyy
 */

/**
 * This class is intended to provide convenient means of collecting PathActionIdentifiers from an Action class
 * which has all action-methods annotated with Action annotation
 */
public class LimePathGatherer implements Iterable<EnhancedPathActionIdentifier> {
    private Set<EnhancedPathActionIdentifier> pathActionIdentifiers = new HashSet<EnhancedPathActionIdentifier>();

    private Set<String> uniquePathSet = new TreeSet<String>();


    /**
     * Gathers actions in given Action class depending on Action annotation
     * <p/>
     * Please note that PathActionIdentifiers are gathered with exact matching ONLY because
     * order in which Actions will be subsequently traversed can not be predicted
     * <p/>
     * Annotations will only be considered on public methods
     *
     * @param actionClass class to gather actions in
     */
    public void gather(Class actionClass) {
        if (actionClass == null) {
            throw new IllegalArgumentException("ActionClass must not be null");
        }
        for (Method method : actionClass.getMethods()) {
            Action annotation = method.getAnnotation(Action.class);
            if (annotation == null) continue;

            validateNotStatic(method);
            validateNotBlankPath(method, annotation);
            validateDuplicatePaths(annotation);

            pathActionIdentifiers.add(new EnhancedPathActionIdentifier(annotation.path(), new ActionPath(actionClass, method.getName()), true));
            uniquePathSet.add(annotation.path());
        }
    }

    private void validateNotBlankPath(Method method, Action annotation) {
        if (Strings.isBlank(annotation.path())) {
            throw new IllegalStateException("Blank path not allowed in Action annotation. Problem exist in " + method.getDeclaringClass());
        }
    }

    private void validateNotStatic(Method method) {
        if (Modifier.isStatic(method.getModifiers())) {
            throw new IllegalStateException("Static method in " + method.getDeclaringClass() + "marked ith @Action");
        }
    }

    private void validateDuplicatePaths(Action annotation) {
        if (uniquePathSet.contains(annotation.path())) {
            throw new IllegalStateException("Duplicate definition of path " + annotation.path() + " found");
        }
    }

    public int size() {
        return pathActionIdentifiers.size();
    }

    /**
     * Convenience method for gathering annotations from arbitrary number of action classes in one method call
     *
     * @param actionClasses
     */
    public void gatherAll(Class... actionClasses) {
        for (Class actionClass : actionClasses) {
            gather(actionClass);
        }
    }

    /**
     * Allows traversing gathered PathActionIdentifiers
     *
     * @return iterator for gathered PathActionIdentifiers
     */
    public Iterator<EnhancedPathActionIdentifier> iterator() {
        return pathActionIdentifiers.iterator();
    }

}
