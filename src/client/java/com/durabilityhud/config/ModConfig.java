package com.durabilityhud.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("durabilityhud.json");
    
    private static ConfigData config = new ConfigData();
    
    public static class ConfigData {
        public boolean enabled = true;
        public int hudX = 10;
        public int hudY = 10;
        public double hudScale = 1.0;
        
        public boolean pinnedBlocksEnabled = true;
        public int pinnedBlocksX = 10;
        public int pinnedBlocksY = 100;
        public List<String> pinnedBlocksList = new ArrayList<>();
        
        public List<String> itemOrder = new ArrayList<>(Arrays.asList(
            "blocks", "sword", "pickaxe", "axe", "shovel", "hoe",
            "helmet", "chestplate", "leggings", "boots", "shield",
            "elytra", "bow", "crossbow", "trident", "fishing_rod", "shears"
        ));
        
        public List<Integer> itemPosX = new ArrayList<>();
        public List<Integer> itemPosY = new ArrayList<>();
        
        public boolean showBlocks = true;
        public boolean showSword = true;
        public boolean showPickaxe = true;
        public boolean showAxe = true;
        public boolean showShovel = true;
        public boolean showHoe = true;
        public boolean showHelmet = true;
        public boolean showChestplate = true;
        public boolean showLeggings = true;
        public boolean showBoots = true;
        public boolean showShield = true;
        public boolean showElytra = true;
        public boolean showBow = true;
        public boolean showCrossbow = true;
        public boolean showTrident = true;
        public boolean showFishingRod = true;
        public boolean showShears = true;
    }
    
    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                config = GSON.fromJson(reader, ConfigData.class);
                if (config == null) {
                    config = new ConfigData();
                }
            } catch (IOException e) {
                e.printStackTrace();
                config = new ConfigData();
            }
        } else {
            config = new ConfigData();
            save();
        }
    }
    
    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(config, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean isEnabled() { return config.enabled; }
    public static void setEnabled(boolean value) { config.enabled = value; }
    
    public static int getHudX() { return config.hudX; }
    public static void setHudX(int value) { config.hudX = value; }
    
    public static int getHudY() { return config.hudY; }
    public static void setHudY(int value) { config.hudY = value; }
    
    public static double getHudScale() { return config.hudScale; }
    public static void setHudScale(double value) { config.hudScale = value; }
    
    public static boolean isPinnedBlocksEnabled() { return config.pinnedBlocksEnabled; }
    public static void setPinnedBlocksEnabled(boolean value) { config.pinnedBlocksEnabled = value; }
    
    public static int getPinnedBlocksX() { return config.pinnedBlocksX; }
    public static void setPinnedBlocksX(int value) { config.pinnedBlocksX = value; }
    
    public static int getPinnedBlocksY() { return config.pinnedBlocksY; }
    public static void setPinnedBlocksY(int value) { config.pinnedBlocksY = value; }
    
    public static List<String> getPinnedBlocksList() { return config.pinnedBlocksList; }
    public static void setPinnedBlocksList(List<String> value) { config.pinnedBlocksList = value; }
    
    public static List<String> getItemOrder() { return config.itemOrder; }
    public static void setItemOrder(List<String> value) { config.itemOrder = value; }
    
    public static List<Integer> getItemPosX() { return config.itemPosX; }
    public static void setItemPosX(List<Integer> value) { config.itemPosX = value; }
    
    public static List<Integer> getItemPosY() { return config.itemPosY; }
    public static void setItemPosY(List<Integer> value) { config.itemPosY = value; }
    
    public static boolean isShowBlocks() { return config.showBlocks; }
    public static void setShowBlocks(boolean value) { config.showBlocks = value; }
    
    public static boolean isShowSword() { return config.showSword; }
    public static void setShowSword(boolean value) { config.showSword = value; }
    
    public static boolean isShowPickaxe() { return config.showPickaxe; }
    public static void setShowPickaxe(boolean value) { config.showPickaxe = value; }
    
    public static boolean isShowAxe() { return config.showAxe; }
    public static void setShowAxe(boolean value) { config.showAxe = value; }
    
    public static boolean isShowShovel() { return config.showShovel; }
    public static void setShowShovel(boolean value) { config.showShovel = value; }
    
    public static boolean isShowHoe() { return config.showHoe; }
    public static void setShowHoe(boolean value) { config.showHoe = value; }
    
    public static boolean isShowHelmet() { return config.showHelmet; }
    public static void setShowHelmet(boolean value) { config.showHelmet = value; }
    
    public static boolean isShowChestplate() { return config.showChestplate; }
    public static void setShowChestplate(boolean value) { config.showChestplate = value; }
    
    public static boolean isShowLeggings() { return config.showLeggings; }
    public static void setShowLeggings(boolean value) { config.showLeggings = value; }
    
    public static boolean isShowBoots() { return config.showBoots; }
    public static void setShowBoots(boolean value) { config.showBoots = value; }
    
    public static boolean isShowShield() { return config.showShield; }
    public static void setShowShield(boolean value) { config.showShield = value; }
    
    public static boolean isShowElytra() { return config.showElytra; }
    public static void setShowElytra(boolean value) { config.showElytra = value; }
    
    public static boolean isShowBow() { return config.showBow; }
    public static void setShowBow(boolean value) { config.showBow = value; }
    
    public static boolean isShowCrossbow() { return config.showCrossbow; }
    public static void setShowCrossbow(boolean value) { config.showCrossbow = value; }
    
    public static boolean isShowTrident() { return config.showTrident; }
    public static void setShowTrident(boolean value) { config.showTrident = value; }
    
    public static boolean isShowFishingRod() { return config.showFishingRod; }
    public static void setShowFishingRod(boolean value) { config.showFishingRod = value; }
    
    public static boolean isShowShears() { return config.showShears; }
    public static void setShowShears(boolean value) { config.showShears = value; }
}
