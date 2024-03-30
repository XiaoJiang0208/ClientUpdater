package com.xiaojiang.clientupdater;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.xiaojiang.clientupdater.screens.UpdateLogScreen;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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

    @SubscribeEvent
    public void showUpdateMassage(ScreenEvent.Opening event) {
        if (needshow) {
            Update update = Update.loadJsonFromURL(Config.serverAddress + "api/getupdate");
            if (update == null) {
                update = new Update();
                update.update_time = "unknow";
                update.update_logs = "Can't connect whit sync server";
                update.mods_list = null;
                event.setNewScreen(new UpdateLogScreen(Config.serverAddress, update, false));
                LOGGER.warn("Connect Error");
            } else {
                if (!update.update_time.equals(Config.last_update_time)) {
                    // 获取更新列表
                    Map<String, String> mods_list = new HashMap<String, String>();
                    File mods_dir = new File("./mods");
                    File mods[] = mods_dir.listFiles();
                    if (mods != null) {
                        for (File file : mods) {
                            if (file.isFile()) {
                                mods_list.put(Tools.getMD5(file.getPath()), file.getName());
                            }
                        }
                    }
                    // 判断完整性
                    boolean needupdate = false;
                    for (String key : update.mods_list) {
                        if (mods_list.get(key) == null) {
                            needupdate = true;
                        }
                    }
                    for (String key : mods_list.keySet()) {
                        if (update.mods_list.indexOf(key) == -1) {
                            needupdate = true;
                        }
                    } // 完整性决定是否客户端需要更新
                    event.setNewScreen(new UpdateLogScreen(Config.serverAddress, update, needupdate));
                    Config.setLastUpdateTime(update.update_time);
                    LOGGER.info("need update");
                } else {
                    // 获取更新列表
                    Map<String, String> mods_list = new HashMap<String, String>();
                    File mods_dir = new File("./mods");
                    File mods[] = mods_dir.listFiles();
                    if (mods != null) {
                        for (File file : mods) {
                            if (file.isFile()) {
                                mods_list.put(Tools.getMD5(file.getPath()), file.getName());
                            }
                        }
                    }
                    // 判断完整性
                    boolean needupdate = false;
                    for (String key : update.mods_list) {
                        if (mods_list.get(key) == null) {
                            needupdate = true;
                        }
                    }
                    for (String key : mods_list.keySet()) {
                        if (update.mods_list.indexOf(key) == -1) {
                            needupdate = true;
                        }
                    }
                    if (needupdate) {
                        event.setNewScreen(new UpdateLogScreen(Config.serverAddress, update, true));
                        LOGGER.info("check");
                    } else {
                        LOGGER.info("Don't need update");
                    }
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
