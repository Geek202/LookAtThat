package me.geek.tom.lat.modapi;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;

public class DefaultLATProvider implements IProvidesLATInfo {

    /**
     * The current message
     */
    private String message = "This is a test!";

    /**
     * Get the message to show
     * @return The message to show on the HUD
     */
    @Override
    public String getInfo() {
        return message;
    }

    /**
     * Reads the message in from and NBT tag.
     * @param nbt The NBT tag to be read from.
     */
    @Override
    public void read(INBT nbt) {
        message = nbt.getString();
    }

    /**
     * Stores the message to an NBT tag.
     * @return The NBT tag to write.
     */
    @Override
    public INBT write() {
        return StringNBT.valueOf(message);
    }

    /**
     * Updates the current message.
     * @param newMessage The new message to set
     */

    @Override
    public void setMessage(String newMessage) {
        this.message = newMessage;
    }
}
