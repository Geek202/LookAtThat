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

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class EditHudSignScreen extends Screen {
    private TextFieldWidget textWiget;
    private final BlockPos pos;
    private final RegistryKey<World> type;

    public EditHudSignScreen(BlockPos pos, RegistryKey<World> type/*, String initialMessage*/) {
        super(new StringTextComponent(""));
        this.pos = pos;
        this.type = type;
    }

    @Override
    protected void init() {
        super.init();
        this.minecraft.keyboardListener.enableRepeatEvents(true);

        int xpos = (this.width / 2) - 75;
        int ypos = (this.height / 2) - 12;

        this.textWiget = new TextFieldWidget(this.font,
                xpos, ypos, 150, 25, new StringTextComponent("Message"));

        this.textWiget.setTextColor(-1);
        this.textWiget.setDisabledTextColour(-1);
        this.textWiget.setEnableBackgroundDrawing(true);
        this.textWiget.setMaxStringLength(35);
        this.textWiget.setEnabled(true);
        this.textWiget.setResponder(text -> Networking.INSTANCE.sendToServer(new PacketUpdateSignText(text, this.pos, this.type)));

        // this.textWiget.setText(initialMessage); @TODO  Request message from server.

        this.children.add(this.textWiget);
    }

    @Override
    public void onClose() {
        super.onClose();

        this.minecraft.keyboardListener.enableRepeatEvents(false);
    }

    @Override
    public boolean keyPressed(int key, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (key == 256) {
            this.minecraft.player.closeScreen();
        }

        return super.keyPressed(key, p_keyPressed_2_, p_keyPressed_3_);
    }

    @Override
    public void tick() {
        super.tick();
        this.textWiget.tick();
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);

        this.textWiget.render(stack, mouseX, mouseY, partialTicks);
        this.textWiget.renderButton(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
