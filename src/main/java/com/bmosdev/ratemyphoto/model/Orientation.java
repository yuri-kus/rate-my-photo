package com.bmosdev.ratemyphoto.model;

public enum Orientation {

    TOP_LEFT(1),
    TOP_RIGHT(2),
    BOTTOM_RIGHT(3),
    BOTTOM_LEFT(4),
    LEFT_TOP(5),
    RIGHT_TOP(6),
    RIGHT_BOTTOM(7),
    LEFT_BOTTOM(8);

    final int index;

    Orientation(int index) {
        this.index = index;
    }

    public static Orientation findByIndex(int index) {
        for (Orientation orientation : values()) {
            if (orientation.index == index) {
                return orientation;
            }
        }
        throw new IllegalArgumentException("Unsupported index: " + index);
    }
}
