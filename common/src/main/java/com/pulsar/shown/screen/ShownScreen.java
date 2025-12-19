package com.pulsar.shown.screen;

import com.pulsar.shown.Shown;
import com.pulsar.shown.UIArea;
import com.pulsar.shown.UIVec;
import com.pulsar.shown.widget.WidgetBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.List;

public abstract class ShownScreen extends Screen {
    private final WidgetBase root;

    public ShownScreen() {
        super(Component.empty());
        this.root = new WidgetBase(new UIVec(0, 0, 0, 0), new UIVec(1, 1, 0, 0));
    }

    @Override
    protected void init() {
        lastNanos = System.nanoTime();
        this.root.onInit();
        this.root.updateArea(new UIArea(0, 0, Minecraft.getInstance().getWindow().getGuiScaledWidth(), Minecraft.getInstance().getWindow().getGuiScaledHeight()));
    }

    public void addWidget(WidgetBase widget) {
        this.root.addWidget(widget);
    }

    public void removeWidget(WidgetBase widget) {
        this.root.removeWidget(widget);
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        this.root.updateArea(new UIArea(0, 0, width, height));
    }

    @Override
    public final void tick() {
        this.root.preTick();
        doTick();
        this.root.tick();
    }

    public void doTick() {

    }

    private void addChildWidgets(WidgetBase widget, int depth, List<WidgetBase> parents, List<Triple<WidgetBase, Integer, List<WidgetBase>>> widgetList) {
        parents.add(widget);
        List<WidgetBase> children = widget.getChildren();
        for (int i = 0; i < children.size(); i++) {
            widgetList.add(Triple.of(children.get(i), depth * 100 + i, List.copyOf(parents)));
            addChildWidgets(children.get(i), depth + 1, parents, widgetList);
        }
        parents.remove(widget);
    }

    float tick = 0f;
    long lastNanos = 0;

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        long diffNanos = System.nanoTime() - lastNanos;
        tick += diffNanos / 50_000_000f;
        float tickDelta = tick - Mth.floor(tick);
        lastNanos = System.nanoTime();

        Shown.LOGGER.info("tickdelta: {}", tickDelta);

        List<Triple<WidgetBase, Integer, List<WidgetBase>>> widgets = new ArrayList<>();
        addChildWidgets(this.root, 0, new ArrayList<>(), widgets);

        widgets.sort((a, b) ->
                (a.getLeft().getZIndex() * 10000 + a.getMiddle()) - (b.getLeft().getZIndex() * 10000 + a.getMiddle())
        );
        for (Triple<WidgetBase, Integer, List<WidgetBase>> pair : widgets) {
            for (WidgetBase parent : pair.getRight()) {
                parent.preRenderChild(guiGraphics, mouseX, mouseY, tickDelta);
            }
            pair.getLeft().preRender(guiGraphics, mouseX, mouseY, tickDelta);
            pair.getLeft().render(guiGraphics, mouseX, mouseY, tickDelta);
            pair.getLeft().postRender(guiGraphics, mouseX, mouseY, tickDelta);
            for (WidgetBase parent : pair.getRight()) {
                parent.postRenderChild(guiGraphics, mouseX, mouseY, tickDelta);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.root.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.root.mouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        this.root.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        this.root.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers) {
        this.root.keyPressed(key, scanCode, modifiers);
        return super.keyPressed(key, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int key, int scanCode, int modifiers) {
        this.root.keyReleased(key, scanCode, modifiers);
        return super.keyReleased(key, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char c, int modifiers) {
        this.root.charTyped(c, modifiers);
        return super.charTyped(c, modifiers);
    }
}
