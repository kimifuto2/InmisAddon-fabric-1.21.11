package net.inmisaddon;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

@Environment(EnvType.CLIENT)
public class InmisAddonClient implements ClientModInitializer {

    public static final boolean isFirstPersonLoaded = FabricLoader.getInstance().isModLoaded("firstperson");

    @Override
    public void onInitializeClient() {
    }

}
