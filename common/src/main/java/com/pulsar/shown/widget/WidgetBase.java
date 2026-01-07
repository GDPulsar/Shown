package com.pulsar.shown.widget;

import com.pulsar.shown.UIArea;
import com.pulsar.shown.UIVec;
import com.pulsar.shown.constraint.WidgetConstraint;
import com.pulsar.shown.layout.WidgetLayout;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec2;

import java.util.ArrayList;
import java.util.List;

public class WidgetBase {
    WidgetBase parent;

    UIVec position;
    float rotation = 0f;
    UIVec size;

    Vec2 anchorPoint = new Vec2(0f, 0f);
    int zIndex = 0;

    boolean focused = false;
    boolean enabled = true;
    boolean visible = true;

    boolean shouldLerp = false;

    WidgetLayout layout = null;
    WidgetConstraint constraint = null;

    UIArea lastArea;
    UIArea uiArea;
    boolean areaChanged = false;

    List<WidgetBase> children = new ArrayList<>();

    boolean initialized = false;

    public WidgetBase(UIVec position, UIVec size) {
        this.position = position;
        this.size = size;
    }

    public <T extends WidgetBase> T setPosition(UIVec position, boolean silent) {
        this.position = position;
        if (this.initialized && !silent) {
            this.updateArea();
        }
        return (T)this;
    }
    public <T extends WidgetBase> T setPosition(UIVec position) {
        return this.setPosition(position, false);
    }
    public UIVec getPosition() {
        return this.position;
    }

    public <T extends WidgetBase> T setRotation(float rotation) {
        this.rotation = rotation;
        return (T)this;
    }
    public float getRotation() {
        return this.rotation;
    }

    public <T extends WidgetBase> T setSize(UIVec size, boolean silent) {
        this.size = size;
        if (this.initialized && !silent) {
            this.updateArea();
        }
        return (T)this;
    }
    public <T extends WidgetBase> T setSize(UIVec size) {
        return this.setSize(size, false);
    }
    public UIVec getSize() {
        return this.size;
    }

    public <T extends WidgetBase> T setParent(WidgetBase parent) {
        this.parent = parent;
        return (T)this;
    }
    public WidgetBase getParent() {
        return this.parent;
    }

    public <T extends WidgetBase> T setLayout(WidgetLayout layout) {
        layout.setParent(this);
        this.layout = layout;
        return (T)this;
    }
    public WidgetLayout getLayout() {
        return this.layout;
    }

    public <T extends WidgetBase> T setConstraint(WidgetConstraint constraint) {
        constraint.setParent(this);
        this.constraint = constraint;
        return (T)this;
    }
    public WidgetConstraint getConstraint() {
        return this.constraint;
    }

    public <T extends WidgetBase> T setAnchorPoint(Vec2 anchorPoint) {
        this.anchorPoint = new Vec2(Math.clamp(anchorPoint.x, 0f, 1f), Math.clamp(anchorPoint.y, 0f, 1f));
        return (T)this;
    }
    public Vec2 getAnchorPoint() {
        return this.anchorPoint;
    }

    public <T extends WidgetBase> T setZIndex(int zIndex) {
        this.zIndex = zIndex;
        return (T)this;
    }
    public int getZIndex() {
        return this.zIndex;
    }

    public <T extends WidgetBase> T setShouldLerp(boolean shouldLerp) {
        this.shouldLerp = shouldLerp;
        return (T)this;
    }
    public boolean getShouldLerp() {
        return this.shouldLerp;
    }

    public void onInit() {
        this.initialized = true;
        for (WidgetBase child : this.children) {
            child.onInit();
        }
    }

    public void preTick() {
        if (this.areaChanged) {
            this.lastArea = this.uiArea;
        }

        for (WidgetBase child : this.children) {
            child.preTick();
        }
    }

    public void tick() {
        for (WidgetBase child : this.children) {
            child.tick();
        }
    }

