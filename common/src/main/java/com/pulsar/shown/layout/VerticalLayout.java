package com.pulsar.shown.layout;

import com.pulsar.shown.Enums;
import com.pulsar.shown.UIArea;
import com.pulsar.shown.UIVec;
import com.pulsar.shown.widget.WidgetBase;

import java.util.List;

public class VerticalLayout extends WidgetLayout {
    int padding;
    Enums.YAlignment alignment;

    public VerticalLayout(int padding, Enums.YAlignment alignment) {
        this.padding = padding;
        this.alignment = alignment;
    }

    @Override
    public void update() {
        if (this.getParent() == null) return;
        UIArea area = this.getParent().getArea();
        List<WidgetBase> children = this.parent.getChildren();
        double totalHeight = 0;
        for (int i = 0; i < children.size(); i++) {
            WidgetBase child = children.get(i);
            totalHeight += child.getArea().height;
            if (i > 0) totalHeight += this.padding;
        }
        double top = 0;
        switch (alignment) {
            case TOP -> top = padding;
            case MIDDLE -> top = (area.height - totalHeight) / 2;
            case BOTTOM -> top = area.height - totalHeight - padding;
        }
        int yOffset = 0;
        for (WidgetBase child : children) {
            UIVec childPos = child.getPosition();
            UIArea childArea = child.getArea();
            child.setPosition(new UIVec(childPos.scaleX, 0, childPos.offsetX, (int) (top + yOffset)));
            yOffset += childArea.height + padding;
        }
    }
}
