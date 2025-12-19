package com.pulsar.shown.layout;

import com.pulsar.shown.Enums;
import com.pulsar.shown.Shown;
import com.pulsar.shown.UIArea;
import com.pulsar.shown.UIVec;
import com.pulsar.shown.widget.WidgetBase;

import java.util.List;

public class HorizontalLayout extends WidgetLayout {
    int padding;
    Enums.XAlignment alignment;

    public HorizontalLayout(int padding, Enums.XAlignment alignment) {
        this.padding = padding;
        this.alignment = alignment;
    }

    @Override
    public void update() {
        if (this.getParent() == null) return;
        UIArea area = this.getParent().getArea();
        List<WidgetBase> children = this.parent.getChildren();
        double totalWidth = 0;
        for (int i = 0; i < children.size(); i++) {
            WidgetBase child = children.get(i);
            totalWidth += child.getArea().width;
            if (i > 0) totalWidth += this.padding;
        }
        double left = 0;
        switch (alignment) {
            case LEFT -> left = padding;
            case CENTER -> left = (area.width - totalWidth) / 2;
            case RIGHT -> left = area.width - totalWidth - padding;
        }
        int xOffset = 0;
        for (WidgetBase child : children) {
            UIVec childPos = child.getPosition();
            UIArea childArea = child.getArea();
            child.setPosition(new UIVec(0, childPos.scaleY, (int)(left + xOffset), childPos.offsetY));
            xOffset += childArea.width + padding;
        }
    }
}
