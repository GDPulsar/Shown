package com.pulsar.shown.widget;

import com.pulsar.shown.UIArea;
import com.pulsar.shown.UIVec;
import net.minecraft.client.gui.GuiGraphics;

public class MaskWidget extends WidgetBase {
    public MaskWidget(UIVec position, UIVec size) {
        super(position, size);
    }

    @Override
    public void preRenderChild(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        UIArea area = this.getArea();
        guiGraphics.pose().pushPose();
        guiGraphics.enableScissor(area.x, area.y, area.x + area.width, area.y + area.height);
    }

    @Override
    public void postRenderChild(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        guiGraphics.disableScissor();
        guiGraphics.pose().popPose();
    }
}
