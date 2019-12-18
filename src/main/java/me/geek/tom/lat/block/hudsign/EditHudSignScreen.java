package me.geek.tom.lat.block.hudsign;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.StringTextComponent;

public class EditHudSignScreen extends Screen {
    private TextFieldWidget textWiget;

    public EditHudSignScreen() {
        super(new StringTextComponent(""));
    }

    @Override
    protected void init() {
        super.init();
        this.minecraft.keyboardListener.enableRepeatEvents(true);

        this.textWiget = new TextFieldWidget(this.font, 50, 50, 150, 25, "test");

        // this.textWiget.setCanLoseFocus(false);
        // this.textWiget.changeFocus(true);
        this.textWiget.setTextColor(-1);
        this.textWiget.setDisabledTextColour(-1);
        this.textWiget.setEnableBackgroundDrawing(true);
        this.textWiget.setMaxStringLength(35);
        this.textWiget.setEnabled(true);
        this.textWiget.setResponder(text -> System.out.println(text.toLowerCase()));

        this.children.add(this.textWiget);
    }

    @Override
    public void removed() {
        super.removed();

        this.minecraft.keyboardListener.enableRepeatEvents(false);
    }

    @Override
    public boolean keyPressed(int key, int p_keyPressed_2_, int p_keyPressed_3_) {
        System.out.println(this.textWiget.getText());
        System.out.println(this.textWiget.func_212955_f());

        if (key == 256) {
            this.minecraft.player.closeScreen();
        }

        /*
        if (this.textWiget.keyPressed(key, p_keyPressed_2_, p_keyPressed_3_) || this.textWiget.func_212955_f()) {
            //return true;
        }*/
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
