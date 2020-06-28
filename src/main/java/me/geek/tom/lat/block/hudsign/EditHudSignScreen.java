package me.geek.tom.lat.block.hudsign;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.geek.tom.lat.networking.Networking;
import me.geek.tom.lat.networking.PacketUpdateSignText;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class EditHudSignScreen extends Screen {
    private TextFieldWidget textWiget;
    private BlockPos pos;
    private RegistryKey<World> type;

    public EditHudSignScreen(BlockPos pos, RegistryKey<World> type/*, String initialMessage*/) {
        super(new StringTextComponent(""));
        this.pos = pos;
        this.type = type;
    }

    @Override
    protected void func_231160_c_() { // init
        super.func_231160_c_();
        this.field_230706_i_.keyboardListener.enableRepeatEvents(true);

        int xpos = (this.field_230708_k_ / 2) - 75;
        int ypos = (this.field_230709_l_ / 2) - 12;

        this.textWiget = new TextFieldWidget(this.field_230712_o_,
                xpos, ypos, 150, 25, new StringTextComponent("Message"));

        this.textWiget.setTextColor(-1);
        this.textWiget.setDisabledTextColour(-1);
        this.textWiget.setEnableBackgroundDrawing(true);
        this.textWiget.setMaxStringLength(35);
        this.textWiget.setEnabled(true);
        this.textWiget.setResponder(text -> Networking.INSTANCE.sendToServer(new PacketUpdateSignText(text, this.pos, this.type)));

        // this.textWiget.setText(initialMessage); @TODO  Request message from server.

        this.field_230705_e_.add(this.textWiget);
    }

    @Override
    public void func_231164_f_() { // remove
        super.func_231164_f_();

        this.field_230706_i_.keyboardListener.enableRepeatEvents(false);
    }

    @Override
    public boolean func_231046_a_(int key, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (key == 256) {
            this.field_230706_i_.player.closeScreen();
        }

        return super.func_231046_a_(key, p_keyPressed_2_, p_keyPressed_3_);
    }

    @Override
    public void func_231023_e_() {
        super.func_231023_e_();
        this.textWiget.tick();
    }

    @Override
    public void func_230430_a_(MatrixStack stack, int mouseX, int mouseY, float partialTicks) { // Render?
        super.func_230430_a_(stack, mouseX, mouseY, partialTicks);

        this.textWiget.func_230430_a_(stack, mouseX, mouseY, partialTicks);
        this.textWiget.func_230431_b_(stack, mouseX, mouseY, partialTicks); // idk, but it looks about right.
    }

    @Override
    public boolean func_231177_au__() {
        return false;
    }
}
