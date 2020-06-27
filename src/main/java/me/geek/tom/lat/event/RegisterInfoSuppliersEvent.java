package me.geek.tom.lat.event;

import me.geek.tom.lat.blockinfo.api.IBlockInfoSupplier;
import net.minecraftforge.eventbus.api.Event;

import java.util.function.Consumer;

/**
 * This event is fired on the forge event bus once during mod initialisation.
 */
public class RegisterInfoSuppliersEvent extends Event {

    private final Consumer<IBlockInfoSupplier> adder;

    public RegisterInfoSuppliersEvent(Consumer<IBlockInfoSupplier> adder) {
        this.adder = adder;
    }

    public void register(IBlockInfoSupplier supplier) {
        adder.accept(supplier);
    }

    @Override
    public boolean isCancelable() {
        return false;
    }
}
