package me.geek.tom.lat.blockinfo.api;

import com.google.common.collect.Lists;
import net.minecraft.network.PacketBuffer;

import java.util.List;

public class BlockInformation {

    private final List<BlockInfoLine> information = Lists.newArrayList();

    public void addInformation(BlockInfoLine info) {
        information.add(info);
    }

    public List<BlockInfoLine> getInformation() {
        return information;
    }

    public void serializeToPacket(PacketBuffer buf) {
        int len = information.size();
        buf.writeInt(len);
        for (int i = 0; i < len; i++) {
            BlockInfoLine ln = information.get(i);
            buf.writeString(ln.getLine(), 32767);
            buf.writeInt(ln.getColour());
        }
    }

    public static BlockInformation fromPacket(PacketBuffer buf) {
        BlockInformation ret = new BlockInformation();
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
