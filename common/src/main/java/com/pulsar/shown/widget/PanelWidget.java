package com.pulsar.shown.widget;

import com.pulsar.shown.UIArea;
import com.pulsar.shown.UIVec;
import net.minecraft.client.gui.GuiGraphics;

import java.awt.*;

public class PanelWidget extends WidgetBase {
    int backgroundColor = 0xFFFFFFFF;
    int borderColor = 0xFF000000;
    int borderThickness = 0;

    public PanelWidget(UIVec position, UIVec size) {
        super(position, size);
    }

    public <T extends PanelWidget> T setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return (T)this;
    }

    public <T extends PanelWidget> T setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor.getRGB();
        return (T)this;
    }

    public int getBackgroundColor() {
        return this.backgroundColor;
    }

    public <T extends PanelWidget> T setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        return (T)this;
    }

    public <T extends PanelWidget> T setBorderColor(Color borderColor) {
        this.borderColor = borderColor.getRGB();
        return (T)this;
    }

    public int getBorderColor() {
        return this.borderColor;
    }

    public <T extends PanelWidget> T setBorderThickness(int borderThickness) {
        this.borderThickness = borderThickness;
        return (T)this;
    }

    public int getBorderThickness() {
        return this.borderThickness;
    }

    @Override
    void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        UIArea drawArea = this.getRenderArea(tickDelta);
        int aX = drawArea.x;
        int aY = drawArea.y;
        int bX = drawArea.x + drawArea.width;
        int bY = drawArea.y + drawArea.height;
        if (borderThickness > 0) {
            guiGraphics.fill(aX, aY, bX, bY, this.getRenderDepth(), borderColor);
            guiGraphics.fill(aX + borderThickness, aY + borderThickness, bX - borderThickness, bY - borderThickness, this.getRenderDepth(), backgroundColor);
        } else {
            guiGraphics.fill(aX, aY, bX, bY, this.getRenderDepth(), backgroundColor);
        }
    }
}
