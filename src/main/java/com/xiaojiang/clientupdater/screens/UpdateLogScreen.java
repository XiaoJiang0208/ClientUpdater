package com.xiaojiang.clientupdater.screens;

import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UpdateLogScreen extends Screen {
    private static final Component TITLE = Component.translatable("更新日志");
    private static String serverURL;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    // private final InfoPanel upDateLogs;

    public UpdateLogScreen(String serverurl) {
        super(TITLE);
        serverURL = serverurl;
    }

    protected void init() {
        // super.init();
        this.layout.addToHeader(new StringWidget(this.getTitle(), this.font));// 添加页头
        GridLayout logs = this.layout.addToContents(new GridLayout()).spacing(8);// 添加内容
        logs.defaultCellSetting().alignHorizontallyCenter();
        GridLayout.RowHelper logs$rowhelper = logs.createRowHelper(1);// 添加布局管理器
        // logs$rowhelper.addChild(new
        // MultiLineTextWidget(Component.translatable(updatelogs), this.font));
        MultiLineEditBox uplogs = logs$rowhelper
                .addChild(new MultiLineEditBox(this.font, this.width / 2, this.width, this.height, 100,
                        Component.translatable(""), Component.translatable("")));
        uplogs.setValue("logs in here");
        logs$rowhelper.addChild(Button.builder(Component.translatable("前往官网"), (p_280784_) -> {
            this.minecraft.setScreen(new ConfirmLinkScreen((p_280783_) -> {
                if (p_280783_) {
                    Util.getPlatform().openUri(serverURL);
                }

                this.minecraft.setScreen(this);
            }, serverURL, true));
        }).bounds(this.width / 2 - 155, this.height - 27, 150, 20).build());
        this.layout.addToFooter(Button.builder(Component.translatable("Update!"), (p_280801_) -> {
            this.minecraft.setScreen((Screen) null);
        }).bounds(this.width / 2 - 100, 140, 200, 20).build());
        this.layout.arrangeElements();
        this.layout.visitWidgets(this::addRenderableWidget);
    }

    protected void repositionElements() {
        this.layout.arrangeElements();
    }

    public void render(GuiGraphics p_281469_, int p_96053_, int p_96054_, float p_96055_) {
        this.renderBackground(p_281469_);
        super.render(p_281469_, p_96053_, p_96054_, p_96055_);
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }
}