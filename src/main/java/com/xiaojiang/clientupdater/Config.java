package com.xiaojiang.clientupdater;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = ClientUpdater.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.ConfigValue<String> SERVER_ADD = BUILDER
            .comment("同步服务器url")
            .define("server_address", "http://exp.example.com:80/");

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static String serverAddress;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        serverAddress = SERVER_ADD.get();
    }

}
