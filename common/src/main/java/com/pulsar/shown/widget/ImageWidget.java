package com.pulsar.shown.widget;

import com.pulsar.shown.UIArea;
import com.pulsar.shown.UIVec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ImageWidget extends WidgetBase {
    private ResourceLocation image = null;

    public ImageWidget(UIVec position, UIVec size) {
        super(position, size);
    }

    public <T extends ImageWidget> T setImage(ResourceLocation image) {
        this.image = image;
        return (T)this;
    }

    public ResourceLocation getImage() {
        return this.image;
    }

    @Override
    void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        if (this.image == null) return;
        UIArea drawArea = this.getRenderArea(tickDelta);
        guiGraphics.blit(this.image, drawArea.x, drawArea.y, drawArea.width, drawArea.height, 0f, 0f, 256, 256, 256, 256);
    }
}
