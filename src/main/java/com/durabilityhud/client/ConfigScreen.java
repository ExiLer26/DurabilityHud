package com.durabilityhud.client;

import com.durabilityhud.config.ModConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends Screen {
    private final Screen parent;
    private int scrollOffset = 0;
    
    public ConfigScreen(Screen parent) {
        super(Component.literal("Durability HUD Ayarları"));
        this.parent = parent;
    }
    
    @Override
    protected void init() {
        super.init();
        
        int y = 30;
        int buttonWidth = 200;
        int centerX = this.width / 2 - buttonWidth / 2;
        
        this.addRenderableWidget(Button.builder(
            Component.literal("HUD Aktif: " + (ModConfig.ENABLED.get() ? "AÇIK" : "KAPALI")),
            button -> {
                ModConfig.ENABLED.set(!ModConfig.ENABLED.get());
                ModConfig.save();
                button.setMessage(Component.literal("HUD Aktif: " + (ModConfig.ENABLED.get() ? "AÇIK" : "KAPALI")));
            }
        ).bounds(centerX, y, buttonWidth, 20).build());
        
        y += 25;
        this.addRenderableWidget(Button.builder(
            Component.literal("X Pozisyon: " + ModConfig.HUD_X.get()),
            button -> {
                int currentX = ModConfig.HUD_X.get();
                ModConfig.HUD_X.set(currentX + 10 > 1000 ? 0 : currentX + 10);
                ModConfig.save();
                button.setMessage(Component.literal("X Pozisyon: " + ModConfig.HUD_X.get()));
            }
        ).bounds(centerX, y, buttonWidth / 2 - 2, 20).build());
        
        this.addRenderableWidget(Button.builder(
            Component.literal("-"),
            button -> {
                int currentX = ModConfig.HUD_X.get();
                ModConfig.HUD_X.set(Math.max(0, currentX - 10));
                ModConfig.save();
                this.rebuildWidgets();
            }
        ).bounds(centerX + buttonWidth / 2 + 2, y, buttonWidth / 2 - 2, 20).build());
        
        y += 25;
        this.addRenderableWidget(Button.builder(
            Component.literal("Y Pozisyon: " + ModConfig.HUD_Y.get()),
            button -> {
                int currentY = ModConfig.HUD_Y.get();
                ModConfig.HUD_Y.set(currentY + 10 > 1000 ? 0 : currentY + 10);
                ModConfig.save();
                button.setMessage(Component.literal("Y Pozisyon: " + ModConfig.HUD_Y.get()));
            }
        ).bounds(centerX, y, buttonWidth / 2 - 2, 20).build());
        
        this.addRenderableWidget(Button.builder(
            Component.literal("-"),
            button -> {
                int currentY = ModConfig.HUD_Y.get();
                ModConfig.HUD_Y.set(Math.max(0, currentY - 10));
                ModConfig.save();
                this.rebuildWidgets();
            }
        ).bounds(centerX + buttonWidth / 2 + 2, y, buttonWidth / 2 - 2, 20).build());
        
        y += 25;
        this.addRenderableWidget(Button.builder(
            Component.literal("Boyut: " + String.format("%.1f", ModConfig.HUD_SCALE.get())),
            button -> {
                double scale = ModConfig.HUD_SCALE.get();
                ModConfig.HUD_SCALE.set(scale + 0.1 > 3.0 ? 0.5 : scale + 0.1);
                ModConfig.save();
                button.setMessage(Component.literal("Boyut: " + String.format("%.1f", ModConfig.HUD_SCALE.get())));
            }
        ).bounds(centerX, y, buttonWidth / 2 - 2, 20).build());
        
        this.addRenderableWidget(Button.builder(
            Component.literal("-"),
            button -> {
                double scale = ModConfig.HUD_SCALE.get();
                ModConfig.HUD_SCALE.set(Math.max(0.1, scale - 0.1));
                ModConfig.save();
                this.rebuildWidgets();
            }
        ).bounds(centerX + buttonWidth / 2 + 2, y, buttonWidth / 2 - 2, 20).build());
        
        y += 30;
        addItemToggle("Bloklar (Toplam)", ModConfig.SHOW_BLOCKS, centerX, y);
        y += 25;
        addItemToggle("Kılıç", ModConfig.SHOW_SWORD, centerX, y);
        y += 25;
        addItemToggle("Kazma", ModConfig.SHOW_PICKAXE, centerX, y);
        y += 25;
        addItemToggle("Balta", ModConfig.SHOW_AXE, centerX, y);
        y += 25;
        addItemToggle("Kürek", ModConfig.SHOW_SHOVEL, centerX, y);
        y += 25;
        addItemToggle("Çapa", ModConfig.SHOW_HOE, centerX, y);
        y += 25;
        addItemToggle("Miğfer", ModConfig.SHOW_HELMET, centerX, y);
        y += 25;
        addItemToggle("Göğüslük", ModConfig.SHOW_CHESTPLATE, centerX, y);
        y += 25;
        addItemToggle("Pantolon", ModConfig.SHOW_LEGGINGS, centerX, y);
        y += 25;
        addItemToggle("Çizme", ModConfig.SHOW_BOOTS, centerX, y);
        y += 25;
        addItemToggle("Kalkan", ModConfig.SHOW_SHIELD, centerX, y);
        y += 25;
        addItemToggle("Elytra", ModConfig.SHOW_ELYTRA, centerX, y);
        y += 25;
        addItemToggle("Yay", ModConfig.SHOW_BOW, centerX, y);
        y += 25;
        addItemToggle("Tatar Yayı", ModConfig.SHOW_CROSSBOW, centerX, y);
        y += 25;
        addItemToggle("Üç Dişli Mızrak", ModConfig.SHOW_TRIDENT, centerX, y);
        y += 25;
        addItemToggle("Olta", ModConfig.SHOW_FISHING_ROD, centerX, y);
        y += 25;
        addItemToggle("Makas", ModConfig.SHOW_SHEARS, centerX, y);
        
        y += 30;
        this.addRenderableWidget(Button.builder(
            Component.literal("Tamamlandı"),
            button -> this.onClose()
        ).bounds(centerX, y, 200, 20).build());
    }
    
    private void addItemToggle(String label, net.minecraftforge.common.ForgeConfigSpec.BooleanValue config, int x, int y) {
        this.addRenderableWidget(Button.builder(
            Component.literal(label + ": " + (config.get() ? "GÖSTER" : "GİZLE")),
            button -> {
                config.set(!config.get());
                ModConfig.save();
                button.setMessage(Component.literal(label + ": " + (config.get() ? "GÖSTER" : "GİZLE")));
            }
        ).bounds(x, y, 200, 20).build());
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 10, 0xFFFFFF);
        
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }
    
    @Override
    public void onClose() {
        ModConfig.save();
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
        }
    }
}
