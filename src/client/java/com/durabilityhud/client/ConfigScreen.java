package com.durabilityhud.client;

import com.durabilityhud.config.ModConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

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
        DURABILITY("Dayaniklilik Ayarlari"),
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
        super(Text.literal("Durability HUD Ayarlari"));
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

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal(ConfigPage.MAIN.getTitle()),
            button -> switchPage(ConfigPage.MAIN)
        ).dimensions(startX, tabY, buttonWidth, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal(ConfigPage.DURABILITY.getTitle()),
            button -> switchPage(ConfigPage.DURABILITY)
        ).dimensions(startX + buttonWidth + spacing, tabY, buttonWidth, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal(ConfigPage.PINNED_BLOCKS.getTitle()),
            button -> switchPage(ConfigPage.PINNED_BLOCKS)
        ).dimensions(startX + (buttonWidth + spacing) * 2, tabY, buttonWidth, 20).build());
    }

    private void initMainPage() {
        int y = 40;
        int buttonWidth = 200;
        int centerX = this.width / 2 - buttonWidth / 2;

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("HUD Aktif: " + (ModConfig.isEnabled() ? "ACIK" : "KAPALI")),
            button -> {
                ModConfig.setEnabled(!ModConfig.isEnabled());
                ModConfig.save();
                button.setMessage(Text.literal("HUD Aktif: " + (ModConfig.isEnabled() ? "ACIK" : "KAPALI")));
            }
        ).dimensions(centerX, y, buttonWidth, 20).build());

        y += 35;
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("X Pozisyon: " + ModConfig.getHudX()),
            button -> {
                int currentX = ModConfig.getHudX();
                ModConfig.setHudX(currentX + 10 > 1000 ? 0 : currentX + 10);
                ModConfig.save();
                button.setMessage(Text.literal("X Pozisyon: " + ModConfig.getHudX()));
            }
        ).dimensions(centerX, y, buttonWidth / 2 - 2, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("-"),
            button -> {
                int currentX = ModConfig.getHudX();
                ModConfig.setHudX(Math.max(0, currentX - 10));
                ModConfig.save();
                this.clearAndInit();
            }
        ).dimensions(centerX + buttonWidth / 2 + 2, y, buttonWidth / 2 - 2, 20).build());

        y += 25;
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Y Pozisyon: " + ModConfig.getHudY()),
            button -> {
                int currentY = ModConfig.getHudY();
                ModConfig.setHudY(currentY + 10 > 1000 ? 0 : currentY + 10);
                ModConfig.save();
                button.setMessage(Text.literal("Y Pozisyon: " + ModConfig.getHudY()));
            }
        ).dimensions(centerX, y, buttonWidth / 2 - 2, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("-"),
            button -> {
                int currentY = ModConfig.getHudY();
                ModConfig.setHudY(Math.max(0, currentY - 10));
                ModConfig.save();
                this.clearAndInit();
            }
        ).dimensions(centerX + buttonWidth / 2 + 2, y, buttonWidth / 2 - 2, 20).build());

        y += 25;
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("GUI Boyutu: " + String.format("%.1f", ModConfig.getHudScale())),
            button -> {
                double scale = ModConfig.getHudScale();
                ModConfig.setHudScale(scale + 0.1 > 3.0 ? 0.5 : scale + 0.1);
                ModConfig.save();
                button.setMessage(Text.literal("GUI Boyutu: " + String.format("%.1f", ModConfig.getHudScale())));
            }
        ).dimensions(centerX, y, buttonWidth / 2 - 2, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("-"),
            button -> {
                double scale = ModConfig.getHudScale();
                ModConfig.setHudScale(Math.max(0.1, scale - 0.1));
                ModConfig.save();
                this.clearAndInit();
            }
        ).dimensions(centerX + buttonWidth / 2 + 2, y, buttonWidth / 2 - 2, 20).build());
    }

    private void initDurabilityPage() {
        rebuildDurabilityButtons();
    }

    private void initPinnedBlocksPage() {
        int y = 40;
        int buttonWidth = 200;
        int centerX = this.width / 2 - buttonWidth / 2;

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Sabit Bloklar: " + (ModConfig.isPinnedBlocksEnabled() ? "ACIK" : "KAPALI")),
            button -> {
                ModConfig.setPinnedBlocksEnabled(!ModConfig.isPinnedBlocksEnabled());
                ModConfig.save();
                button.setMessage(Text.literal("Sabit Bloklar: " + (ModConfig.isPinnedBlocksEnabled() ? "ACIK" : "KAPALI")));
            }
        ).dimensions(centerX, y, buttonWidth, 20).build());

        y += 35;
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("X Pozisyon: " + ModConfig.getPinnedBlocksX()),
            button -> {
                int currentX = ModConfig.getPinnedBlocksX();
                ModConfig.setPinnedBlocksX(currentX + 10 > 1000 ? 0 : currentX + 10);
                ModConfig.save();
                button.setMessage(Text.literal("X Pozisyon: " + ModConfig.getPinnedBlocksX()));
            }
        ).dimensions(centerX, y, buttonWidth / 2 - 2, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("-"),
            button -> {
                int currentX = ModConfig.getPinnedBlocksX();
                ModConfig.setPinnedBlocksX(Math.max(0, currentX - 10));
                ModConfig.save();
                this.clearAndInit();
            }
        ).dimensions(centerX + buttonWidth / 2 + 2, y, buttonWidth / 2 - 2, 20).build());

        y += 25;
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Y Pozisyon: " + ModConfig.getPinnedBlocksY()),
            button -> {
                int currentY = ModConfig.getPinnedBlocksY();
                ModConfig.setPinnedBlocksY(currentY + 10 > 1000 ? 0 : currentY + 10);
                ModConfig.save();
                button.setMessage(Text.literal("Y Pozisyon: " + ModConfig.getPinnedBlocksY()));
            }
        ).dimensions(centerX, y, buttonWidth / 2 - 2, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("-"),
            button -> {
                int currentY = ModConfig.getPinnedBlocksY();
                ModConfig.setPinnedBlocksY(Math.max(0, currentY - 10));
                ModConfig.save();
                this.clearAndInit();
            }
        ).dimensions(centerX + buttonWidth / 2 + 2, y, buttonWidth / 2 - 2, 20).build());

        y += 35;
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Elde Tutulan Blogu Ekle"),
            button -> {
                if (this.client != null && this.client.player != null) {
                    var heldItem = this.client.player.getMainHandStack();
                    if (!heldItem.isEmpty() && heldItem.getItem() instanceof BlockItem) {
                        String blockId = Registries.ITEM.getId(heldItem.getItem()).toString();
                        var list = new ArrayList<>(ModConfig.getPinnedBlocksList());
                        if (!list.contains(blockId)) {
                            list.add(blockId);
                            ModConfig.setPinnedBlocksList(list);
                            ModConfig.save();
                            this.clearAndInit();
                        }
                    }
                }
            }
        ).dimensions(centerX, y, buttonWidth, 20).build());

        y += 35;
        List<String> pinnedBlocks = ModConfig.getPinnedBlocksList();
        for (int i = 0; i < pinnedBlocks.size(); i++) {
            final int index = i;
            String blockId = pinnedBlocks.get(i);
            String displayName = blockId.replace("minecraft:", "");

            this.addDrawableChild(ButtonWidget.builder(
                Text.literal(displayName),
                button -> {}
            ).dimensions(centerX, y, buttonWidth - 30, 20).build());

            this.addDrawableChild(ButtonWidget.builder(
                Text.literal("X"),
                button -> {
                    var list = new ArrayList<>(ModConfig.getPinnedBlocksList());
                    list.remove(index);
                    ModConfig.setPinnedBlocksList(list);
                    ModConfig.save();
                    this.clearAndInit();
                }
            ).dimensions(centerX + buttonWidth - 25, y, 25, 20).build());

            y += 25;
        }
    }

    private void switchPage(ConfigPage page) {
        this.currentPage = page;
        this.clearAndInit();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        context.drawCenteredTextWithShadow(this.textRenderer, currentPage.getTitle(), this.width / 2, 15, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        ModConfig.save();
        if (this.client != null) {
            this.client.setScreen(this.parent);
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
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggingButton != null && currentPage == ConfigPage.DURABILITY) {
            draggingButton.setPosition((int)mouseX - dragOffsetX, (int)mouseY - dragOffsetY);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (draggingButton != null) {
            int newX = draggingButton.getX();
            int newY = draggingButton.getY();

            var posX = new ArrayList<>(ModConfig.getItemPosX());
            var posY = new ArrayList<>(ModConfig.getItemPosY());
            var orderList = new ArrayList<>(ModConfig.getItemOrder());

            while (posX.size() < orderList.size()) posX.add(0);
            while (posY.size() < orderList.size()) posY.add(0);

            if (dragStartIndex >= 0 && dragStartIndex < orderList.size()) {
                posX.set(dragStartIndex, newX);
                posY.set(dragStartIndex, newY);
                ModConfig.setItemPosX(posX);
                ModConfig.setItemPosY(posY);
                ModConfig.save();
            }

            List<DraggableButton> allButtons = new ArrayList<>();
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
        var orderList = new ArrayList<>(ModConfig.getItemOrder());
        if (fromIndex >= 0 && fromIndex < orderList.size() && toIndex >= 0 && toIndex < orderList.size()) {
            String item = orderList.remove(fromIndex);
            orderList.add(toIndex, item);
            ModConfig.setItemOrder(orderList);

            var posX = new ArrayList<>(ModConfig.getItemPosX());
            var posY = new ArrayList<>(ModConfig.getItemPosY());
            while (posX.size() < orderList.size()) posX.add(0);
            while (posY.size() < orderList.size()) posY.add(0);

            int size = orderList.size();
            if (fromIndex >= 0 && fromIndex < size && toIndex >= 0 && toIndex < size) {
                int vx = posX.remove(fromIndex);
                int vy = posY.remove(fromIndex);
                posX.add(toIndex, vx);
                posY.add(toIndex, vy);
            }

            ModConfig.setItemPosX(posX);
            ModConfig.setItemPosY(posY);
            ModConfig.save();
        }
    }

    private void rebuildDurabilityButtons() {
        this.clearChildren();

        int buttonWidth = 80;
        int spacing = 5;
        int totalWidth = (buttonWidth * 3) + (spacing * 2);
        int startX = (this.width - totalWidth) / 2;
        int tabY = this.height - 30;

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal(ConfigPage.MAIN.getTitle()),
            button -> switchPage(ConfigPage.MAIN)
        ).dimensions(startX, tabY, buttonWidth, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal(ConfigPage.DURABILITY.getTitle()),
            button -> switchPage(ConfigPage.DURABILITY)
        ).dimensions(startX + buttonWidth + spacing, tabY, buttonWidth, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal(ConfigPage.PINNED_BLOCKS.getTitle()),
            button -> switchPage(ConfigPage.PINNED_BLOCKS)
        ).dimensions(startX + (buttonWidth + spacing) * 2, tabY, buttonWidth, 20).build());

        int y = 40;
        int itemButtonWidth = 200;
        int centerX = this.width / 2 - itemButtonWidth / 2;

        var orderList = new ArrayList<>(ModConfig.getItemOrder());
        var posX = new ArrayList<>(ModConfig.getItemPosX());
        var posY = new ArrayList<>(ModConfig.getItemPosY());
        while (posX.size() < orderList.size()) posX.add(0);
        while (posY.size() < orderList.size()) posY.add(0);

        for (int i = 0; i < orderList.size(); i++) {
            String itemKey = orderList.get(i);
            int px = posX.get(i);
            int py = posY.get(i);
            int useX = (px != 0) ? px : centerX;
            int useY = (py != 0) ? py : y;
            addDraggableItemToggle(itemKey, i, useX, useY);
            y += 25;
        }
    }

    private void addDraggableItemToggle(String itemKey, int index, int x, int y) {
        Map<String, String> labels = new HashMap<>();
        labels.put("blocks", "Bloklar (Elde Tutulan)");
        labels.put("sword", "Kilic");
        labels.put("pickaxe", "Kazma");
        labels.put("axe", "Balta");
        labels.put("shovel", "Kurek");
        labels.put("hoe", "Capa");
        labels.put("helmet", "Migfer");
        labels.put("chestplate", "Gogusluk");
        labels.put("leggings", "Pantolon");
        labels.put("boots", "Cizme");
        labels.put("shield", "Kalkan");
        labels.put("elytra", "Elytra");
        labels.put("bow", "Yay");
        labels.put("crossbow", "Tatar Yayi");
        labels.put("trident", "Uc Disli Mizrak");
        labels.put("fishing_rod", "Olta");
        labels.put("shears", "Makas");

        Map<String, ConfigToggle> configs = new HashMap<>();
        configs.put("blocks", new ConfigToggle(ModConfig::isShowBlocks, ModConfig::setShowBlocks));
        configs.put("sword", new ConfigToggle(ModConfig::isShowSword, ModConfig::setShowSword));
        configs.put("pickaxe", new ConfigToggle(ModConfig::isShowPickaxe, ModConfig::setShowPickaxe));
        configs.put("axe", new ConfigToggle(ModConfig::isShowAxe, ModConfig::setShowAxe));
        configs.put("shovel", new ConfigToggle(ModConfig::isShowShovel, ModConfig::setShowShovel));
        configs.put("hoe", new ConfigToggle(ModConfig::isShowHoe, ModConfig::setShowHoe));
        configs.put("helmet", new ConfigToggle(ModConfig::isShowHelmet, ModConfig::setShowHelmet));
        configs.put("chestplate", new ConfigToggle(ModConfig::isShowChestplate, ModConfig::setShowChestplate));
        configs.put("leggings", new ConfigToggle(ModConfig::isShowLeggings, ModConfig::setShowLeggings));
        configs.put("boots", new ConfigToggle(ModConfig::isShowBoots, ModConfig::setShowBoots));
        configs.put("shield", new ConfigToggle(ModConfig::isShowShield, ModConfig::setShowShield));
        configs.put("elytra", new ConfigToggle(ModConfig::isShowElytra, ModConfig::setShowElytra));
        configs.put("bow", new ConfigToggle(ModConfig::isShowBow, ModConfig::setShowBow));
        configs.put("crossbow", new ConfigToggle(ModConfig::isShowCrossbow, ModConfig::setShowCrossbow));
        configs.put("trident", new ConfigToggle(ModConfig::isShowTrident, ModConfig::setShowTrident));
        configs.put("fishing_rod", new ConfigToggle(ModConfig::isShowFishingRod, ModConfig::setShowFishingRod));
        configs.put("shears", new ConfigToggle(ModConfig::isShowShears, ModConfig::setShowShears));

        String label = labels.getOrDefault(itemKey, itemKey);
        var config = configs.get(itemKey);

        if (config != null) {
            DraggableButton button = new DraggableButton(x, y, 200, 20, label, config, index);
            button.setPosition(x, y);
            this.addDrawableChild(button);
        }
    }

    private record ConfigToggle(java.util.function.BooleanSupplier getter, java.util.function.Consumer<Boolean> setter) {
        public boolean get() { return getter.getAsBoolean(); }
        public void set(boolean value) { setter.accept(value); }
    }

    private class DraggableButton extends ButtonWidget {
        private final String label;
        private final ConfigToggle config;
        private final int originalIndex;

        public DraggableButton(int x, int y, int width, int height, String label, ConfigToggle config, int index) {
            super(x, y, width, height,
                  Text.literal(label + ": " + (config.get() ? "GOSTER" : "GIZLE")),
                  button -> {
                      config.set(!config.get());
                      ModConfig.save();
                      button.setMessage(Text.literal(label + ": " + (config.get() ? "GOSTER" : "GIZLE")));
                  },
                  DEFAULT_NARRATION_SUPPLIER);
            this.label = label;
            this.config = config;
            this.originalIndex = index;
        }

        @Override
        protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
            if (this == draggingButton) {
                context.fill(this.getX() - 2, this.getY() - 2,
                           this.getX() + this.width + 2, this.getY() + this.height + 2,
                           0xFF00FF00);
            }
            super.renderButton(context, mouseX, mouseY, delta);
        }
    }
}
