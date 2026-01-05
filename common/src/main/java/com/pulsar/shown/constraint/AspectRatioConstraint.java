package com.pulsar.shown.constraint;

import com.pulsar.shown.UIArea;
import com.pulsar.shown.UIVec;

public class AspectRatioConstraint extends WidgetConstraint {
    double ratio;
    boolean prioritizeHeight;

    public AspectRatioConstraint(double ratio, boolean prioritizeHeight) {
        this.ratio = ratio;
        this.prioritizeHeight = prioritizeHeight;
    }

    @Override
    public void update() {
        if (this.getParent() == null) return;
        UIArea area = this.getParent().getArea();
        int targetWidth; int targetHeight;
        if (prioritizeHeight) {
            targetHeight = area.height;
            targetWidth = (int)(ratio * targetHeight);
        } else {
            targetWidth = area.width;
            targetHeight = (int)(targetWidth / ratio);
        }
        this.getParent().setSize(new UIVec(0, 0, targetWidth, targetHeight), true);
    }
}
