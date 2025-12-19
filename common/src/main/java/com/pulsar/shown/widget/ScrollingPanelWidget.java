package com.pulsar.shown.widget;

import com.pulsar.shown.Enums;
import com.pulsar.shown.UIArea;
import com.pulsar.shown.UIVec;
import net.minecraft.client.gui.GuiGraphics;

public class ScrollingPanelWidget extends PanelWidget {
    private double scrollX = 0;
    private double scrollY = 0;

    private boolean allowDragging = false;
    private boolean allowMouseWheel = true;

    private Enums.MouseButton dragButton = Enums.MouseButton.LEFT;
    private double scrollSensitivity = 1;

    public ScrollingPanelWidget(UIVec position, UIVec size) {
        super(position, size);
    }

    public <T extends ScrollingPanelWidget> T enableDragging() {
        this.allowDragging = true;
        return (T)this;
    }

    public <T extends ScrollingPanelWidget> T disableDragging() {
        this.allowDragging = false;
        return (T)this;
    }

    public <T extends ScrollingPanelWidget> T enableMouseWheel() {
        this.allowMouseWheel = true;
        return (T)this;
    }

    public <T extends ScrollingPanelWidget> T disableMouseWheel() {
        this.allowMouseWheel = false;
        return (T)this;
    }

    public <T extends ScrollingPanelWidget> T setScrollSensitivity(double scrollSensitivity) {
        this.scrollSensitivity = scrollSensitivity;
        return (T)this;
    }

    public double getScrollSensitivity() {
        return this.scrollSensitivity;
    }

    public <T extends ScrollingPanelWidget> T setDragButton(Enums.MouseButton dragButton) {
        this.dragButton = dragButton;
        return (T)this;
    }

    public Enums.MouseButton getDragButton() {
        return this.dragButton;
    }

    private boolean startedDrag = false;

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean childClicked = super.mouseClicked(mouseX, mouseY, button);
        if (childClicked) return true;
        if (this.contains(mouseX, mouseY) && button == dragButton.button()) {
            startedDrag = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean childReleased = super.mouseReleased(mouseX, mouseY, button);
        if (childReleased) return true;
        startedDrag = false;
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        if (allowDragging && startedDrag && button == dragButton.button()) {
            this.scrollX += dx;
            this.scrollY += dy;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (allowMouseWheel) {
            this.scrollX += scrollX * scrollSensitivity;
            this.scrollY += scrollY * scrollSensitivity;
            return true;
        }
        return false;
    }

    @Override
    public void preRenderChild(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        UIArea area = this.getArea();
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(this.scrollX, this.scrollY, 0);
        guiGraphics.enableScissor(area.x, area.y, area.x + area.width, area.y + area.height);
    }

    @Override
    public void postRenderChild(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        guiGraphics.disableScissor();
        guiGraphics.pose().popPose();
    }
}
