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

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.xiaojiang.clientupdater.Update;

@OnlyIn(Dist.CLIENT)
public class UpdateLogScreen extends Screen {
    private static final Component TITLE = Component.translatable("gui.clientupdater.updatelog");
    private String serverURL;
    private Update update = new Update();
    private boolean need_update;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    private static final Logger LOGGER = LogUtils.getLogger();
    // private final InfoPanel upDateLogs;

    public UpdateLogScreen(String serverurl, Update up, boolean needupdate) {
        super(TITLE);
        this.serverURL = serverurl;
        this.update = up;
        this.need_update = needupdate;
        if (this.update.mods_list == null)
            LOGGER.info("123");
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
                        Component.translatable(""), Component.translatable("")));// 添加多行文本框用于显示更新日志
        uplogs.setValue(this.update.update_time + "\n" + this.update.update_logs);// 设置更新日志
        logs$rowhelper.addChild(Button.builder(Component.translatable("gui.clientupdater.gotoweb"), (p_280784_) -> {
            this.minecraft.setScreen(new ConfirmLinkScreen((p_280783_) -> {
                if (p_280783_) {
                    Util.getPlatform().openUri(serverURL);
                }

                this.minecraft.setScreen(this);
            }, serverURL, true));
        }).bounds(this.width / 2 - 155, this.height - 27, 150, 20).build());// 添加官网跳转按钮
        if (need_update) {
            this.layout.addToFooter(Button.builder(Component.translatable("gui.clientupdater.update"), (p_280801_) -> {
                this.minecraft.setScreen(new UpdateScreen(this.update, this.serverURL));
            }).bounds(this.width / 2 - 100, 140, 200, 20).build());
        } else {
            this.layout
                    .addToFooter(Button.builder(Component.translatable("gui.clientupdater.complete"), (p_280801_) -> {
                        this.minecraft.setScreen((Screen) null);
                    }).bounds(this.width / 2 - 100, 140, 200, 20).build());
        }
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