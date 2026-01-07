package com.pulsar.shown.widget;

import com.pulsar.shown.Shown;
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
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean childClicked = super.mouseClicked(mouseX, mouseY, button);
        if (childClicked) return true;
        if (this.contains(mouseX, mouseY)) {
            this.pressed = !this.pressed;
            return true;
        }
        return false;
    }

    @Override
    void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta, boolean debug) {
        UIArea drawArea = this.getRenderArea(tickDelta);
        int drawColor = backgroundColor;
        if (this.isPressed()) drawColor = this.getToggledColor();
        if (this.getHoverColor() != -1 && this.contains(mouseX, mouseY)) drawColor = this.getHoverColor();
        if (borderThickness > 0) {
            Shown.betterFill(guiGraphics, drawArea, this.getRenderDepth(), borderColor);
            Shown.betterFill(guiGraphics, drawArea.shrink(2), this.getRenderDepth(), drawColor);
        } else {
            Shown.betterFill(guiGraphics, drawArea, this.getRenderDepth(), drawColor);
        }
    }
}
