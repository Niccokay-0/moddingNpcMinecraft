package net.nic.npc;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.nic.npc.block.ModBlocks;
import net.nic.npc.creativetabs.ModCreativeModeTabs;
import net.nic.npc.entity.EntityNpc;
import net.nic.npc.entity.ModEntities;
import net.nic.npc.entity.NpcCitizen;
import net.nic.npc.entity.Renderer.NpcCitizenRenderer;
import net.nic.npc.entity.Renderer.NpcRenderer;
import net.nic.npc.item.ModItems;
import net.nic.npc.menu.ModMenus;
import net.nic.npc.menu.screen.CommandingTableScreen;
import net.nic.npc.menu.screen.RecruitScreen;
import org.slf4j.Logger;

@Mod(NpcMain.MOD_ID)
public class NpcMain {
    public static final String MOD_ID = "npc";
    public static final Logger LOGGER = LogUtils.getLogger();

    public NpcMain() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModMenus.register(modEventBus);
        ModEntities.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {}
    private void addCreative(BuildCreativeModeTabContentsEvent event) {}

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class CommonModEvents {
        @SubscribeEvent
        public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
            event.put(ModEntities.NPC.get(), EntityNpc.createAttributes().build());
            event.put(ModEntities.NPC_CITIZEN.get(), EntityNpc.createAttributes().build());
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(ModMenus.COMMAND_TABLE_MENU.get(), CommandingTableScreen::new);
            MenuScreens.register(ModMenus.RECRUIT_MENU.get(), RecruitScreen::new);
            EntityRenderers.register(ModEntities.NPC.get(), (EntityRendererProvider<EntityNpc>) NpcRenderer::new);
            EntityRenderers.register(ModEntities.NPC_CITIZEN.get(), (EntityRendererProvider<NpcCitizen>) NpcCitizenRenderer::new);
        }
    }
}
