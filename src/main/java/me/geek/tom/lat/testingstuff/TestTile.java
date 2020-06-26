package me.geek.tom.lat.testingstuff;

import me.geek.tom.lat.modapi.CapabilityLATInfo;
import me.geek.tom.lat.modapi.IProvidesLATInfo;
import me.geek.tom.lat.modapi.DefaultLATProvider;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static me.geek.tom.lat.setup.ModSetup.TESTBLOCK_TILE;

public class TestTile extends TileEntity {

    private LazyOptional<IProvidesLATInfo> latInfoCapability = LazyOptional.of(this::createLatInfo);

    public TestTile() {
        super(TESTBLOCK_TILE);
    }

    private IProvidesLATInfo createLatInfo() {
        return new DefaultLATProvider();
    }

    @Override
    public void func_230337_a_(BlockState state, CompoundNBT nbt) {
        super.func_230337_a_(state, nbt);
        latInfoCapability.ifPresent(handler -> handler.read(nbt.get("message")));
    }

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
