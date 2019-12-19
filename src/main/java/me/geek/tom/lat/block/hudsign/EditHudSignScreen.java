package me.geek.tom.lat.block.hudsign;

import me.geek.tom.lat.networking.Networking;
import me.geek.tom.lat.networking.PacketUpdateSignText;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.dimension.DimensionType;

public class EditHudSignScreen extends Screen {
    private TextFieldWidget textWiget;
    private BlockPos pos;
    private DimensionType type;
    private String initialMessage;

    public EditHudSignScreen(BlockPos pos, DimensionType type, String initialMessage) {
        super(new StringTextComponent(""));
        this.pos = pos;
        this.type = type;
        this.initialMessage = initialMessage;
    }

    @Override
    protected void init() {
        super.init();
        this.minecraft.keyboardListener.enableRepeatEvents(true);

        this.textWiget = new TextFieldWidget(this.font, 50, 50, 150, 25, "Message");

        // this.textWiget.setCanLoseFocus(false);
        // this.textWiget.changeFocus(true);
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
    public void removed() {
        super.removed();

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
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);

        this.textWiget.render(mouseX, mouseY, partialTicks);
        this.textWiget.renderButton(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
