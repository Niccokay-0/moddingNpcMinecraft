package net.nic.npc.initentities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nic.npc.NpcMain;
import net.nic.npc.entity.EntityNpc;
import net.nic.npc.entity.NpcCitizen;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, NpcMain.MOD_ID);

    public static final RegistryObject<EntityType<EntityNpc>> NPC =
            ENTITY_TYPES.register("npc",
                    () -> EntityType.Builder.<EntityNpc>of(EntityNpc::new, MobCategory.MISC)
                            .sized(0.6f, 1.8f)
                            .build(ResourceLocation.fromNamespaceAndPath(NpcMain.MOD_ID, "npc").toString()));

    public static final RegistryObject<EntityType<NpcCitizen>> NPC_CITIZEN =
            ENTITY_TYPES.register("npc_citizen",
                    () -> EntityType.Builder.<NpcCitizen>of(NpcCitizen::new, MobCategory.MISC)
                            .sized(0.6f, 1.8f)
                            .build(ResourceLocation.fromNamespaceAndPath(NpcMain.MOD_ID, "npc_citizen").toString()));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
