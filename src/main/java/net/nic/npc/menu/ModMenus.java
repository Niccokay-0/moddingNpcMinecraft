package net.nic.npc.menu;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.nic.npc.NpcMain;
import net.nic.npc.menu.menus.CommandingTableMenu;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nic.npc.menu.menus.RecruitMenu;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENU =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, NpcMain.MOD_ID); // Replace with your MODID

    public static final RegistryObject<MenuType<CommandingTableMenu>> COMMAND_TABLE_MENU =
            MENU.register("command_table_menu", () ->
                    new MenuType<>((containerId, inventory) ->
                            new CommandingTableMenu(containerId, inventory, inventory.player),
                            FeatureFlags.VANILLA_SET));

    public static final RegistryObject<MenuType<RecruitMenu>> RECRUIT_MENU =
            MENU.register("recruit_menu",
                    () -> new MenuType<>(RecruitMenu::new, net.minecraft.world.flag.FeatureFlags.VANILLA_SET));


    public static void register(IEventBus eventBus) {
        MENU.register(eventBus);
    }

}
