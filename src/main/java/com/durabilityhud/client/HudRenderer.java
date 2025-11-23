package com.durabilityhud.client;

import com.durabilityhud.config.ModConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
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
        
        // Check if holding a block in main hand
        ItemStack heldItem = mc.player.getMainHandItem();
        if (!heldItem.isEmpty() && heldItem.getItem() instanceof BlockItem) {
            blockExample = heldItem;
            blockCount = countMatchingBlocks(mc, heldItem);
        }
        
        boolean hasPinnedBlocks = ModConfig.PINNED_BLOCKS_ENABLED.get() && !ModConfig.PINNED_BLOCKS_LIST.get().isEmpty();
        if (itemsToRender.isEmpty() && blockCount == 0 && !hasPinnedBlocks) return;
        
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
        
        renderPinnedBlocks(guiGraphics, mc);
    }
    
    private static void renderPinnedBlocks(GuiGraphics guiGraphics, Minecraft mc) {
        if (!ModConfig.PINNED_BLOCKS_ENABLED.get()) return;
        
        List<? extends String> pinnedBlocks = ModConfig.PINNED_BLOCKS_LIST.get();
        if (pinnedBlocks.isEmpty()) return;
        
        int x = ModConfig.PINNED_BLOCKS_X.get();
        int y = ModConfig.PINNED_BLOCKS_Y.get();
        float scale = ModConfig.HUD_SCALE.get().floatValue();
        
        RenderSystem.enableBlend();
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0);
        guiGraphics.pose().scale(scale, scale, 1.0f);
        
        int offsetY = 0;
        
        for (String blockId : pinnedBlocks) {
            try {
                ResourceLocation location = new ResourceLocation(blockId);
                Item item = BuiltInRegistries.ITEM.get(location);
                
                if (item != Items.AIR && item instanceof BlockItem) {
                    ItemStack stack = new ItemStack(item);
                    int count = countMatchingBlocks(mc, stack);
                    
                    renderBlockGroup(guiGraphics, mc, stack, count, 0, offsetY);
                    offsetY += 20;
                }
            } catch (Exception e) {
            }
        }
        
        guiGraphics.pose().popPose();
        RenderSystem.disableBlend();
    }
    
    private static void addPlayerItems(Minecraft mc, List<ItemStack> items) {
        List<ItemStack> tempItems = new ArrayList<>();
        
        ItemStack mainHand = mc.player.getMainHandItem();
        if (!mainHand.isEmpty() && mainHand.isDamageableItem() && shouldShowItem(mainHand)) {
            tempItems.add(mainHand);
        }
        
        ItemStack offHand = mc.player.getOffhandItem();
        if (!offHand.isEmpty() && offHand.isDamageableItem() && shouldShowItem(offHand) && !ItemStack.matches(mainHand, offHand)) {
            tempItems.add(offHand);
        }
        
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack armorPiece = mc.player.getItemBySlot(slot);
                if (!armorPiece.isEmpty() && armorPiece.isDamageableItem() && shouldShowItem(armorPiece)) {
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
        
        var orderList = new ArrayList<String>(ModConfig.ITEM_ORDER.get());
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
            return switch (armor.getType()) {
                case HELMET -> "helmet";
                case CHESTPLATE -> "chestplate";
                case LEGGINGS -> "leggings";
                case BOOTS -> "boots";
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
    
    private static int countMatchingBlocks(Minecraft mc, ItemStack targetBlock) {
        int count = 0;
        for (int i = 0; i < mc.player.getInventory().getContainerSize(); i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (!stack.isEmpty() && ItemStack.isSameItemSameTags(stack, targetBlock)) {
                count += stack.getCount();
            }
        }
        return count;
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
