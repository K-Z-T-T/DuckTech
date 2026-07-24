package org.quiltmc.users.duckteam.DuckTech.gui.transporter_node;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.quiltmc.users.duckteam.DuckTech.DuckTech;

public class TransporterNodeScreen extends AbstractContainerScreen<TransporterNodeMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(DuckTech.MODID, "textures/screen/kong_da_xiang.png");

    public TransporterNodeScreen(TransporterNodeMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 222; // 箱子高度
    }

    @Override
    protected void renderBg(GuiGraphics gui, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        gui.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        // 绘制27个槽位背景，第一个为正常，其余变灰
        for (int i = 0; i < 27; i++) {
            int x = leftPos + 8 + (i % 9) * 18;
            int y = topPos + 18 + (i / 9) * 18;
            if (i >= 1) {
                // 覆盖灰色半透明
                gui.fill(x, y, x + 16, y + 16, 0x80AAAAAA);
            }
        }
    }

    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float delta) {
        this.renderBackground(gui);
        super.render(gui, mouseX, mouseY, delta);
        this.renderTooltip(gui, mouseX, mouseY);
    }
}