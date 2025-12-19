package com.pulsar.shown.widget;

import com.pulsar.shown.UIArea;
import com.pulsar.shown.UIVec;
import net.minecraft.client.gui.GuiGraphics;

public class ToggleButtonWidget extends ButtonWidget {
    private int toggledColor;

    private boolean pressed;

    public ToggleButtonWidget(UIVec position, UIVec size, ClickEvent clickEvent) {
        super(position, size, clickEvent);
    }

    public <T extends ToggleButtonWidget> T setToggledColor(int toggledColor) {
        this.toggledColor = toggledColor;
        return (T)this;
    }

    public int getToggledColor() {
        return this.toggledColor;
    }

    public boolean isPressed() {
        return this.pressed;
    }

    @Override
    void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        UIArea drawArea = this.getRenderArea(tickDelta);
        int aX = drawArea.x;
        int aY = drawArea.y;
        int bX = drawArea.x + drawArea.width;
        int bY = drawArea.y + drawArea.height;
        int drawColor = backgroundColor;
        if (this.isPressed()) drawColor = this.getToggledColor();
        if (this.getHoverColor() != -1 && this.contains(mouseX, mouseY)) drawColor = this.getHoverColor();
        if (borderThickness > 0) {
            guiGraphics.fill(aX, aY, bX, bY, borderColor);
            guiGraphics.fill(aX + borderThickness, aY + borderThickness, bX - borderThickness, bY - borderThickness, drawColor);
        } else {
            guiGraphics.fill(aX, aY, bX, bY, drawColor);
        }
    }
}
