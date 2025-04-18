package net.nic.npc.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.nic.npc.entities.NpcCitizen;
import net.nic.npc.entities.KingdomInfo;
import net.nic.npc.menu.CommandingTableMenu;
import net.nic.npc.NpcMain;

import java.util.ArrayList;
import java.util.List;

public class CommandingTableScreen extends AbstractContainerScreen<CommandingTableMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(NpcMain.MOD_ID, "textures/gui/commanding_table.png");

    private static int displayMode = 0;
    private BlockPos mapCenter = BlockPos.ZERO;
    private float mapScale = 1.5f;
    private final List<Button> displayButtons = new ArrayList<>();

    public CommandingTableScreen(CommandingTableMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        if (menu != null) {
            this.mapCenter = new BlockPos(playerInventory.player.chunkPosition().getBlockX(16),
                    384,
                    playerInventory.player.chunkPosition().getBlockZ(16));
        }

    }

    @Override
    protected void init() {
        super.init();
        this.imageWidth = width;
        this.imageHeight = height;
        this.leftPos = 0;
        this.topPos = 0;

        String[] sectionKeys = {
                "info", "food", "military", "diplomacy", "territory", "infrastructure"
        };

        int btnW = 80, btnH = 20, spacing = 6;
        int totalWidth = (btnW + spacing) * sectionKeys.length;
        int startX = (this.width / 2) - (totalWidth / 2);
        int y = 10;

        for (int i = 0; i < sectionKeys.length; i++) {
            final int modeIndex = i;
            String key = sectionKeys[i];

            Button button = Button.builder(
                            Component.translatable("npc.gui.button_" + key),
                            b -> {
                                displayMode = modeIndex;
                                updateButtonHighlight();
                            })
                    .bounds(startX + i * (btnW + spacing), y, btnW, btnH)
                    .build();

            this.displayButtons.add(button);
            this.addRenderableWidget(button);
        }

    }

    private void updateButtonHighlight() {
        String[] sectionKeys = {
                "info","food", "military", "diplomacy", "territory", "infrastructure"
        };

        for (int i = 0; i < displayButtons.size(); i++) {
            Button button = displayButtons.get(i);
            button.setMessage(Component.translatable("npc.gui.button_" + sectionKeys[i]));
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics, mouseX, mouseY, delta);
        super.render(graphics, mouseX, mouseY, delta);
        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        MinimapRenderer.renderMinimap(graphics, this.minecraft.level, mapCenter, mapScale, this.width, this.height, this.font);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.title, 16, 16, 0xFFFFFF);
        graphics.drawString(this.font, this.playerInventoryTitle, 8, height - 96 + 2, 0xFFFFFF);

        switch (displayMode) {
            case 0 -> {
                // Info: Kingdom name, type, happiness, population
                graphics.drawString(this.font, Component.literal("Kingdom: Eldoria"), 20, 40, 0xFFFFFF);
                graphics.drawString(this.font, Component.literal("Type: Monarchy"), 20, 55, 0xFFFFFF);
                graphics.drawString(this.font, Component.literal(String.valueOf(KingdomInfo.getCitizensHappiness())), 20, 70, KingdomInfo.happinessColor());
                graphics.drawString(this.font, Component.literal(String.valueOf(KingdomInfo.getPopulation())), 20, 85, 0xFFFFFF);

                List<NpcCitizen> citizens = KingdomInfo.getRegisteredCitizens();
                int yOffset = 110;
                for (NpcCitizen citizen : citizens) {
                    String line = String.format(
                            "%s %s - %s - %s - %s",
                            citizen.getFName(),            // first name
                            citizen.getSName(),            // surname
                            citizen.getProfession(),       // e.g., Farmer
                            citizen.getHappiness(),        // e.g., Happy
                            citizen.getGender()            // e.g., Male/Female
                    );

                    graphics.drawString(this.font, Component.literal(line), 20, yOffset, 0xAAAAAA);
                    yOffset += 15;
                }

            }
            case 1 -> {
                graphics.drawString(this.font, Component.literal("Needed Food: " + KingdomInfo.getNeededFood()), 20, 40, KingdomInfo.foodColor(KingdomInfo.getFoodValue(),KingdomInfo.getNeededFood()));
                graphics.drawString(this.font, Component.literal("Food Value: " + KingdomInfo.getFoodValue()), 20, 60, KingdomInfo.foodColor(KingdomInfo.getFoodValue(),KingdomInfo.getNeededFood()));

            }
            case 2 -> {
                // Military
            }
            case 3 -> {
                // Diplomacy
            }
            case 4 -> {
                // Territory
            }
            case 5 -> {
                // Infrastructure
            }
        }
    }



    public static int getDisplayMode() {
        return displayMode;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
