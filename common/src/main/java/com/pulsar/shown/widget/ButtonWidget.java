package com.pulsar.shown.widget;

import com.pulsar.shown.Shown;
import com.pulsar.shown.UIArea;
import com.pulsar.shown.UIVec;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.Consumer;

public class ButtonWidget extends PanelWidget {
    private final ClickEvent clickEvent;

    private int hoverColor = -1;

    public ButtonWidget(UIVec position, UIVec size, ClickEvent clickEvent) {
        super(position, size);
        this.clickEvent = clickEvent;
    }

    public <T extends ButtonWidget> T setHoverColor(int hoverColor) {
        this.hoverColor = hoverColor;
        return (T)this;
    }

    public int getHoverColor() {
        return this.hoverColor;
    }

    @Override
    void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta, boolean debug) {
        UIArea drawArea = this.getRenderArea(tickDelta);
        int drawColor = hoverColor != -1 && this.contains(mouseX, mouseY) ? hoverColor : backgroundColor;
        if (borderThickness > 0) {
            Shown.betterFill(guiGraphics, drawArea, this.getRenderDepth(), borderColor);
            Shown.betterFill(guiGraphics, drawArea.shrink(borderThickness), this.getRenderDepth(), drawColor);
        } else {
            Shown.betterFill(guiGraphics, drawArea, this.getRenderDepth(), drawColor);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean childClicked = super.mouseClicked(mouseX, mouseY, button);
        if (childClicked) return true;
        if (this.contains(mouseX, mouseY)) {
            this.clickEvent.onClick(mouseX, mouseY, button);
            return true;
        }
        return false;
    }

    public interface ClickEvent {
        void onClick(double mouseX, double mouseY, int button);
    }
}
