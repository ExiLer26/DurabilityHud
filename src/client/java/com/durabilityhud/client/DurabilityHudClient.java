package com.durabilityhud.client;

import com.durabilityhud.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class DurabilityHudClient implements ClientModInitializer {
    public static final String MOD_ID = "durabilityhud";

    @Override
    public void onInitializeClient() {
        ModConfig.load();

        KeyBindingHelper.registerKeyBinding(KeyBindings.openConfigKey);

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            HudRenderer.render(drawContext);
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            KeyBindings.handleKeyInput(client);
        });
    }
}
