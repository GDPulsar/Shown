package com.pulsar.shown.widget;

import com.pulsar.shown.Shown;
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
    void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta, boolean debug) {
        UIArea drawArea = this.getRenderArea(tickDelta);
        if (borderThickness > 0) {
            Shown.betterFill(guiGraphics, drawArea, this.getRenderDepth(), borderColor);
            Shown.betterFill(guiGraphics, drawArea.shrink(borderThickness), this.getRenderDepth(), backgroundColor);
        } else {
            Shown.betterFill(guiGraphics, drawArea, this.getRenderDepth(), backgroundColor);
        }
    }
}
