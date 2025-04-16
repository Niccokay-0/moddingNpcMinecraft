package net.nic.npc.menu.menus;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.nic.npc.menu.ModMenus;


public class CommandingTableMenu extends AbstractContainerMenu {
    public CommandingTableMenu(int containerId, Inventory playerInventory) {
        super(ModMenus.COMMAND_TABLE_MENU.get(), containerId);

        // bottom-left corner
        int xOffset = 10;
        int yOffset = 280;

        //player inv
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9,
                        xOffset + col * 18,
                        yOffset + row * 18));
            }
        }

        // hotbarrr
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col,
                    xOffset + col * 18,
                    yOffset + 3 * 18 + 4)); // little gap under inventory
        }
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        return ItemStack.EMPTY;
    }


    @Override
    public boolean stillValid(Player player) {
        return true; // Always valid for now
    }


}