    public <T extends WidgetBase> T addWidget(WidgetBase widget) {
        widget.setParent(this);
        this.children.add(widget);
        this.onChildAdded(widget);
        return (T)this;
    }
    public <T extends WidgetBase> T removeWidget(WidgetBase widget) {
        if (this.children.remove(widget)) {
            this.onChildRemoved(widget);
        }
        return (T)this;
    }

    void onChildAdded(WidgetBase child) {}
    void onChildRemoved(WidgetBase child) {}

    public List<WidgetBase> getChildren() {
        return this.children;
    }

    public UIArea getArea() {
        return this.uiArea;
    }

    public UIArea getRenderArea(float tickDelta) {
        if (shouldLerp && this.lastArea != null) {
            return this.lastArea.lerp(this.uiArea, tickDelta);
        }
        return this.uiArea;
    }

    public void updateArea() {
        this.updateArea(this.parent.getArea());
    }

    public void updateArea(UIArea parentArea) {
        this.lastArea = uiArea;
        this.areaChanged = true;
        this.uiArea = parentArea.getSubArea(this.position, this.size, this.anchorPoint);
        if (this.constraint != null) this.constraint.update();
        for (WidgetBase child : this.children) {
            child.updateArea();
        }
        if (this.layout != null) this.layout.update();
    }

    public boolean contains(double x, double y) {
        return this.uiArea.isInArea(x, y);
    }

    public boolean isFocused() {
        return this.focused;
    }
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public boolean isEnabled() {
        return this.enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isVisible() {
        return this.visible;
    }
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (WidgetBase child : this.children) {
            if (child.isEnabled()) {
                if (child.mouseClicked(mouseX, mouseY, button)) return true;
            }
        }
        return false;
    }
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (WidgetBase child : this.children) {
            if (child.isEnabled()) {
                if (child.mouseReleased(mouseX, mouseY, button)) return true;
            }
        }
        return false;
    }
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        for (WidgetBase child : this.children) {
            if (child.isEnabled()) {
                if (child.mouseDragged(mouseX, mouseY, button, dx, dy)) return true;
            }
        }
        return false;
    }
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        for (WidgetBase child : this.children) {
            if (child.isEnabled()) {
                if (child.mouseScrolled(mouseX, mouseY, scrollX, scrollY)) return true;
            }
        }
        return false;
    }

    public boolean keyPressed(int key, int scanCode, int modifiers) {
        for (WidgetBase child : this.children) {
            if (child.isEnabled()) {
                if (child.keyPressed(key, scanCode, modifiers)) return true;
            }
        }
        return false;
    }
    public boolean keyReleased(int key, int scanCode, int modifiers) {
        for (WidgetBase child : this.children) {
            if (child.isEnabled()) {
                if (child.keyReleased(key, scanCode, modifiers)) return true;
            }
        }
        return false;
    }
    public boolean charTyped(char c, int modifiers) {
        for (WidgetBase child : this.children) {
            if (child.isEnabled()) {
                if (child.charTyped(c, modifiers)) return true;
            }
        }
        return false;
    }

    private int getDepth(WidgetBase current, int depth) {
        if (current.getParent() == null) return depth;
        return getDepth(current.getParent(), depth + 1);
    }

    float getRenderDepth() {
        return getZIndex() * 10000 + getDepth(this, 0) + this.getParent().getChildren().indexOf(this);
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta, boolean debug) {
        if (!isVisible()) return;
        renderBackground(guiGraphics, mouseX, mouseY, tickDelta, debug);
        renderContent(guiGraphics, mouseX, mouseY, tickDelta, debug);
        renderOverlay(guiGraphics, mouseX, mouseY, tickDelta, debug);
    }

    void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta, boolean debug) {}
    void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta, boolean debug) {}
    void renderOverlay(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta, boolean debug) {}

    public void preRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta, boolean debug) {}
    public void postRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta, boolean debug) {}
    public void preRenderChild(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {}
    public void postRenderChild(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {}
}
