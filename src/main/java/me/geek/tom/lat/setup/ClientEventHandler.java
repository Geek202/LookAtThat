package me.geek.tom.lat.setup;

import me.geek.tom.lat.networking.Networking;
import me.geek.tom.lat.networking.PacketRequestBlockInfo;
import me.geek.tom.lat.overlay.OverlayRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
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
        switch (event.getType()) {
            case HOTBAR:
                getRenderer().render();
                break;
            default:
                break;
        }
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
            if (result.getType().equals(RayTraceResult.Type.BLOCK)) {
                BlockRayTraceResult re = (BlockRayTraceResult) result;
                if (!re.getPos().equals(lastPos)) {
                    getRenderer().setHasAdditionalData(false);
                    lastPos = re.getPos();
                }

                if (Minecraft.getInstance().player.getServerBrand().toLowerCase().equals("forge")) {
                    Networking.INSTANCE.sendToServer(
                            new PacketRequestBlockInfo(
                                    Minecraft.getInstance().player.dimension,
                                    ((BlockRayTraceResult) result).getPos()
                            )
                    );

                } else {
                    getRenderer().setHasAdditionalData(false);
                    BlockState state = Minecraft.getInstance().world.getBlockState(((BlockRayTraceResult) result).getPos());
                    getRenderer().setItem(state.getBlock().getItem(Minecraft.getInstance().world, ((BlockRayTraceResult) result).getPos(), state).getItem());
                }
            } else if (result.getType().equals(RayTraceResult.Type.MISS)) {
                getRenderer().setItem(Items.AIR);
            }
        }
    }
}
