package com.durabilityhud.client;

import com.durabilityhud.config.ModConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class HudRenderer {
    
    public static void render(DrawContext drawContext) {
        if (!ModConfig.isEnabled()) return;
        
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.options.hudHidden) return;
        
        List<ItemStack> itemsToRender = new ArrayList<>();
        int blockCount = 0;
        ItemStack blockExample = ItemStack.EMPTY;
        
        addPlayerItems(mc, itemsToRender);
        
        ItemStack heldItem = mc.player.getMainHandStack();
        if (!heldItem.isEmpty() && heldItem.getItem() instanceof BlockItem) {
            blockExample = heldItem;
            blockCount = countMatchingBlocks(mc, heldItem);
        }
        
        boolean hasPinnedBlocks = ModConfig.isPinnedBlocksEnabled() && !ModConfig.getPinnedBlocksList().isEmpty();
        if (itemsToRender.isEmpty() && blockCount == 0 && !hasPinnedBlocks) return;
        
        int x = ModConfig.getHudX();
        int y = ModConfig.getHudY();
        float scale = (float) ModConfig.getHudScale();
        
        RenderSystem.enableBlend();
        drawContext.getMatrices().push();
        drawContext.getMatrices().translate(x, y, 0);
        drawContext.getMatrices().scale(scale, scale, 1.0f);
        
        int offsetY = 0;
        
        if (ModConfig.isShowBlocks() && blockCount > 0 && !blockExample.isEmpty()) {
            renderBlockGroup(drawContext, mc, blockExample, blockCount, 0, offsetY);
            offsetY += 20;
        }
        
        for (ItemStack stack : itemsToRender) {
            renderItemWithDurability(drawContext, mc, stack, 0, offsetY);
            offsetY += 20;
        }
        
        drawContext.getMatrices().pop();
        RenderSystem.disableBlend();
        
        renderPinnedBlocks(drawContext, mc);
    }
    
    private static void renderPinnedBlocks(DrawContext drawContext, MinecraftClient mc) {
        if (!ModConfig.isPinnedBlocksEnabled()) return;
        
        List<String> pinnedBlocks = ModConfig.getPinnedBlocksList();
        if (pinnedBlocks.isEmpty()) return;
        
        int x = ModConfig.getPinnedBlocksX();
        int y = ModConfig.getPinnedBlocksY();
        float scale = (float) ModConfig.getHudScale();
        
        RenderSystem.enableBlend();
        drawContext.getMatrices().push();
        drawContext.getMatrices().translate(x, y, 0);
        drawContext.getMatrices().scale(scale, scale, 1.0f);
        
        int offsetY = 0;
        
        for (String blockId : pinnedBlocks) {
            try {
                Identifier location = new Identifier(blockId);
                Item item = Registries.ITEM.get(location);
                
                if (item != Items.AIR && item instanceof BlockItem) {
                    ItemStack stack = new ItemStack(item);
                    int count = countMatchingBlocks(mc, stack);
                    
                    renderBlockGroup(drawContext, mc, stack, count, 0, offsetY);
                    offsetY += 20;
                }
            } catch (Exception e) {
            }
        }
        
        drawContext.getMatrices().pop();
        RenderSystem.disableBlend();
    }
    
    private static void addPlayerItems(MinecraftClient mc, List<ItemStack> items) {
        List<ItemStack> tempItems = new ArrayList<>();
        
        ItemStack mainHand = mc.player.getMainHandStack();
        if (!mainHand.isEmpty() && mainHand.isDamageable() && shouldShowItem(mainHand)) {
            tempItems.add(mainHand);
        }
        
        ItemStack offHand = mc.player.getOffHandStack();
        if (!offHand.isEmpty() && offHand.isDamageable() && shouldShowItem(offHand) && !ItemStack.areEqual(mainHand, offHand)) {
            tempItems.add(offHand);
        }
        
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack armorPiece = mc.player.getEquippedStack(slot);
                if (!armorPiece.isEmpty() && armorPiece.isDamageable() && shouldShowItem(armorPiece)) {
                    tempItems.add(armorPiece);
                }
            }
        }
        
        tempItems.sort((a, b) -> {
            int orderA = getItemOrder(a);
            int orderB = getItemOrder(b);
            return Integer.compare(orderA, orderB);
        });
        
        items.addAll(tempItems);
    }
    
    private static int getItemOrder(ItemStack stack) {
        Item item = stack.getItem();
        String itemType = getItemType(item);
        
        List<String> orderList = new ArrayList<>(ModConfig.getItemOrder());
        int index = orderList.indexOf(itemType);
        return index == -1 ? 9999 : index;
    }
    
    private static String getItemType(Item item) {
        if (item instanceof SwordItem) return "sword";
        if (item instanceof PickaxeItem) return "pickaxe";
        if (item instanceof AxeItem) return "axe";
        if (item instanceof ShovelItem) return "shovel";
        if (item instanceof HoeItem) return "hoe";
        if (item instanceof ArmorItem armor) {
            return switch (armor.getSlotType()) {
                case HEAD -> "helmet";
                case CHEST -> "chestplate";
                case LEGS -> "leggings";
                case FEET -> "boots";
                default -> "unknown";
            };
        }
        if (item instanceof ShieldItem) return "shield";
        if (item instanceof ElytraItem) return "elytra";
        if (item instanceof BowItem) return "bow";
        if (item instanceof CrossbowItem) return "crossbow";
        if (item instanceof TridentItem) return "trident";
        if (item instanceof FishingRodItem) return "fishing_rod";
        if (item instanceof ShearsItem) return "shears";
        
        return "unknown";
    }
    
    private static boolean shouldShowItem(ItemStack stack) {
        Item item = stack.getItem();
        
        if (item instanceof SwordItem) return ModConfig.isShowSword();
        if (item instanceof PickaxeItem) return ModConfig.isShowPickaxe();
        if (item instanceof AxeItem) return ModConfig.isShowAxe();
        if (item instanceof ShovelItem) return ModConfig.isShowShovel();
        if (item instanceof HoeItem) return ModConfig.isShowHoe();
        if (item instanceof ArmorItem armor) {
            return switch (armor.getSlotType()) {
                case HEAD -> ModConfig.isShowHelmet();
                case CHEST -> ModConfig.isShowChestplate();
                case LEGS -> ModConfig.isShowLeggings();
                case FEET -> ModConfig.isShowBoots();
                default -> true;
            };
        }
        if (item instanceof ShieldItem) return ModConfig.isShowShield();
        if (item instanceof ElytraItem) return ModConfig.isShowElytra();
        if (item instanceof BowItem) return ModConfig.isShowBow();
        if (item instanceof CrossbowItem) return ModConfig.isShowCrossbow();
        if (item instanceof TridentItem) return ModConfig.isShowTrident();
        if (item instanceof FishingRodItem) return ModConfig.isShowFishingRod();
        if (item instanceof ShearsItem) return ModConfig.isShowShears();
        
        return true;
    }
    
    private static int countMatchingBlocks(MinecraftClient mc, ItemStack targetBlock) {
        int count = 0;
        for (int i = 0; i < mc.player.getInventory().size(); i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (!stack.isEmpty() && ItemStack.canCombine(stack, targetBlock)) {
                count += stack.getCount();
            }
        }
        return count;
    }
    
    private static void renderBlockGroup(DrawContext drawContext, MinecraftClient mc, ItemStack exampleBlock, int totalCount, int x, int y) {
        drawContext.drawItem(exampleBlock, x, y);
        
        String countText = "x" + totalCount;
        
        int textX = x + 20;
        int textY = y + 4;
        
        drawContext.fill(textX - 1, textY - 1, textX + mc.textRenderer.getWidth(countText) + 1, textY + 9, 0x80000000);
        drawContext.drawText(mc.textRenderer, countText, textX, textY, 0xFFFFFFFF, false);
    }
    
    private static void renderItemWithDurability(DrawContext drawContext, MinecraftClient mc, ItemStack stack, int x, int y) {
        drawContext.drawItem(stack, x, y);
        drawContext.drawItemInSlot(mc.textRenderer, stack, x, y);
        
        int maxDamage = stack.getMaxDamage();
        int damage = stack.getDamage();
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
        
        drawContext.fill(textX - 1, textY - 1, textX + mc.textRenderer.getWidth(durabilityText) + 1, textY + 9, 0x80000000);
        
        drawContext.drawText(mc.textRenderer, durabilityText, textX, textY, barColor, false);
    }
}
