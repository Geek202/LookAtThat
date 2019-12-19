package me.geek.tom.lat.modapi;

import net.minecraft.nbt.INBT;

/**
 * Implement this capability on your block to provide specific information
 * on the LookAtThat overlay.
 */
public interface IProvidesLATInfo {
    /**
     * Gets the string to be rendered under the block information.
     * This is called server-side.
     * @return The string to display
     */
    String getInfo();

    /**
     * This allows the storage of the message; for example, to
     * cache a state so the message doesn't have to wait for a
     * component to update.
     *
     * @param nbt The NBT tag to be read from.
     */
    void read(INBT nbt);

    /**
     *
     * @return The NBT tag to be written to the save.
     */
    INBT write();

    /**
     * Set the current message to the specified new one.
     * This function can do nothing if you have an internal
     * method of updating the message.
     *
     * @param newMessage The new message to set
     */

    void setMessage(String newMessage);
}
