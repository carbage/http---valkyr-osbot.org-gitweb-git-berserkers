package org.valkyr.api.framework.gui.utils;

/**
 * @Author Josef
 */
public abstract class Returnable<T> {

    public abstract T get();

    @Override
    public String toString() {
        if (get() != null)
            return get().toString();
        return null;
    }
}
