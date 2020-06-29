package me.geek.tom.lat.overlay;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import me.geek.tom.lat.Config;
import me.geek.tom.lat.blockinfo.api.BlockInfoLine;
import me.geek.tom.lat.blockinfo.api.BlockInformation;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.ModList;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.List;

public class OverlayRenderer extends Screen {

    // private static final ResourceLocation BAR_TEXTURE = new ResourceLocation(LookAtThat.MODID, "textures/overlay/bar");

    private Item currentItem = Items.AIR;

    public BlockInformation currentBlockInfo;

    public OverlayRenderer() {
        super(new StringTextComponent(""));
    }

    public void setItem(Item item) {
        currentItem = item;
    }

    public Item getCurrentItem() {
        return currentItem;
    }

    @SuppressWarnings("deprecation")
    public void render(MatrixStack stack) {
        if (currentBlockInfo == null) return;

        RenderSystem.pushMatrix();

        Minecraft mc = Minecraft.getInstance();
        ItemStack itemStack = new ItemStack(currentItem, 1);

        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);

        int width = 0;

        List<BlockInfoLine> information = currentBlockInfo.getInformation();
        for (BlockInfoLine ln : information)
            width = Math.max(
                    width,
                    mc.fontRenderer.getStringWidth(ln.getLine())
            );

        width += 35;

        int height = 12 + 10 * information.size();
        if (information.size() == 1)
            height += 10;

        preconfigureRender(width);

        func_238467_a_(stack,5, 5, width, height, 0x88000000);

        boolean canBreak = currentBlockInfo.canHarvest();
        if (!information.isEmpty()) {
            renderBox(stack, 5, 5, width-5, height-5, canBreak ? 0xFF00FF00 : 0xFFFF0000);
        }

        this.renderItemStack(itemStack, 10, 10);

        int i = 0;
        for (BlockInfoLine line : information) {
            mc.fontRenderer.func_238421_b_(stack, line.getLine(), 30, 10 + i * 10, line.getColour());
            i++;
        }
        RenderSystem.popMatrix();
    }

    private void renderBox(MatrixStack stack, int x, int y, int width, int height, int col) {
        /*BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);*/

        line(stack, x, y, width, false, col);
        line(stack, x, y+height-1, width, false, col);
        line(stack, x, y, height, true, col);
        line(stack, x+width-1, y, height, true, col);

        /*bufferbuilder.finishDrawing();
        WorldVertexBufferUploader.draw(bufferbuilder);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();*/
    }

    private void line(MatrixStack stack, int x, int y, int length, boolean vertical, int col) {
        if (vertical) {
            /*builder.pos((x+1),      (y),          0.0D).color(red, green, blue, alpha).endVertex();
            builder.pos((x+1),      (y + length), 0.0D).color(red, green, blue, alpha).endVertex();
            builder.pos((x),        (y + length), 0.0D).color(red, green, blue, alpha).endVertex();
            builder.pos((x),        (y),          0.0D).color(red, green, blue, alpha).endVertex();*/
            func_238467_a_(stack, x, y, x+1, y+length, col);
        } else {
            /*builder.pos((x+length), (y),          0.0D).color(red, green, blue, alpha).endVertex();
            builder.pos((x+length), (y+1),        0.0D).color(red, green, blue, alpha).endVertex();
            builder.pos((x),        (y+1),        0.0D).color(red, green, blue, alpha).endVertex();
            builder.pos((x),        (y),          0.0D).color(red, green, blue, alpha).endVertex();*/
            func_238467_a_(stack, x, y, x+length, y+1, col);
        }
    }

    @SuppressWarnings("deprecation")
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

    @SuppressWarnings("deprecation")
    private void preconfigureRender(int width) {
        if (Config.CENTER_HUD.get()) {
            int screenWidth = Minecraft.getInstance().getMainWindow().getScaledWidth();
            int screenCenterPosition = ((screenWidth - 10) / 2);
            int translateToCenter = screenCenterPosition - (width / 2);

            RenderSystem.translatef((float) translateToCenter, 0f, 0f);
        }
    }

    @SuppressWarnings("ConstantConditions")
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
