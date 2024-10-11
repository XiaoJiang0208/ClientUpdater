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
            .define("server_address", "http://example.com:25564/");

    private static final ForgeConfigSpec.ConfigValue<String> LAST_UPDATE_TIME = BUILDER
            .comment("最后更新时间(自动生成请勿更改)")
            .define("last_update_time", "0");

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static String serverAddress;
    public static String last_update_time;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        serverAddress = SERVER_ADD.get();
        last_update_time = LAST_UPDATE_TIME.get();
    }

    static void setLastUpdateTime(String time) {
        LAST_UPDATE_TIME.set(time);
    }

}
