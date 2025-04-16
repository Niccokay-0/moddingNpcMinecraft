package net.nic.npc.menu.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.nic.npc.NpcMain;
import net.nic.npc.entity.EntityNpc;

import net.nic.npc.menu.RecruitMenu;

import java.util.ArrayList;
import java.util.List;

public class RecruitScreen extends AbstractContainerScreen<RecruitMenu> {

    static EntityNpc npcC;

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(NpcMain.MOD_ID, "textures/gui/commanding_table.png"); // replace with actual path

    private final List<Button> displayButtons = new ArrayList<>();

    public RecruitScreen(RecruitMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        displayButtons.clear();

        int buttonWidth = 100;
        int buttonHeight = 20;
        int spacing = 10;
        int totalWidth = (buttonWidth * 2) + spacing;
        int startX = (this.width / 2) - (totalWidth / 2);
        int y = this.topPos + 10;

        Button recruitButton = Button.builder(
                        Component.translatable("npc.gui.button_recruit"),
                        btn -> onRecruitButtonPressed()
                )
                .bounds(startX, y, buttonWidth, buttonHeight)
                .build();

        Button dismissButton = Button.builder(
                        Component.translatable("npc.gui.button_dismiss"),
                        btn -> onDismissButtonPressed()
                )
                .bounds(startX + buttonWidth + spacing, y, buttonWidth, buttonHeight)
                .build();

        this.displayButtons.add(recruitButton);
        this.displayButtons.add(dismissButton);

        this.addRenderableWidget(recruitButton);
        this.addRenderableWidget(dismissButton);
    }

    private void onRecruitButtonPressed() {
        // TODO: Add recruitment logic
        Minecraft.getInstance().setScreen(null);
        npcC.transformToCitizen();
        System.out.println("Recruitment button pressed");
    }

    private void onDismissButtonPressed() {
        // TODO: Add dismiss logic
        Minecraft.getInstance().setScreen(null);
        System.out.println("Dismiss button pressed");
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    public static Void getNpc(EntityNpc npc) {
        npcC = npc;
        return null;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
        InventoryScreen.renderEntityInInventoryFollowsMouse(graphics,245,120,500,264,40,0,mouseX,mouseY,npcC);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pScrollX, double pScrollY) {
        return super.mouseScrolled(pMouseX, pMouseY, pScrollX, pScrollY);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        int py = imageHeight/2 - 40;
        int px = imageWidth/2 - 104;
        graphics.drawString(this.font, this.title, 8, 6, 0x404040, false);
        graphics.drawString(this.font, Component.literal(npcC.getFullName()),px,py,0xFFFFFF);
        graphics.drawString(this.font, Component.literal(npcC.getGender()),px, py + 25,npcC.getGenderColor(npcC.getGender()));
        graphics.drawString(this.font, Component.literal(npcC.getProfession()),px,py + 50, npcC.getProfessionColor(npcC.getProfession()));
        graphics.drawString(this.font, Component.literal(String.valueOf(npcC.getHappiness())),px,py + 75,npcC.getHappinessColor(npcC.getHappiness()));

    }



    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
