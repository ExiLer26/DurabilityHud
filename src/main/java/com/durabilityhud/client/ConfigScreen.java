package com.durabilityhud.client;

import com.durabilityhud.config.ModConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigScreen extends Screen {
    private final Screen parent;
    private ConfigPage currentPage = ConfigPage.MAIN;

    private DraggableButton draggingButton = null;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;
    private int dragStartIndex = -1;

    private enum ConfigPage {
        MAIN("Ana Sayfa"),
        DURABILITY("Dayanıklılık Ayarları"),
        PINNED_BLOCKS("Sabit Bloklar");

        private final String title;

        ConfigPage(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    public ConfigScreen(Screen parent) {
        super(Component.literal("Durability HUD Ayarları"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        switch (currentPage) {
            case MAIN -> initMainPage();
            case DURABILITY -> initDurabilityPage();
            case PINNED_BLOCKS -> initPinnedBlocksPage();
        }

        int buttonWidth = 80;
        int spacing = 5;
        int totalWidth = (buttonWidth * 3) + (spacing * 2);
        int startX = (this.width - totalWidth) / 2;
        int tabY = this.height - 30;

        this.addRenderableWidget(Button.builder(
            Component.literal(ConfigPage.MAIN.getTitle()),
            button -> switchPage(ConfigPage.MAIN)
        ).bounds(startX, tabY, buttonWidth, 20).build());

        this.addRenderableWidget(Button.builder(
            Component.literal(ConfigPage.DURABILITY.getTitle()),
            button -> switchPage(ConfigPage.DURABILITY)
        ).bounds(startX + buttonWidth + spacing, tabY, buttonWidth, 20).build());

        this.addRenderableWidget(Button.builder(
            Component.literal(ConfigPage.PINNED_BLOCKS.getTitle()),
            button -> switchPage(ConfigPage.PINNED_BLOCKS)
        ).bounds(startX + (buttonWidth + spacing) * 2, tabY, buttonWidth, 20).build());
    }

    private void initMainPage() {
        int y = 40;
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

        y += 35;
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
            Component.literal("GUI Boyutu: " + String.format("%.1f", ModConfig.HUD_SCALE.get())),
            button -> {
                double scale = ModConfig.HUD_SCALE.get();
                ModConfig.HUD_SCALE.set(scale + 0.1 > 3.0 ? 0.5 : scale + 0.1);
                ModConfig.save();
                button.setMessage(Component.literal("GUI Boyutu: " + String.format("%.1f", ModConfig.HUD_SCALE.get())));
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
    }

    private void initDurabilityPage() {
        rebuildDurabilityButtons();
    }

    private void initPinnedBlocksPage() {
        int y = 40;
        int buttonWidth = 200;
        int centerX = this.width / 2 - buttonWidth / 2;

        this.addRenderableWidget(Button.builder(
            Component.literal("Sabit Bloklar: " + (ModConfig.PINNED_BLOCKS_ENABLED.get() ? "AÇIK" : "KAPALI")),
            button -> {
                ModConfig.PINNED_BLOCKS_ENABLED.set(!ModConfig.PINNED_BLOCKS_ENABLED.get());
                ModConfig.save();
                button.setMessage(Component.literal("Sabit Bloklar: " + (ModConfig.PINNED_BLOCKS_ENABLED.get() ? "AÇIK" : "KAPALI")));
            }
        ).bounds(centerX, y, buttonWidth, 20).build());

        y += 35;
        this.addRenderableWidget(Button.builder(
            Component.literal("X Pozisyon: " + ModConfig.PINNED_BLOCKS_X.get()),
            button -> {
                int currentX = ModConfig.PINNED_BLOCKS_X.get();
                ModConfig.PINNED_BLOCKS_X.set(currentX + 10 > 1000 ? 0 : currentX + 10);
                ModConfig.save();
                button.setMessage(Component.literal("X Pozisyon: " + ModConfig.PINNED_BLOCKS_X.get()));
            }
        ).bounds(centerX, y, buttonWidth / 2 - 2, 20).build());

        this.addRenderableWidget(Button.builder(
            Component.literal("-"),
            button -> {
                int currentX = ModConfig.PINNED_BLOCKS_X.get();
                ModConfig.PINNED_BLOCKS_X.set(Math.max(0, currentX - 10));
                ModConfig.save();
                this.rebuildWidgets();
            }
        ).bounds(centerX + buttonWidth / 2 + 2, y, buttonWidth / 2 - 2, 20).build());

        y += 25;
        this.addRenderableWidget(Button.builder(
            Component.literal("Y Pozisyon: " + ModConfig.PINNED_BLOCKS_Y.get()),
            button -> {
                int currentY = ModConfig.PINNED_BLOCKS_Y.get();
                ModConfig.PINNED_BLOCKS_Y.set(currentY + 10 > 1000 ? 0 : currentY + 10);
                ModConfig.save();
                button.setMessage(Component.literal("Y Pozisyon: " + ModConfig.PINNED_BLOCKS_Y.get()));
            }
        ).bounds(centerX, y, buttonWidth / 2 - 2, 20).build());

        this.addRenderableWidget(Button.builder(
            Component.literal("-"),
            button -> {
                int currentY = ModConfig.PINNED_BLOCKS_Y.get();
                ModConfig.PINNED_BLOCKS_Y.set(Math.max(0, currentY - 10));
                ModConfig.save();
                this.rebuildWidgets();
            }
        ).bounds(centerX + buttonWidth / 2 + 2, y, buttonWidth / 2 - 2, 20).build());

        y += 35;
        this.addRenderableWidget(Button.builder(
            Component.literal("Elde Tutulan Bloğu Ekle"),
            button -> {
                if (this.minecraft != null && this.minecraft.player != null) {
                    var heldItem = this.minecraft.player.getMainHandItem();
                    if (!heldItem.isEmpty() && heldItem.getItem() instanceof net.minecraft.world.item.BlockItem) {
                        String blockId = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(heldItem.getItem()).toString();
                        var list = new java.util.ArrayList<String>(ModConfig.PINNED_BLOCKS_LIST.get());
                        if (!list.contains(blockId)) {
                            list.add(blockId);
                            ModConfig.PINNED_BLOCKS_LIST.set(list);
                            ModConfig.save();
                            this.rebuildWidgets();
                        }
                    }
                }
            }
        ).bounds(centerX, y, buttonWidth, 20).build());

        y += 35;
        java.util.List<? extends String> pinnedBlocks = ModConfig.PINNED_BLOCKS_LIST.get();
        for (int i = 0; i < pinnedBlocks.size(); i++) {
            final int index = i;
            String blockId = pinnedBlocks.get(i);
            String displayName = blockId.replace("minecraft:", "");

            this.addRenderableWidget(Button.builder(
                Component.literal(displayName),
                button -> {}
            ).bounds(centerX, y, buttonWidth - 30, 20).build());

            this.addRenderableWidget(Button.builder(
                Component.literal("X"),
                button -> {
                    var list = new java.util.ArrayList<String>(ModConfig.PINNED_BLOCKS_LIST.get());
                    list.remove(index);
                    ModConfig.PINNED_BLOCKS_LIST.set(list);
                    ModConfig.save();
                    this.rebuildWidgets();
                }
            ).bounds(centerX + buttonWidth - 25, y, 25, 20).build());

            y += 25;
        }
    }

    private void switchPage(ConfigPage page) {
        this.currentPage = page;
        this.rebuildWidgets();
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

        guiGraphics.drawCenteredString(this.font, currentPage.getTitle(), this.width / 2, 15, 0xFFFFFF);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        ModConfig.save();
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (currentPage == ConfigPage.DURABILITY) {
            for (var widget : this.children()) {
                if (widget instanceof DraggableButton draggable && draggable.isMouseOver(mouseX, mouseY)) {
                    draggingButton = draggable;
                    dragOffsetX = (int)(mouseX - draggable.getX());
                    dragOffsetY = (int)(mouseY - draggable.getY());
                    dragStartIndex = draggable.originalIndex;
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (draggingButton != null && currentPage == ConfigPage.DURABILITY) {
            draggingButton.setPosition((int)mouseX - dragOffsetX, (int)mouseY - dragOffsetY);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (draggingButton != null) {
            // Save position for the dragged item (by its start index)
            int newX = draggingButton.getX();
            int newY = draggingButton.getY();

            // ensure lists exist and are big enough
            var posX = new ArrayList<>(ModConfig.ITEM_POS_X.get());
            var posY = new ArrayList<>(ModConfig.ITEM_POS_Y.get());
            var orderList = new ArrayList<>(ModConfig.ITEM_ORDER.get());

            while (posX.size() < orderList.size()) posX.add(0);
            while (posY.size() < orderList.size()) posY.add(0);

            if (dragStartIndex >= 0 && dragStartIndex < orderList.size()) {
                posX.set(dragStartIndex, newX);
                posY.set(dragStartIndex, newY);
                ModConfig.ITEM_POS_X.set(posX);
                ModConfig.ITEM_POS_Y.set(posY);
                ModConfig.save();
            }

            java.util.List<DraggableButton> allButtons = new ArrayList<>();
            for (var widget : this.children()) {
                if (widget instanceof DraggableButton draggable && draggable != draggingButton) {
                    allButtons.add(draggable);
                }
            }

            allButtons.sort((a, b) -> Integer.compare(a.getY(), b.getY()));

            int dragCenterY = draggingButton.getY() + draggingButton.getHeight() / 2;
            int targetIndex = orderList.size() - 1;

            if (allButtons.isEmpty()) {
                targetIndex = 0;
            } else if (dragCenterY < allButtons.get(0).getY() + allButtons.get(0).getHeight() / 2) {
                targetIndex = 0;
            } else {
                boolean found = false;
                for (DraggableButton btn : allButtons) {
                    int btnCenterY = btn.getY() + btn.getHeight() / 2;

                    if (dragCenterY < btnCenterY) {
                        targetIndex = btn.originalIndex;
                        if (targetIndex > dragStartIndex) {
                            targetIndex--;
                        }
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    targetIndex = orderList.size() - 1;
                }
            }

            if (targetIndex != dragStartIndex) {
                reorderItems(dragStartIndex, targetIndex);
            }

            draggingButton = null;
            dragStartIndex = -1;
            rebuildDurabilityButtons();
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void reorderItems(int fromIndex, int toIndex) {
        var orderList = new ArrayList<String>(ModConfig.ITEM_ORDER.get());
        if (fromIndex >= 0 && fromIndex < orderList.size() && toIndex >= 0 && toIndex < orderList.size()) {
            String item = orderList.remove(fromIndex);
            orderList.add(toIndex, item);
            ModConfig.ITEM_ORDER.set(orderList);

            // Also reorder saved positions to match same movement
            var posX = new ArrayList<>(ModConfig.ITEM_POS_X.get());
            var posY = new ArrayList<>(ModConfig.ITEM_POS_Y.get());
            while (posX.size() < orderList.size()) posX.add(0);
            while (posY.size() < orderList.size()) posY.add(0);

            // move values in pos lists
            int size = orderList.size();
            if (fromIndex >= 0 && fromIndex < size && toIndex >= 0 && toIndex < size) {
                int vx = posX.remove(fromIndex);
                int vy = posY.remove(fromIndex);
                posX.add(toIndex, vx);
                posY.add(toIndex, vy);
            }

            ModConfig.ITEM_POS_X.set(posX);
            ModConfig.ITEM_POS_Y.set(posY);
            ModConfig.save();
        }
    }

    private void rebuildDurabilityButtons() {
        this.clearWidgets();

        int buttonWidth = 80;
        int spacing = 5;
        int totalWidth = (buttonWidth * 3) + (spacing * 2);
        int startX = (this.width - totalWidth) / 2;
        int tabY = this.height - 30;

        this.addRenderableWidget(Button.builder(
            Component.literal(ConfigPage.MAIN.getTitle()),
            button -> switchPage(ConfigPage.MAIN)
        ).bounds(startX, tabY, buttonWidth, 20).build());

        this.addRenderableWidget(Button.builder(
            Component.literal(ConfigPage.DURABILITY.getTitle()),
            button -> switchPage(ConfigPage.DURABILITY)
        ).bounds(startX + buttonWidth + spacing, tabY, buttonWidth, 20).build());

        this.addRenderableWidget(Button.builder(
            Component.literal(ConfigPage.PINNED_BLOCKS.getTitle()),
            button -> switchPage(ConfigPage.PINNED_BLOCKS)
        ).bounds(startX + (buttonWidth + spacing) * 2, tabY, buttonWidth, 20).build());

        int y = 40;
        int itemButtonWidth = 200;
        int centerX = this.width / 2 - itemButtonWidth / 2;

        var orderList = new ArrayList<String>(ModConfig.ITEM_ORDER.get());
        var posX = new ArrayList<>(ModConfig.ITEM_POS_X.get());
        var posY = new ArrayList<>(ModConfig.ITEM_POS_Y.get());
        while (posX.size() < orderList.size()) posX.add(0);
        while (posY.size() < orderList.size()) posY.add(0);

        for (int i = 0; i < orderList.size(); i++) {
            String itemKey = orderList.get(i);
            int px = posX.get(i);
            int py = posY.get(i);
            // if no saved position, fallback to vertical list position
            int useX = (px != 0) ? px : centerX;
            int useY = (py != 0) ? py : y;
            addDraggableItemToggle(itemKey, i, useX, useY);
            y += 25;
        }
    }

    private void addDraggableItemToggle(String itemKey, int index, int x, int y) {
        Map<String, String> labels = new HashMap<>();
        labels.put("blocks", "Bloklar (Elde Tutulan)");
        labels.put("sword", "Kılıç");
        labels.put("pickaxe", "Kazma");
        labels.put("axe", "Balta");
        labels.put("shovel", "Kürek");
        labels.put("hoe", "Çapa");
        labels.put("helmet", "Miğfer");
        labels.put("chestplate", "Göğüslük");
        labels.put("leggings", "Pantolon");
        labels.put("boots", "Çizme");
        labels.put("shield", "Kalkan");
        labels.put("elytra", "Elytra");
        labels.put("bow", "Yay");
        labels.put("crossbow", "Tatar Yayı");
        labels.put("trident", "Üç Dişli Mızrak");
        labels.put("fishing_rod", "Olta");
        labels.put("shears", "Makas");

        Map<String, net.minecraftforge.common.ForgeConfigSpec.BooleanValue> configs = new HashMap<>();
        configs.put("blocks", ModConfig.SHOW_BLOCKS);
        configs.put("sword", ModConfig.SHOW_SWORD);
        configs.put("pickaxe", ModConfig.SHOW_PICKAXE);
        configs.put("axe", ModConfig.SHOW_AXE);
        configs.put("shovel", ModConfig.SHOW_SHOVEL);
        configs.put("hoe", ModConfig.SHOW_HOE);
        configs.put("helmet", ModConfig.SHOW_HELMET);
        configs.put("chestplate", ModConfig.SHOW_CHESTPLATE);
        configs.put("leggings", ModConfig.SHOW_LEGGINGS);
        configs.put("boots", ModConfig.SHOW_BOOTS);
        configs.put("shield", ModConfig.SHOW_SHIELD);
        configs.put("elytra", ModConfig.SHOW_ELYTRA);
        configs.put("bow", ModConfig.SHOW_BOW);
        configs.put("crossbow", ModConfig.SHOW_CROSSBOW);
        configs.put("trident", ModConfig.SHOW_TRIDENT);
        configs.put("fishing_rod", ModConfig.SHOW_FISHING_ROD);
        configs.put("shears", ModConfig.SHOW_SHEARS);

        String label = labels.getOrDefault(itemKey, itemKey);
        var config = configs.get(itemKey);

        if (config != null) {
            DraggableButton button = new DraggableButton(x, y, 200, 20, label, config, index);
            // ensure button is placed at saved position
            button.setPosition(x, y);
            this.addRenderableWidget(button);
        }
    }

    private class DraggableButton extends Button {
        private final String label;
        private final net.minecraftforge.common.ForgeConfigSpec.BooleanValue config;
        private final int originalIndex;

        public DraggableButton(int x, int y, int width, int height, String label,
                              net.minecraftforge.common.ForgeConfigSpec.BooleanValue config, int index) {
            super(x, y, width, height,
                  Component.literal(label + ": " + (config.get() ? "GÖSTER" : "GİZLE")),
                  button -> {
                      config.set(!config.get());
                      ModConfig.save();
                      button.setMessage(Component.literal(label + ": " + (config.get() ? "GÖSTER" : "GİZLE")));
                  },
                  DEFAULT_NARRATION);
            this.label = label;
            this.config = config;
            this.originalIndex = index;
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            if (this == draggingButton) {
                guiGraphics.fill(this.getX() - 2, this.getY() - 2,
                               this.getX() + this.width + 2, this.getY() + this.height + 2,
                               0xFF00FF00);
            }
            super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        }
    }
}
