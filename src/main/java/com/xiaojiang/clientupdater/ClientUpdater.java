package com.xiaojiang.clientupdater;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.xiaojiang.clientupdater.screens.UpdateLogScreen;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.LinkedList;

import net.minecraft.client.resources.language.I18n;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ClientUpdater.MODID)
public class ClientUpdater {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "clientupdater";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static boolean needshow = true;

    public ClientUpdater() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // 注册配置文件
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.SPEC);

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("Made By XiaoJiang");
    }

    // 客户端
    @SubscribeEvent
    public void showUpdateMassage(ScreenEvent.Opening event) {
        // 更新逻辑
        if (needshow) {
            Update update = Update.loadJsonFromURL(Config.serverAddress + "api/getupdate");
            if (update == null) {
                update = new Update();
                update.update_time = I18n.get("gui.clientupdater.error");
                update.update_logs = I18n.get("gui.clientupdater.unknowhost");
                update.mods_list = null;
                update.config_list = null;
                event.setNewScreen(new UpdateLogScreen(Config.serverAddress, update, false));
                LOGGER.warn("Connect Error");
            } else {
                // 获取本地mod列表
                Map<String, String> file_list = new HashMap<String, String>();
                File file_dir = new File("./mods");
                Queue<File> files = new LinkedList<File>();
                for (File f : file_dir.listFiles()) {
                    files.offer(f);
                }
                if (files != null) {
                    while (!files.isEmpty()) {
                        if (files.peek().isFile()) {
                            file_list.put(Tools.getMD5(files.peek().getPath()), files.poll().getName());
                        } else if (files.peek().isDirectory()) {
                            for (File f : files.poll().listFiles()) {
                                files.offer(f);
                            }
                        }
                    }
                }
                // 判断完整性
                boolean needupdate = false;
                for (String key : update.mods_list) {
                    if (file_list.get(key) == null) {
                        needupdate = true;
                    }
                }
                for (String key : file_list.keySet()) {
                    if (update.mods_list.indexOf(key) == -1) {
                        needupdate = true;
                    }
                }
                // 获取本地config列表
                file_dir = new File("./config");
                files = new LinkedList<File>();
                for (File f : file_dir.listFiles()) {
                    files.offer(f);
                }
                if (files != null) {
                    while (!files.isEmpty()) {
                        if (files.peek().isFile()) {
                            file_list.put(Tools.getMD5(files.peek().getPath()), files.poll().getName());
                        } else if (files.peek().isDirectory()) {
                            for (File f : files.poll().listFiles()) {
                                files.offer(f);
                            }
                        }
                    }
                }
                // 对比config列表
                for (String key : update.config_list) {
                    if (file_list.get(key) == null) {
                        needupdate = true;
                    }
                }
                // 更新判断
                if (needupdate) {
                    event.setNewScreen(new UpdateLogScreen(Config.serverAddress, update, needupdate));
                    Config.setLastUpdateTime(update.update_time);
                } else if (!update.update_time.equals(Config.last_update_time)) {
                    event.setNewScreen(new UpdateLogScreen(Config.serverAddress, update, needupdate));
                    Config.setLastUpdateTime(update.update_time);
                }
            }
            needshow = false;
        }
    }
    // Add the example block item to the building blocks tab

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.warn("此mod无需在服务端加载");
        LOGGER.warn("this mod don't need to load on server");
    }
}
