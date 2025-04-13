package net.nic.npc.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.nic.npc.entity.EntityNpcCitizen;
import org.jetbrains.annotations.NotNull;

public class ModSpawnEggItem extends SpawnEggItem {

    public ModSpawnEggItem(@NotNull EntityType<EntityNpcCitizen> entityNpcCitizenEntityType, int backgroundcolor, int foregroundcolor, Item.Properties properties) {
        super(entityNpcCitizenEntityType,backgroundcolor,foregroundcolor,properties);
    }
}
