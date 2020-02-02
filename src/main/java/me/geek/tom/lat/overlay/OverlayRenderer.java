package me.geek.tom.lat.overlay;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.ModList;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;

public class OverlayRenderer extends Screen {

    // private static final ResourceLocation BAR_TEXTURE = new ResourceLocation(LookAtThat.MODID, "textures/overlay/bar");

    private Item currentItem = Items.AIR;
    private Block currentBlock = Blocks.AIR;
    private boolean useBlock;
    private boolean hasAdditionalData = false;
    private String[] additionalData = {};

    public OverlayRenderer() {
        super(new StringTextComponent(""));
    }

    public void setItem(Item item) {
        currentItem = item;
        useBlock = false;
    }
    public void setBlock(Block block) {
        currentBlock = block;
        useBlock = true;
    }
    public void setHasAdditionalData(boolean hasAdditionalData) {
        this.hasAdditionalData = hasAdditionalData;
    }
    public void setAdditionalData(String[] additionalData) {
        this.additionalData = additionalData;
    }

    public void render() {

        if (this.currentItem.equals(Items.AIR) && !this.useBlock)
            return;

        if (this.currentBlock.equals(Blocks.AIR) && this.useBlock)
            return;

        RenderSystem.pushMatrix();

        Minecraft mc = Minecraft.getInstance();
        ItemStack itemStack = new ItemStack(currentItem, 1);

        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        String displayName = this.useBlock ? currentBlock.getNameTextComponent().getString() : itemStack.getDisplayName().getString();
        String modName;

        if (!this.useBlock) {
            modName = getModName(itemStack);
        } else {
            modName = getModName(currentBlock);
        }

        int width = Math.max(
                mc.fontRenderer.getStringWidth(displayName),
                mc.fontRenderer.getStringWidth(modName)
        );

        if (this.hasAdditionalData)
            for (String s : this.additionalData)
                width = Math.max(width, mc.fontRenderer.getStringWidth(s));
        width += 35;

        fill(5, 5, width, this.hasAdditionalData ? 32 + 10 * this.additionalData.length : 32, 0x88000000);

        if (!this.useBlock)
            this.renderItemStack(itemStack, 10, 10);
        mc.fontRenderer.drawStringWithShadow(displayName, 32, 10, 0xFFFFFF);
        mc.fontRenderer.drawStringWithShadow(modName, 32, 20, 0x0055FF);
        if (this.hasAdditionalData) {
            int i = 0;
            for (String s : this.additionalData) {
                mc.fontRenderer.drawStringWithShadow(s, 32 + i * 10, 30, 0x888800);
                i++;
            }
        }
        RenderSystem.popMatrix();
    }

    private void renderItemStack(ItemStack itm, int x, int y) {
        ItemRenderer itemRender = Minecraft.getInstance().getItemRenderer();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0f);

        if (!itm.isEmpty()) {
            RenderSystem.pushMatrix();
            RenderSystem.translatef(0.0F, 0.0F, 32.0F);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableRescaleNormal();
            RenderSystem.enableLighting();
            short short1 = 240;
            short short2 = 240;
            RenderHelper.setupGui3DDiffuseLighting();
            try {
                itemRender.renderItemAndEffectIntoGUI(itm, x, y);
            } catch (Exception ignored) {
            }
            RenderSystem.popMatrix();
            RenderSystem.disableRescaleNormal();
            RenderSystem.disableLighting();
        }
    }

    @Nullable
    private String getModName(ItemStack itemStack) {
        if (!itemStack.isEmpty()) {
            Item item = itemStack.getItem();
            String modId = item.getCreatorModId(itemStack);
            if (modId != null) {
                return ModList.get().getModContainerById(modId)
                        .map(modContainer -> modContainer.getModInfo().getDisplayName())
                        .orElse(StringUtils.capitalize(modId));
            }
        }
        return null;
    }

    @Nullable
    private String getModName(Block block) {
        if (!block.equals(Blocks.AIR)) {
            String modId = block.getRegistryName().getNamespace();
            return ModList.get().getModContainerById(modId)
                    .map(modContainer -> modContainer.getModInfo().getDisplayName())
                    .orElse(StringUtils.capitalize(modId));
        }
        return null;
    }
}
