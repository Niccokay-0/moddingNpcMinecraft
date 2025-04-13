package net.nic.npc.creativetabs;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.nic.npc.NpcMain;
import net.nic.npc.block.ModBlocks;
import net.nic.npc.item.ModItems;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NpcMain.MOD_ID);


    public static final RegistryObject<CreativeModeTab> NPCBLOCKTAB = TABS.register("block_tab", ()-> CreativeModeTab.builder().icon(()-> new ItemStack(Items.DIAMOND_BLOCK)).title(Component.translatable("creativetab.npc.npcblocks")).displayItems(((itemDisplayParameters, output) -> {
        output.accept(ModBlocks.COMMANDING_TABLE.get());
        output.accept(ModItems.NPC_SPAWN_EGG.get());
    })).build());

    public static void register(IEventBus eventBus)     {
        TABS.register(eventBus);

    }
}
