package me.geek.tom.lat.modapi;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class CapabilityLATInfo {

    /**
     * The capability you need to implement to allow custom messages.
     * See {@link DefaultLATProvider}
     */
    @CapabilityInject(IProvidesLATInfo.class)
    public static Capability<IProvidesLATInfo> LAT_INFO_CAPABILITY = null;

    /**
     * Registers the capablity.
     *
     * <b>DO NOT CALL YOURSELF!</b>
     */
    public static void register() {
        CapabilityManager.INSTANCE.register(IProvidesLATInfo.class, new Capability.IStorage<IProvidesLATInfo>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<IProvidesLATInfo> capability, IProvidesLATInfo instance, Direction side) {
                return instance.write();
            }

            @Override
            public void readNBT(Capability<IProvidesLATInfo> capability, IProvidesLATInfo instance, Direction side, INBT nbt) {
                instance.read(nbt);
            }
        }, () -> {throw new UnsupportedOperationException();});
    }
}
