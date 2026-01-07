package com.pulsar.shown;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Shown {
    public static final String MOD_ID = "shown";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {

    }

    public static void betterFill(GuiGraphics graphics, UIArea area, float depth, int color) {
        Matrix4f matrix4f = graphics.pose().last().pose();
        VertexConsumer vertexConsumer = graphics.bufferSource().getBuffer(RenderType.gui());
        vertexConsumer.addVertex(matrix4f, area.x, area.y, depth).setColor(color);
        vertexConsumer.addVertex(matrix4f, area.x, area.y + area.height, depth).setColor(color);
        vertexConsumer.addVertex(matrix4f, area.x + area.width, area.y + area.height, depth).setColor(color);
        vertexConsumer.addVertex(matrix4f, area.x + area.width, area.y, depth).setColor(color);
    }

    public static void betterRenderItem(GuiGraphics graphics, ItemStack stack, UIArea drawArea, float depth) {
        if (!stack.isEmpty()) {
            BakedModel bakedModel = Minecraft.getInstance().getItemRenderer().getModel(stack, Minecraft.getInstance().level, null, 0);
            graphics.pose().pushPose();
            graphics.pose().translate(drawArea.x + drawArea.width / 2f, drawArea.y + drawArea.height / 2f, depth);

            try {
                graphics.pose().scale(drawArea.width, -drawArea.height, 1.0F);
                boolean bl = !bakedModel.usesBlockLight();
                if (bl) {
                    Lighting.setupForFlatItems();
                }

                Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.GUI, false, graphics.pose(), graphics.bufferSource(), 15728880, OverlayTexture.NO_OVERLAY, bakedModel);
                graphics.flush();
                if (bl) {
                    Lighting.setupFor3DItems();
                }
            } catch (Throwable throwable) {
                CrashReport crashReport = CrashReport.forThrowable(throwable, "Rendering item");
                CrashReportCategory crashReportCategory = crashReport.addCategory("Item being rendered");
                crashReportCategory.setDetail("Item Type", () -> String.valueOf(stack.getItem()));
                crashReportCategory.setDetail("Item Components", () -> String.valueOf(stack.getComponents()));
                crashReportCategory.setDetail("Item Foil", () -> String.valueOf(stack.hasFoil()));
                throw new ReportedException(crashReport);
            }
            graphics.pose().popPose();
        }
    }

    public static void betterBlit(GuiGraphics graphics, ResourceLocation id, UIArea drawArea, float depth, float u1, float v1, float u2, float v2) {
        RenderSystem.setShaderTexture(0, id);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        Matrix4f matrix4f = graphics.pose().last().pose();
        BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.addVertex(matrix4f, drawArea.x, drawArea.y, depth).setUv(u1, v1);
        bufferBuilder.addVertex(matrix4f, drawArea.x, drawArea.y + drawArea.height, depth).setUv(u1, v2);
        bufferBuilder.addVertex(matrix4f, drawArea.x + drawArea.width, drawArea.y + drawArea.height, depth).setUv(u2, v2);
        bufferBuilder.addVertex(matrix4f, drawArea.x + drawArea.width, drawArea.y, depth).setUv(u2, v1);
        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
    }
}
