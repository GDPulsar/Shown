package com.pulsar.shown.layout;

import com.pulsar.shown.widget.WidgetBase;

public abstract class WidgetLayout {
    WidgetBase parent;

    public WidgetLayout() {}

    public <T extends WidgetLayout> T setParent(WidgetBase parent) {
        this.parent = parent;
        return (T)this;
    }
    public WidgetBase getParent() {
        return this.parent;
    }

    public void update() {}
}
