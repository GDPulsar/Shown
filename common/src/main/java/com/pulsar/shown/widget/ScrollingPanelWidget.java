package com.pulsar.shown.widget;

import com.pulsar.shown.Enums;
import com.pulsar.shown.UIArea;
import com.pulsar.shown.UIVec;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import org.joml.Vector2i;

public class ScrollingPanelWidget extends PanelWidget {
    private double scrollX = 0;
    private double scrollY = 0;

    private UIArea scrollLimits = null;
    private Vector2i scrollPadding = null;
    private boolean autoScrollLimits = false;

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

    public <T extends ScrollingPanelWidget> T setScrollLimits(UIArea scrollLimits) {
        this.scrollLimits = scrollLimits;
        this.autoScrollLimits = false;
        return (T)this;
    }
    public <T extends ScrollingPanelWidget> T setScrollLimits(int paddingX, int paddingY) {
        this.scrollPadding = new Vector2i(paddingX, paddingY);
        this.autoScrollLimits = true;
        return (T)this;
    }

    public UIArea getScrollLimits() {
        return this.scrollLimits;
    }

    @Override
    public void onInit() {
        super.onInit();
    }

    @Override
    public void updateArea(UIArea parentArea) {
        super.updateArea(parentArea);
        this.recalculateScrollLimits();
    }

    private void recalculateScrollLimits() {
        if (!this.autoScrollLimits) return;
        UIArea area = this.getArea();
        int minX = 0;
        int minY = 0;
        int maxX = 0;
        int maxY = 0;
        for (WidgetBase child : this.getChildren()) {
            minX = Math.min(minX, child.getArea().x);
            minY = Math.min(minY, child.getArea().y);
            maxX = Math.max(maxX, child.getArea().x + child.getArea().width - area.width);
            maxY = Math.max(maxY, child.getArea().y + child.getArea().height - area.height);
        }
        this.scrollLimits = new UIArea(minX, minY, maxX - minX, maxY - minY);
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
            recalculateScrollLimits();
            if (this.scrollLimits != null) {
                this.scrollX = Mth.clamp(this.scrollX, this.scrollLimits.x, this.scrollLimits.x + this.scrollLimits.width);
                this.scrollY = Mth.clamp(this.scrollY, this.scrollLimits.y, this.scrollLimits.y + this.scrollLimits.height);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (allowMouseWheel) {
            this.scrollX += scrollX * scrollSensitivity;
            this.scrollY += scrollY * scrollSensitivity;
            recalculateScrollLimits();
            if (this.scrollLimits != null) {
                this.scrollX = Mth.clamp(this.scrollX, this.scrollLimits.x, this.scrollLimits.x + this.scrollLimits.width);
                this.scrollY = Mth.clamp(this.scrollY, this.scrollLimits.y, this.scrollLimits.y + this.scrollLimits.height);
            }
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
