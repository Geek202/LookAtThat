package me.geek.tom.lat.block.hudsign;

import me.geek.tom.lat.modapi.CapabilityLATInfo;
import me.geek.tom.lat.modapi.DefaultLATProvider;
import me.geek.tom.lat.modapi.IProvidesLATInfo;
import me.geek.tom.lat.setup.ModSetup;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HudSignTile extends TileEntity {

    private LazyOptional<IProvidesLATInfo> latInfoCapability = LazyOptional.of(this::createLatInfo);

    public HudSignTile() {
        super(ModSetup.HUDSIGN_TILE);
    }

    private IProvidesLATInfo createLatInfo() {
        return new DefaultLATProvider();
    }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);
        latInfoCapability.ifPresent(handler -> {
            INBT message = nbt.get("message");
            if (message != null) {
                handler.read(message);
            }
        });
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        latInfoCapability.ifPresent(handler -> {
            StringNBT msg = (StringNBT) handler.write();
            nbt.put("message", msg);
        });

        return super.write(nbt);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityLATInfo.LAT_INFO_CAPABILITY) {
            return latInfoCapability.cast();
        }
        return super.getCapability(cap, side);
    }
}
