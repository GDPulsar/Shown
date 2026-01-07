package com.pulsar.shown.widget;

import com.pulsar.shown.Shown;
import com.pulsar.shown.UIArea;
import com.pulsar.shown.UIVec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ItemWidget extends WidgetBase {
    private ItemStack item = ItemStack.EMPTY;

    public ItemWidget(UIVec position, UIVec size) {
        super(position, size);
    }

    public <T extends ItemWidget> T setItem(ItemStack item) {
        this.item = item;
        return (T)this;
    }

    public ItemStack getItem() {
        return this.item;
    }

    @Override
    void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta, boolean debug) {
        if (this.item == null) return;
        UIArea drawArea = this.getRenderArea(tickDelta);
        if (debug) Shown.betterFill(guiGraphics, drawArea, this.getRenderDepth(), 0xFF000000);
        Shown.betterRenderItem(guiGraphics, getItem(), drawArea, this.getRenderDepth());
        if (this.contains(mouseX, mouseY)) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, this.item, mouseX, mouseY);
        }
    }
}
