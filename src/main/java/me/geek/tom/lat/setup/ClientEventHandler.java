package me.geek.tom.lat.setup;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.geek.tom.lat.blockinfo.api.BlockInfoLine;
import me.geek.tom.lat.blockinfo.api.BlockInformation;
import me.geek.tom.lat.networking.Networking;
import me.geek.tom.lat.networking.PacketRequestBlockInfo;
import me.geek.tom.lat.overlay.OverlayRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEventHandler {
    private static float lastPitch = Float.MIN_NORMAL;
    private static float lastYaw = Float.MIN_NORMAL;
    private static BlockPos lastPos;

    private static OverlayRenderer renderer;
    public static OverlayRenderer getRenderer() {
        if (renderer == null)
            renderer = new OverlayRenderer();
        return renderer;
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void render(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR)
            getRenderer().render(new MatrixStack());
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void cameraUpdate(EntityViewRenderEvent.CameraSetup event) {
        float pitch = event.getPitch();
        float yaw = event.getYaw();

        if (lastPitch != pitch || lastYaw != yaw) {
            lastPitch = pitch;
            lastYaw = yaw;

            RayTraceResult result = Minecraft.getInstance().objectMouseOver;
            if (result == null) return;

            if (result.getType().equals(RayTraceResult.Type.BLOCK)) {
                BlockRayTraceResult re = (BlockRayTraceResult) result;
                if (!re.getPos().equals(lastPos)) {
                    lastPos = re.getPos();
                }

                if (Minecraft.getInstance().player.getServerBrand().toLowerCase().equals("forge")) {
                    Networking.INSTANCE.sendToServer(
                            new PacketRequestBlockInfo(
                                    Minecraft.getInstance().world.func_234923_W_(),
                                    ((BlockRayTraceResult) result).getPos()
                            )
                    );
                    BlockState state = Minecraft.getInstance().world.getBlockState(((BlockRayTraceResult) result).getPos());
                    Item item = state.getBlock().getItem(Minecraft.getInstance().world, ((BlockRayTraceResult) result).getPos(), state).getItem();
                    if (item.equals(getRenderer().getCurrentItem()))
                        return;
                    getRenderer().setItem(item);
                    getRenderer().currentBlockInfo = new BlockInformation();
                    //getRenderer().currentBlockInfo.addInformation(new BlockInfoLine("Waiting for server...", 0xFF0000));
                } else {
                    BlockState state = Minecraft.getInstance().world.getBlockState(((BlockRayTraceResult) result).getPos());
                    getRenderer().setItem(state.getBlock().getItem(Minecraft.getInstance().world, ((BlockRayTraceResult) result).getPos(), state).getItem());
                    getRenderer().currentBlockInfo = new BlockInformation();
                }
            } else if (result.getType().equals(RayTraceResult.Type.MISS)) {
                getRenderer().setItem(Items.AIR);
                getRenderer().currentBlockInfo = new BlockInformation();
            }
        }
    }
}
