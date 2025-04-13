package net.nic.npc.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;


public class RecruitMenu extends AbstractContainerMenu {

    public RecruitMenu(int containerId, Inventory playerInventory) {
        super(ModMenus.RECRUIT_MENU.get(), containerId);
        }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

}
