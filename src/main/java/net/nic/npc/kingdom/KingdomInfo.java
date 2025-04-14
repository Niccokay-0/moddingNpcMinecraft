package net.nic.npc.kingdom;

import net.nic.npc.entity.NpcCitizen;

import java.util.ArrayList;
import java.util.List;

public class KingdomInfo {

    KingdomInfo info = KingdomInfoManager.get(player);

// then:
    int pop = info.getPopulation();
    float happy = info.getCitizensHappiness();

    public static final List<NpcCitizen> REGISTERED_CITIZENS = new ArrayList<>();
    public static int POPULATION;
    public static float HAPPINESS;

    public static List<NpcCitizen> getRegisteredCitizens() {
        return REGISTERED_CITIZENS;
    }

    public static float getCitizensHappiness() {
        float averageHappiness = 0f;
        for (NpcCitizen citizen : REGISTERED_CITIZENS) {
            averageHappiness += citizen.getHappiness()/200;
        }
        if (!REGISTERED_CITIZENS.isEmpty()) {
            HAPPINESS = averageHappiness / REGISTERED_CITIZENS.size();
        } else HAPPINESS = 0;
        return HAPPINESS * 100;
    }

    public static int getPopulation() {
        POPULATION = getRegisteredCitizens().size();
        return POPULATION;
    }
    public static int FOOD_VALUE;
    public static int NEEDED_FOOD;


    public static void addFoodValue(int foodValue) {
        FOOD_VALUE = FOOD_VALUE + foodValue;
    }
    public static void setFoodValue(int foodValue) {
        FOOD_VALUE = foodValue;
    }

    public static int getFoodValue() {
        return FOOD_VALUE;
    }

    public static void setNeededFood() {
        NEEDED_FOOD = getPopulation()*90;
    }

    public static int getNeededFood() {
        return NEEDED_FOOD;
    }

    public static int foodColor(int foodValue, int foodNeeded) {
        if (foodValue > (foodNeeded + (foodNeeded/4))) {
            return 0x00FF00; //green

        }
        if (foodValue > (foodNeeded - (foodNeeded/10)) || foodValue > (foodNeeded + (foodNeeded/10))) {
            return 0xFFA500; //orange
        }

        if (foodValue < (foodNeeded - (foodNeeded/25))) {
            return 0xFF0000; //red
        }
        else return 0xFFFFFF;
    }

    public static int happinessColor() {
        if (HAPPINESS >= 0.40f) {
            return 0x00FF00; //green

        }
        if (HAPPINESS > 0.10f && HAPPINESS < 0.40f) {
            return 0xFFA500; //orange
        }

        if (HAPPINESS < 0.10f) {
            return 0xFF0000; //red
        }
        else return 0xFFFFFF;
    }

}