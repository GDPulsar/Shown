package com.pulsar.shown.widget;

import com.pulsar.shown.Enums;
import com.pulsar.shown.UIArea;
import com.pulsar.shown.UIVec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TextWidget extends WidgetBase {
    private MutableComponent text = Component.empty();
    private double fontSize = 9;
    private Enums.Alignment alignment = Enums.Alignment.TOP_LEFT;

    public TextWidget(UIVec position, UIVec size) {
        super(position, size);
    }

    public <T extends TextWidget> T setText(@NotNull MutableComponent text) {
        this.text = text;
        return (T)this;
    }

    public MutableComponent getText() {
        return this.text;
    }

    public <T extends TextWidget> T setFontSize(double fontSize) {
        this.fontSize = fontSize;
        return (T)this;
    }

    public double getFontSize() {
        return this.fontSize;
    }

    public <T extends TextWidget> T setAlignment(@NotNull Enums.Alignment alignment) {
        this.alignment = alignment;
        return (T)this;
    }

    public Enums.Alignment getAlignment() {
        return this.alignment;
    }

    @Override
    void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        UIArea drawArea = this.getRenderArea(tickDelta);
        List<FormattedCharSequence> wrapped = Minecraft.getInstance().font.split(this.text, drawArea.width);
        double yOff = 0;
        if (alignment.getYAlignment() == Enums.YAlignment.MIDDLE) {
            yOff = drawArea.height / 2d - fontSize * wrapped.size() / 2;
        }
        if (alignment.getYAlignment() == Enums.YAlignment.BOTTOM) {
            yOff = drawArea.height - fontSize * wrapped.size();
        }
        for(FormattedCharSequence line : wrapped) {
            guiGraphics.pose().pushPose();
            int lineWidth = Minecraft.getInstance().font.width(line);
            switch (alignment.getXAlignment()) {
                case LEFT -> {
                    guiGraphics.pose().translate(drawArea.x, drawArea.y + yOff, this.getRenderDepth());
                    guiGraphics.pose().scale((float)fontSize / 9f, (float)fontSize / 9f, 1f);
                }
                case CENTER -> {
                    guiGraphics.pose().translate(drawArea.x + (drawArea.width - lineWidth) / 2f, drawArea.y + yOff, this.getRenderDepth());
                    guiGraphics.pose().scale((float)fontSize / 9f, (float)fontSize / 9f, 1f);
                }
                case RIGHT -> {
                    guiGraphics.pose().translate(drawArea.x + drawArea.width - lineWidth, drawArea.y + yOff, this.getRenderDepth());
                    guiGraphics.pose().scale((float)fontSize / 9f, (float)fontSize / 9f, 1f);
                }
            }
            guiGraphics.drawString(Minecraft.getInstance().font, line, 0, 0, 0xFFFFFFFF, false);
            guiGraphics.pose().popPose();
            yOff += fontSize;
        }
    }
}
