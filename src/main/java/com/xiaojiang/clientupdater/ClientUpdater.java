package com.xiaojiang.clientupdater;

import com.mojang.logging.LogUtils;
import com.xiaojiang.clientupdater.screens.UpdateLogScreen;

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

import org.slf4j.Logger;

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
        LOGGER.info("我了个豆");
    }

    @SubscribeEvent
    public void showUpdateMassage(ScreenEvent.Opening event) {
        if (needshow) {
            event.setNewScreen(new UpdateLogScreen(Config.serverAddress));
            LOGGER.info("ok");
            needshow = false;
        }
    }
    // Add the example block item to the building blocks tab

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
