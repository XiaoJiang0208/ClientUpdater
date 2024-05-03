package com.xiaojiang.clientupdater.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.xiaojiang.clientupdater.Updater;

//import org.slf4j.Logger;

//import com.mojang.logging.LogUtils;
import com.xiaojiang.clientupdater.Update;

@OnlyIn(Dist.CLIENT)
public class UpdateScreen extends Screen {
    private static final Component TITLE = Component.translatable("gui.clientupdater.updating");
    private Update update = new Update();
    private String serverURL;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    private static Button bt;
    private Updater updater = new Updater();
    // private static final Logger LOGGER = LogUtils.getLogger();

    public UpdateScreen(Update up, String url) {
        super(TITLE);
        this.update = up;
        this.serverURL = url;
        this.updater.update = this.update;
        this.updater.server_url = this.serverURL;
        this.updater.start();
    }

    protected void init() {
        this.layout.addToHeader(new StringWidget(this.getTitle(), this.font));
        // 添加按钮
        bt = this.layout.addToFooter(Button.builder(Component.translatable("gui.clientupdater.restart"), (v) -> {
            this.minecraft.stop();
        }).bounds(this.width / 2 - 100, 140, 200, 20).build());
        this.layout.arrangeElements();
        this.layout.visitWidgets(this::addRenderableWidget);
        bt.visible = updater.isComplete;
    }

    public void render(GuiGraphics p_281469_, int p_96053_, int p_96054_, float p_96055_) {
        this.renderBackground(p_281469_);
        bt.visible = updater.isComplete;
        super.render(p_281469_, p_96053_, p_96054_, p_96055_);
    }

    public void tick() {
        bt.visible = updater.isComplete;
        super.tick();
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }
}
