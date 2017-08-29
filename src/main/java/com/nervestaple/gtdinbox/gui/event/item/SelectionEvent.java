package com.nervestaple.gtdinbox.gui.event.item;

import com.nervestaple.gtdinbox.gui.event.item.ItemEvent;

/**
 *
 */
public class SelectionEvent<T> extends ItemEvent {

    public SelectionEvent(T instance) {
        super(instance);
    }
}
