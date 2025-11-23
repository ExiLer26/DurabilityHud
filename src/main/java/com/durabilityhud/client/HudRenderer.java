package com.durabilityhud.client;

import com.durabilityhud.config.ModConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = "durabilityhud", value = Dist.CLIENT)
public class HudRenderer {
    
    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
        if (!event.getOverlay().id().equals(VanillaGuiOverlay.HOTBAR.id())) return;
        if (!ModConfig.ENABLED.get()) return;
        
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) return;
        
        GuiGraphics guiGraphics = event.getGuiGraphics();
        
        List<ItemStack> itemsToRender = new ArrayList<>();
        int blockCount = 0;
        ItemStack blockExample = ItemStack.EMPTY;
        
        addPlayerItems(mc, itemsToRender);
        blockCount = countBlocks(mc);
        blockExample = getFirstBlock(mc);
        
        if (itemsToRender.isEmpty() && blockCount == 0) return;
        
        int x = ModConfig.HUD_X.get();
        int y = ModConfig.HUD_Y.get();
        float scale = ModConfig.HUD_SCALE.get().floatValue();
        
        RenderSystem.enableBlend();
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0);
        guiGraphics.pose().scale(scale, scale, 1.0f);
        
        int offsetY = 0;
        
        if (ModConfig.SHOW_BLOCKS.get() && blockCount > 0 && !blockExample.isEmpty()) {
            renderBlockGroup(guiGraphics, mc, blockExample, blockCount, 0, offsetY);
            offsetY += 20;
        }
        
        for (ItemStack stack : itemsToRender) {
            renderItemWithDurability(guiGraphics, mc, stack, 0, offsetY);
            offsetY += 20;
        }
        
        guiGraphics.pose().popPose();
        RenderSystem.disableBlend();
    }
    
    private static void addPlayerItems(Minecraft mc, List<ItemStack> items) {
        ItemStack mainHand = mc.player.getMainHandItem();
        if (!mainHand.isEmpty() && mainHand.isDamageableItem() && shouldShowItem(mainHand)) {
            items.add(mainHand);
        }
        
        ItemStack offHand = mc.player.getOffhandItem();
        if (!offHand.isEmpty() && offHand.isDamageableItem() && shouldShowItem(offHand) && !ItemStack.matches(mainHand, offHand)) {
            items.add(offHand);
        }
        
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack armorPiece = mc.player.getItemBySlot(slot);
                if (!armorPiece.isEmpty() && armorPiece.isDamageableItem() && shouldShowItem(armorPiece)) {
                    items.add(armorPiece);
                }
            }
        }
    }
    
    private static boolean shouldShowItem(ItemStack stack) {
        Item item = stack.getItem();
        
        if (item instanceof SwordItem) return ModConfig.SHOW_SWORD.get();
        if (item instanceof PickaxeItem) return ModConfig.SHOW_PICKAXE.get();
        if (item instanceof AxeItem) return ModConfig.SHOW_AXE.get();
        if (item instanceof ShovelItem) return ModConfig.SHOW_SHOVEL.get();
        if (item instanceof HoeItem) return ModConfig.SHOW_HOE.get();
        if (item instanceof ArmorItem armor) {
            return switch (armor.getType()) {
                case HELMET -> ModConfig.SHOW_HELMET.get();
                case CHESTPLATE -> ModConfig.SHOW_CHESTPLATE.get();
                case LEGGINGS -> ModConfig.SHOW_LEGGINGS.get();
                case BOOTS -> ModConfig.SHOW_BOOTS.get();
                default -> true;
            };
        }
        if (item instanceof ShieldItem) return ModConfig.SHOW_SHIELD.get();
        if (item instanceof ElytraItem) return ModConfig.SHOW_ELYTRA.get();
        if (item instanceof BowItem) return ModConfig.SHOW_BOW.get();
        if (item instanceof CrossbowItem) return ModConfig.SHOW_CROSSBOW.get();
        if (item instanceof TridentItem) return ModConfig.SHOW_TRIDENT.get();
        if (item instanceof FishingRodItem) return ModConfig.SHOW_FISHING_ROD.get();
        if (item instanceof ShearsItem) return ModConfig.SHOW_SHEARS.get();
        
        return true;
    }
    
    private static int countBlocks(Minecraft mc) {
        int count = 0;
        for (int i = 0; i < mc.player.getInventory().getContainerSize(); i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
                count += stack.getCount();
            }
        }
        return count;
    }
    
    private static ItemStack getFirstBlock(Minecraft mc) {
        for (int i = 0; i < mc.player.getInventory().getContainerSize(); i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }
    
    private static void renderBlockGroup(GuiGraphics guiGraphics, Minecraft mc, ItemStack exampleBlock, int totalCount, int x, int y) {
        guiGraphics.renderItem(exampleBlock, x, y);
        
        String countText = "x" + totalCount;
        
        int textX = x + 20;
        int textY = y + 4;
        
        guiGraphics.fill(textX - 1, textY - 1, textX + mc.font.width(countText) + 1, textY + 9, 0x80000000);
        guiGraphics.drawString(mc.font, countText, textX, textY, 0xFFFFFFFF, false);
    }
    
    private static void renderItemWithDurability(GuiGraphics guiGraphics, Minecraft mc, ItemStack stack, int x, int y) {
        guiGraphics.renderItem(stack, x, y);
        guiGraphics.renderItemDecorations(mc.font, stack, x, y);
        
        int maxDamage = stack.getMaxDamage();
        int damage = stack.getDamageValue();
        int durability = maxDamage - damage;
        
        float durabilityPercent = (float) durability / maxDamage;
        int barColor;
        if (durabilityPercent > 0.66f) {
            barColor = 0xFF00FF00;
        } else if (durabilityPercent > 0.33f) {
            barColor = 0xFFFFFF00;
        } else {
            barColor = 0xFFFF0000;
        }
        
        String durabilityText = durability + "/" + maxDamage;
        
        int textX = x + 20;
        int textY = y + 4;
        
        guiGraphics.fill(textX - 1, textY - 1, textX + mc.font.width(durabilityText) + 1, textY + 9, 0x80000000);
        
        guiGraphics.drawString(mc.font, durabilityText, textX, textY, barColor, false);
    }
}
