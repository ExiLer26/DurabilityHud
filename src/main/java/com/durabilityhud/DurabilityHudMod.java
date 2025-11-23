package com.durabilityhud;

import com.durabilityhud.client.HudRenderer;
import com.durabilityhud.client.KeyBindings;
import com.durabilityhud.config.ModConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(DurabilityHudMod.MODID)
public class DurabilityHudMod {
    public static final String MODID = "durabilityhud";
    
    public DurabilityHudMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        ModConfig.register();
        
        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(this::onClientSetup);
            modEventBus.addListener(this::onRegisterKeyMappings);
            
            ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                    (minecraft, screen) -> new com.durabilityhud.client.ConfigScreen(screen)
                )
            );
        }
    }
    
    private void onClientSetup(FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(HudRenderer.class);
        MinecraftForge.EVENT_BUS.register(KeyBindings.class);
    }
    
    private void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(KeyBindings.openConfigKey);
    }
}
