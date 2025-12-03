package com.durabilityhud.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    
    public static final String KEY_CATEGORY = "key.categories.durabilityhud";
    
    public static final KeyBinding openConfigKey = new KeyBinding(
        "key.durabilityhud.openconfig",
        InputUtil.Type.KEYSYM,
        GLFW.GLFW_KEY_Y,
        KEY_CATEGORY
    );
    
    public static void handleKeyInput(MinecraftClient client) {
        if (client.currentScreen == null && openConfigKey.wasPressed()) {
            client.setScreen(new ConfigScreen(null));
        }
    }
}
