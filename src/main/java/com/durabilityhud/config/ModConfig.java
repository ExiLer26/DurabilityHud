package com.durabilityhud.config;

import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ModConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    
    public static final ForgeConfigSpec.IntValue HUD_X;
    public static final ForgeConfigSpec.IntValue HUD_Y;
    public static final ForgeConfigSpec.DoubleValue HUD_SCALE;
    public static final ForgeConfigSpec.BooleanValue ENABLED;
    
    // Pinned Blocks
    public static final ForgeConfigSpec.BooleanValue PINNED_BLOCKS_ENABLED;
    public static final ForgeConfigSpec.IntValue PINNED_BLOCKS_X;
    public static final ForgeConfigSpec.IntValue PINNED_BLOCKS_Y;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> PINNED_BLOCKS_LIST;
    
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_ORDER;
    
    public static final ForgeConfigSpec.BooleanValue SHOW_BLOCKS;
    public static final ForgeConfigSpec.BooleanValue SHOW_SWORD;
    public static final ForgeConfigSpec.BooleanValue SHOW_PICKAXE;
    public static final ForgeConfigSpec.BooleanValue SHOW_AXE;
    public static final ForgeConfigSpec.BooleanValue SHOW_SHOVEL;
    public static final ForgeConfigSpec.BooleanValue SHOW_HOE;
    public static final ForgeConfigSpec.BooleanValue SHOW_HELMET;
    public static final ForgeConfigSpec.BooleanValue SHOW_CHESTPLATE;
    public static final ForgeConfigSpec.BooleanValue SHOW_LEGGINGS;
    public static final ForgeConfigSpec.BooleanValue SHOW_BOOTS;
    public static final ForgeConfigSpec.BooleanValue SHOW_SHIELD;
    public static final ForgeConfigSpec.BooleanValue SHOW_ELYTRA;
    public static final ForgeConfigSpec.BooleanValue SHOW_BOW;
    public static final ForgeConfigSpec.BooleanValue SHOW_CROSSBOW;
    public static final ForgeConfigSpec.BooleanValue SHOW_TRIDENT;
    public static final ForgeConfigSpec.BooleanValue SHOW_FISHING_ROD;
    public static final ForgeConfigSpec.BooleanValue SHOW_SHEARS;
    public static ForgeConfigSpec.ConfigValue<List<Integer>> ITEM_POS_X;
    public static ForgeConfigSpec.ConfigValue<List<Integer>> ITEM_POS_Y;

    static {
        BUILDER.push("general");
        
        ENABLED = BUILDER
            .comment("HUD'ı aktif/pasif et")
            .define("enabled", true);
        
        HUD_X = BUILDER
            .comment("HUD X pozisyonu (ekranın solundan piksel)")
            .defineInRange("hud_x", 10, 0, 10000);
            
        HUD_Y = BUILDER
            .comment("HUD Y pozisyonu (ekranın üstünden piksel)")
            .defineInRange("hud_y", 10, 0, 10000);
            
        HUD_SCALE = BUILDER
            .comment("HUD boyutu (0.5 = yarı, 1.0 = normal, 2.0 = iki kat)")
            .defineInRange("hud_scale", 1.0, 0.1, 5.0);
        
        BUILDER.pop();
        BUILDER.push("pinned_blocks");
        
        PINNED_BLOCKS_ENABLED = BUILDER
            .comment("Sabit blokları göster/gizle")
            .define("pinned_blocks_enabled", true);
            
        PINNED_BLOCKS_X = BUILDER
            .comment("Sabit bloklar X pozisyonu")
            .defineInRange("pinned_blocks_x", 10, 0, 10000);
            
        PINNED_BLOCKS_Y = BUILDER
            .comment("Sabit bloklar Y pozisyonu")
            .defineInRange("pinned_blocks_y", 100, 0, 10000);
            
        PINNED_BLOCKS_LIST = BUILDER
            .comment("Sabit olarak gösterilecek bloklar (örn: minecraft:dirt, minecraft:stone)")
            .defineList("pinned_blocks_list", new ArrayList<String>(), obj -> obj instanceof String);
        
        BUILDER.pop();
        BUILDER.push("items");
        
        ITEM_ORDER = BUILDER
            .comment("Item gösterim sırası")
            .defineList("item_order", Arrays.asList(
                "blocks", "sword", "pickaxe", "axe", "shovel", "hoe",
                "helmet", "chestplate", "leggings", "boots", "shield",
                "elytra", "bow", "crossbow", "trident", "fishing_rod", "shears"
            ), obj -> obj instanceof String);
            
            ITEM_POS_X = BUILDER
        .comment("Her dayanıklılık item'i için X pozisyonları")
        .define("item_pos_x", new ArrayList<>());

          ITEM_POS_Y = BUILDER
        .comment("Her dayanıklılık item'i için Y pozisyonları")
        .define("item_pos_y", new ArrayList<>());

        
        SHOW_BLOCKS = BUILDER
            .comment("Tüm blokları gruplandır ve toplam sayısını göster")
            .define("show_blocks", true);
        SHOW_SWORD = BUILDER.define("show_sword", true);
        SHOW_PICKAXE = BUILDER.define("show_pickaxe", true);
        SHOW_AXE = BUILDER.define("show_axe", true);
        SHOW_SHOVEL = BUILDER.define("show_shovel", true);
        SHOW_HOE = BUILDER.define("show_hoe", true);
        SHOW_HELMET = BUILDER.define("show_helmet", true);
        SHOW_CHESTPLATE = BUILDER.define("show_chestplate", true);
        SHOW_LEGGINGS = BUILDER.define("show_leggings", true);
        SHOW_BOOTS = BUILDER.define("show_boots", true);
        SHOW_SHIELD = BUILDER.define("show_shield", true);
        SHOW_ELYTRA = BUILDER.define("show_elytra", true);
        SHOW_BOW = BUILDER.define("show_bow", true);
        SHOW_CROSSBOW = BUILDER.define("show_crossbow", true);
        SHOW_TRIDENT = BUILDER.define("show_trident", true);
        SHOW_FISHING_ROD = BUILDER.define("show_fishing_rod", true);
        SHOW_SHEARS = BUILDER.define("show_shears", true);
        
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
    
    public static void register() {
        ModLoadingContext.get().registerConfig(Type.CLIENT, SPEC, "durabilityhud-client.toml");
    }
    
    public static void save() {
        SPEC.save();
    }
}
