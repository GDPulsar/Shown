package com.pulsar.shown.constraint;

import com.pulsar.shown.widget.WidgetBase;

public class WidgetConstraint {
    WidgetBase parent;

    public WidgetConstraint() {}

    public <T extends WidgetConstraint> T setParent(WidgetBase parent) {
        this.parent = parent;
        return (T)this;
    }
    public WidgetBase getParent() {
        return this.parent;
    }

    public void update() {}
}
