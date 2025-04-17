package net.nic.npc.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.nic.npc.kingdom.KingdomInfo;
import net.nic.npc.kingdom.KingdomManager;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class NpcCitizen extends PathfinderMob {

    public boolean recruitable = false;

    private UUID OWNER;
    private static final EntityDataAccessor<String> DATA_NAME = SynchedEntityData.defineId(NpcCitizen.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> DATA_SURNAME = SynchedEntityData.defineId(NpcCitizen.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> DATA_GENDER = SynchedEntityData.defineId(NpcCitizen.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> DATA_VARIANT = SynchedEntityData.defineId(NpcCitizen.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> DATA_PROFESSION = SynchedEntityData.defineId(NpcCitizen.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Float> DATA_HAPPINESS = SynchedEntityData.defineId(NpcCitizen.class, EntityDataSerializers.FLOAT);

    public NpcCitizen(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setPersistenceRequired();
    }



    @Override
    protected void registerGoals() {

        this.goalSelector.addGoal(0, new FloatGoal(this)); // Swim if in water

        this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1.0D)); // Wander randomly
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this)); // Randomly look around


        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F)); // Look at nearby players
        // New AI goals
        this.goalSelector.addGoal(5, new OpenDoorGoal(this, true)); // Open doors (if they can)
        this.goalSelector.addGoal(6, new MoveThroughVillageGoal(this, 1.0D, false, 4, () -> true));
        this.goalSelector.addGoal(7, new AvoidEntityGoal<>(this, Zombie.class, 24.0F, 1.6D, 1.4D)); // Run from zombies


    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.MAX_HEALTH, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 16.0D);

    }

    public void setOwner(UUID id) {
        KingdomInfo kingdom = KingdomManager.getKingdom(id);
        kingdom.registerCitizen(this);
        OWNER = id;
    }

    public KingdomInfo getKingdom(UUID id) {
     return KingdomManager.getKingdom(id);
    }

    public UUID getOwnerUUid() {
        return OWNER;
    }

    public void setName(String name) {
        this.entityData.set(DATA_NAME, name);
    }
    public void setSurname(String surname) {
        this.entityData.set(DATA_SURNAME, surname);
    }
    public void setGender(String gender) {
        this.entityData.set(DATA_GENDER, gender);
    }
    public void setTextureVariant(int variant) {
        this.entityData.set(DATA_VARIANT, variant);
    }
    public void setProfession(String profession) {
        this.entityData.set(DATA_PROFESSION, profession);
    }
    public void setHappiness(float happiness) {
        this.entityData.set(DATA_HAPPINESS, happiness);
    }
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pSpawnType, @Nullable SpawnGroupData pSpawnGroupData) {
        String gender = pickGender();
        this.entityData.set(DATA_GENDER,gender);
        this.entityData.set(DATA_NAME, pickRandomName(gender));
        this.entityData.set(DATA_SURNAME, pickRandomSurname());
        this.entityData.set(DATA_VARIANT, pickTexture());
        return super.finalizeSpawn(pLevel, pDifficulty, pSpawnType, pSpawnGroupData);
    }
    @Override
    public void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_GENDER, "gender");
        pBuilder.define(DATA_NAME, "name");
        pBuilder.define(DATA_SURNAME, "surname");
        pBuilder.define(DATA_VARIANT, 1);
        //work and happiness
        pBuilder.define(DATA_PROFESSION, "Unemployed");
        pBuilder.define(DATA_HAPPINESS, 0.5f); // Range from 0.0f (sad) to 1.0f (happy)
        OWNER = null;

    }
    public String getFName() {
        return this.entityData.get(DATA_NAME);
    }
    public String getSName() {
        return this.entityData.get(DATA_SURNAME);
    }
    public String getGender() {
        return this.entityData.get(DATA_GENDER);
    }

    public int getTextureVariant() {
        return this.entityData.get(DATA_VARIANT);
    }
    private String pickRandomName(String gender) {
        String[] maleNames = {
                "Liam", "Noah", "Oliver", "Elijah", "James", "William", "Benjamin", "Lucas", "Henry", "Alexander", "Mason", "Michael", "Ethan", "Daniel", "Jacob", "Logan", "Jackson", "Levi", "Sebastian", "Mateo", "Jack", "Owen", "Theodore", "Aiden", "Samuel", "Joseph", "John", "David", "Wyatt", "Matthew", "Luke", "Asher", "Carter", "Julian", "Grayson", "Leo", "Jayden", "Gabriel", "Isaac", "Lincoln", "Anthony", "Hudson", "Dylan", "Ezra", "Thomas", "Charles", "Christopher", "Jaxon", "Maverick", "Josiah", "Andrew", "Elias", "Joshua", "Nathan", "Caleb", "Ryan", "Adrian", "Miles", "Eli", "Nolan", "Christian", "Aaron", "Cameron", "Ezekiel", "Colton", "Luca", "Landon", "Hunter", "Jonathan", "Santiago", "Axel", "Easton", "Cooper", "Jeremiah", "Angel", "Roman", "Connor", "Jameson", "Robert", "Greyson", "Jordan", "Ian", "Carson", "Jaxson", "Leonardo", "Nicholas", "Dominic", "Austin", "Everett", "Brooks", "Xavier", "Kai", "Jose", "Parker", "Adam", "Jace", "Wesley", "Kayden", "Silas", "Declan", "Weston", "Bennett", "Micah", "Blake", "Waylon", "Damian", "Brayden", "Vincent", "Ryder", "Tristan", "Bentley", "Luis", "George", "Kai", "Zion", "Carlos", "Max", "Ryker", "Ashton", "Diego", "Braxton", "Ivan", "Giovanni", "Rowan", "Harrison", "Jasper", "Brandon", "Jonah", "Kaiden", "Bryson", "Amir", "Kingston", "Archer", "Theo", "Enzo", "Kevin", "Judah", "Beau", "Tucker", "Finn", "Grant", "Emmett", "Emmanuel", "Corbin", "Rhett", "Edward", "August", "Nicolas", "Emilio", "Colin", "Tyler", "Phoenix", "Jude", "Alex", "Karter", "Francisco", "Paul", "Oscar", "Knox", "Dakota", "Leon", "Graham", "Malachi", "Hendrix", "Adonis", "Jesse", "Zane", "Travis", "Kaden", "Gianni", "Cody", "Rafael", "Remington", "Maxwell", "Andre", "Ali", "Arthur", "Erick", "Ronin", "Kairo", "Andy", "Orion", "Prince", "Avery", "Marco", "Gideon", "Fernando", "Brady", "Jayce", "Jake", "Tobias", "Pedro", "Zander", "Kash", "Israel", "Preston", "Marcus", "Kyrie", "Kenneth", "Jett", "Stephen", "Spencer", "Shane", "Nasir", "Holden", "Malik", "Troy", "Jaylen", "Kobe", "Bryce"
        };
        String[] femaleNames = {
                "Olivia", "Emma", "Charlotte", "Amelia", "Sophia", "Isabella", "Ava", "Mia", "Evelyn", "Luna", "Harper", "Camila", "Gianna", "Elizabeth", "Eleanor", "Ella", "Abigail", "Emily", "Avery", "Scarlett", "Grace", "Chloe", "Lily", "Aria", "Layla", "Aurora", "Nora", "Hazel", "Zoey", "Riley", "Victoria", "Isla", "Penelope", "Lillian", "Addison", "Natalie", "Lucy", "Brooklyn", "Audrey", "Bella", "Claire", "Skylar", "Leah", "Violet", "Savannah", "Stella", "Madison", "Luna", "Nova", "Ellie", "Paisley", "Everly", "Anna", "Caroline", "Maya", "Genesis", "Emilia", "Kennedy", "Samantha", "Aaliyah", "Cora", "Ruby", "Eva", "Serenity", "Autumn", "Adeline", "Hailey", "Giuliana", "Nevaeh", "Alice", "Piper", "Lyla", "Aubrey", "Ivy", "Josephine", "Sarah", "Quinn", "Sadie", "Delilah", "Natalia", "Isabelle", "Mary", "Kinsley", "Brielle", "Allison", "Gabriella", "Hailey", "Madelyn", "Kaylee", "Ariana", "Eliana", "Melanie", "Jade", "Athena", "Taylor", "Hadley", "Raelynn", "Khloe", "Rose", "Molly", "Bailey", "Emery", "Rylee", "Arya", "Peyton", "Adalynn", "Eden", "Brianna", "Jasmine", "Maria", "London", "Lola", "Morgan", "Reagan", "Valentina", "Andrea", "Clara", "Eliza", "Aliyah", "Finley", "Vivian", "Ruth", "Alexandra", "Aspen", "Sophie", "Margot", "Juliette", "Juliana", "Lena", "Blakely", "Camille", "Sloane", "Zoe", "Alina", "June", "Genevieve", "Georgia", "Harmony", "Dakota", "Kimberly", "Catalina", "Remi", "Londyn", "Noelle", "Amara", "Rosalie", "Freya", "Adalyn", "Julianna", "Rebecca", "Mckenzie", "Millie", "Blake", "Lucia", "Anastasia", "Leia", "Ariella", "Ayla", "Faith", "Lia", "Alaina", "Marley", "Kylie", "Amy", "Mckenna", "Liliana", "Charlee", "Tessa", "Alani", "Daisy", "Rachel", "Arabella", "Ximena", "Raegan", "Norah", "Brooke", "Eloise", "Valeria", "Alayna", "Sabrina", "Cecilia", "Angela", "Paris", "Lilah", "Talia", "Heidi", "Catherine", "Journey", "Adelaide", "Gracie", "Winter", "Amaya", "Melissa", "Helen", "Wren", "Harlow", "Kali", "Esme", "Mira", "Selena"
        };

        String returnName = "";

        if (gender.equals("Male"))    {
            returnName = maleNames[getRandom().nextInt(maleNames.length)];
        } else if (gender.equals("Female"))   {
            returnName = femaleNames[getRandom().nextInt(maleNames.length)];
        }
        return returnName;
    }
    private String pickRandomSurname() {
        String[] surnames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin", "Lee", "Perez", "Thompson", "White", "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson", "Walker", "Young", "Allen", "King", "Wright", "Scott", "Torres", "Nguyen", "Hill", "Flores", "Green", "Adams", "Nelson", "Baker", "Hall", "Rivera", "Campbell", "Mitchell", "Carter", "Roberts", "Gomez", "Phillips", "Evans", "Turner", "Diaz", "Parker", "Cruz", "Edwards", "Collins", "Reyes", "Stewart", "Morris", "Morales", "Murphy", "Cook", "Rogers", "Gutierrez", "Ortiz", "Morgan", "Cooper", "Peterson", "Bailey", "Reed", "Kelly", "Howard", "Ramos", "Kim", "Cox", "Ward", "Richardson", "Watson", "Brooks", "Chavez", "Wood", "James", "Bennett", "Gray", "Mendoza", "Ruiz", "Hughes", "Price", "Alvarez", "Castillo", "Sanders", "Patel", "Myers", "Long", "Ross", "Foster", "Jimenez", "Powell", "Jenkins", "Perry", "Russell", "Sullivan", "Bell", "Coleman", "Butler", "Henderson", "Barnes", "Gonzales", "Fisher", "Vasquez", "Simmons", "Romero", "Jordan", "Patterson", "Alexander", "Hamilton", "Graham", "Reynolds", "Griffin", "Wallace", "Moreno", "West", "Cole", "Hayes", "Bryant", "Herrera", "Gibson", "Ellis", "Tran", "Medina", "Aguilar", "Stevens", "Murray", "Ford", "Castro", "Marshall", "Owens", "Harrison", "Fernandez", "Mcdonald", "Woods", "Washington", "Kennedy", "Wells", "Vargas", "Henry", "Chen", "Freeman", "Webb", "Tucker", "Guzman", "Burns", "Crawford", "Olson", "Simpson", "Porter", "Hunter", "Gordon", "Mendez", "Silva", "Shaw", "Snyder", "Mason", "Dixon", "Munoz", "Hunt", "Hicks", "Holmes", "Palmer", "Wagner", "Black", "Robertson", "Boyd", "Rose", "Stone", "Salazar", "Fox", "Warren", "Mills", "Meyer", "Rice", "Schmidt", "Garza", "Daniels", "Ferguson", "Nichols", "Stephens", "Soto", "Weaver", "Ryan", "Gardner", "Payne", "Grant", "Dunn", "Kelley", "Spencer", "Hawkins", "Arnold", "Pierce", "Vega", "Hansen", "Peters"};
        return surnames[getRandom().nextInt(surnames.length)];
    }
    private String pickGender() {
        String[] genders = {"Male", "Female"};
        return genders[this.getRandom().nextInt(genders.length)];
    }
    private int pickTexture() {
        return this.getRandom().nextInt(19) + 1;
    }
    public String getFullName() {
        return getFName() + " " + getSName();
    }
    public String getProfession() {
        return this.entityData.get(DATA_PROFESSION);
    }
    public float getHappiness() {
        return this.entityData.get(DATA_HAPPINESS) * 100;
    }

    public Boolean setRecruitable() {
        return recruitable = true;
    }
    public int getHappinessColor(float happinessF)  {
        if (getHappiness()/100 <= 0.25f){
            return 0xFF0000;
        }

        if (getHappiness()/100 <= 0.5f && getHappiness() / 100 > 0.25f){
            return 0xFFA500;
        }

        if (getHappiness()/100 < 0.75f && getHappiness() / 100 > 0.5f){
            return 0xFFFF00;
        }

        if (getHappiness() / 100 > 0.75f){
            return 0x00FF00;
        } else
        return 0xFFFFFF;
    }
    public int getProfessionColor(String string) {

      return 0xD3D3D3;
    }

    public int getGenderColor(String gender) {

        if (gender.equals("Male")) {
            return 0x0000FF;
        }
        if (gender.equals("Female")) {
            return 0xFF8DA1;
        }
        else return 0xFFFFFF;
    }
    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putString("Name", getFName());
        pCompound.putString("Surname", getSName());
        pCompound.putString("Gender", getGender());
        pCompound.putInt("TextureVariant", getTextureVariant());
        pCompound.putString("Profession", getProfession());
        pCompound.putFloat("Happiness", getHappiness()/100);


    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);

        if (pCompound.contains("Name", 8)) {
            this.entityData.set(DATA_NAME, pCompound.getString("Name"));

        }

        if (pCompound.contains("Surname", 8)) {
            this.entityData.set(DATA_SURNAME, pCompound.getString("Surname"));

        }

        if (pCompound.contains("Gender", 8)) {
            this.entityData.set(DATA_GENDER, pCompound.getString("Gender"));

        }

        if (pCompound.contains("TextureVariant", 3)) {
            this.entityData.set(DATA_VARIANT, pCompound.getInt("TextureVariant"));

        }

        if (pCompound.contains("Profession", 8)) {
            this.entityData.set(DATA_PROFESSION, pCompound.getString("Profession"));
        }

        if (pCompound.contains("Happiness", 5)) { // 5 = float
            this.entityData.set(DATA_HAPPINESS, pCompound.getFloat("Happiness"));
        }

    }

}

