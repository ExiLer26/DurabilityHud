package com.durabilityhud.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "durabilityhud", value = Dist.CLIENT)
public class KeyBindings {
    
    public static final String KEY_CATEGORY = "key.categories.durabilityhud";
    
    public static final KeyMapping openConfigKey = new KeyMapping(
        "key.durabilityhud.openconfig",
        KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM,
        InputConstants.KEY_Y,
        KEY_CATEGORY
    );
    
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        
        if (mc.screen == null && openConfigKey.consumeClick()) {
            mc.setScreen(new ConfigScreen(null));
        }
    }
}
