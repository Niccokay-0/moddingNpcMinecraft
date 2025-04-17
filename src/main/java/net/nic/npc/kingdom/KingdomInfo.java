package net.nic.npc.kingdom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.nic.npc.entity.NpcCitizen;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KingdomInfo {

    private String kingdomName;
    private final Player owner;
    private final List<NpcCitizen> registeredCitizens;
    private int population;
    private float happiness;
    private int foodValue;
    private int neededFood;

    public KingdomInfo(String name, ServerLevel level, Player owner) {
        this.kingdomName = name;
        this.owner = owner;

        this.registeredCitizens = new ArrayList<>();
        this.population = 0;
        this.happiness = 0f;
        this.foodValue = 0;
        this.neededFood = 0;
    }


    public KingdomInfo getKingdom()    {
        return this;
    }

    public String getKingdomName()  {
        return kingdomName;
    }


    public List<String> getCitizenNames() {
        List<String> names = new ArrayList<>();
        for (NpcCitizen citizen : registeredCitizens) {
            names.add(citizen.getName().getString()); // Assumes getName() returns a Component
        }
        return names;
    }


    public List<NpcCitizen> getRegisteredCitizens() {
        return registeredCitizens;
    }

    public void registerCitizen(NpcCitizen citizen) {
        registeredCitizens.add(citizen);
    }

    public float getCitizensHappiness() {
        float averageHappiness = 0f;
        for (NpcCitizen citizen : registeredCitizens) {
            averageHappiness += citizen.getHappiness() / 200;
        }
        if (!registeredCitizens.isEmpty()) {
            happiness = averageHappiness / registeredCitizens.size();
        } else {
            happiness = 0;
        }
        return happiness * 100;
    }

    public int getPopulation() {
        population = registeredCitizens.size();
        return population;
    }

    public void addFoodValue(int foodValue) {
        this.foodValue += foodValue;
    }

    public void setFoodValue(int foodValue) {
        this.foodValue = foodValue;
    }

    public int getFoodValue() {
        return foodValue;
    }

    public void updateNeededFood() {
        neededFood = getPopulation() * 90;
    }

    public int getNeededFood() {
        return neededFood;
    }

    public int foodColor() {
        if (foodValue > (neededFood + (neededFood / 4))) {
            return 0x00FF00; // green
        }
        if (foodValue > (neededFood - (neededFood / 10)) || foodValue > (neededFood + (neededFood / 10))) {
            return 0xFFA500; // orange
        }
        if (foodValue < (neededFood - (neededFood / 25))) {
            return 0xFF0000; // red
        }
        return 0xFFFFFF;
    }



    public int happinessColor() {
        if (happiness >= 0.40f) {
            return 0x00FF00; // green
        }
        if (happiness > 0.10f && happiness < 0.40f) {
            return 0xFFA500; // orange
        }
        if (happiness < 0.10f) {
            return 0xFF0000; // red
        }
        return 0xFFFFFF;
    }


    public CompoundTag save(CompoundTag tag) {
        tag.putString("KingdomName", kingdomName);
        tag.putUUID("OwnerUUID", owner.getUUID());

        tag.putInt("Population", getPopulation());
        tag.putFloat("Happiness", happiness);
        tag.putInt("FoodValue", foodValue);
        tag.putInt("NeededFood", neededFood);

        // Optionally save citizen names for debugging/logging (not full entities)
        ListTag citizenList = new ListTag();
        for (NpcCitizen citizen : registeredCitizens) {
            citizenList.add(StringTag.valueOf(citizen.getName().getString()));
        }
        tag.put("CitizenNames", citizenList);

        return tag;
    }

    public static KingdomInfo load(CompoundTag tag, ServerLevel level) {
        String kingdomName = tag.getString("KingdomName");
        UUID ownerUUID = tag.getUUID("OwnerUUID");

        Player owner = level.getPlayerByUUID(ownerUUID);
        if (owner == null) {
            throw new IllegalStateException("Owner player not found in world when loading kingdom.");
        }

        KingdomInfo info = new KingdomInfo(kingdomName, level, owner);
        info.population = tag.getInt("Population");
        info.happiness = tag.getFloat("Happiness");
        info.foodValue = tag.getInt("FoodValue");
        info.neededFood = tag.getInt("NeededFood");

        // Citizen list loading can be implemented later with actual entity data if needed
        return info;
    }
}
