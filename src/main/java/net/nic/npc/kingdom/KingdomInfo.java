package net.nic.npc.kingdom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.nic.npc.entity.NpcCitizen;

import java.util.ArrayList;
import java.util.List;

public class KingdomInfo {

    private String kingdomName;
    private final ServerLevel level;
    private final Player owner;
    private final List<NpcCitizen> registeredCitizens;
    private int population;
    private float happiness;
    private int foodValue;
    private int neededFood;

    public KingdomInfo(String name, ServerLevel level, Player owner) {
        kingdomName = name;
        this.level = level;
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
}
