package me.geek.tom.lat.blockinfo.api;

import com.google.common.collect.Lists;
import net.minecraft.network.PacketBuffer;

import java.util.List;

public class BlockInformation {

    private final List<BlockInfoLine> information = Lists.newArrayList();
    private final boolean canHarvest;

    public BlockInformation(boolean canHarvest) {
        this.canHarvest = canHarvest;
    }

    public void addInformation(BlockInfoLine info) {
        information.add(info);
    }

    public List<BlockInfoLine> getInformation() {
        return information;
    }

    public void serializeToPacket(PacketBuffer buf) {
        int len = information.size();
        buf.writeBoolean(canHarvest);
        buf.writeInt(len);
        for (BlockInfoLine ln : information) {
            buf.writeString(ln.getLine(), 32767);
            buf.writeInt(ln.getColour());
        }
    }

    public boolean canHarvest() {
        return canHarvest;
    }

    public static BlockInformation fromPacket(PacketBuffer buf) {
        BlockInformation ret = new BlockInformation(buf.readBoolean());
        int len = buf.readInt();
        for (int i = 0; i < len; i++) {
            ret.addInformation(new BlockInfoLine(
                    buf.readString(32767),
                    buf.readInt()
            ));
        }
        return ret;
    }

}
