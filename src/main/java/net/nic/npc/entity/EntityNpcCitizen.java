package net.nic.npc.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class EntityNpcCitizen extends PathfinderMob {

    public String name;
    public String surname;
    public String gender;
    public int textureVariant;

    public EntityNpcCitizen(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        if (this.name == null || this.name.isEmpty()) {
            this.gender = pickGender();
            this.name = pickRandomName(gender);
            this.surname = pickRandomSurname();
            this.textureVariant = pickTexture();
        }
        this.setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.MAX_HEALTH, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 16.0D);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pSpawnType, @Nullable SpawnGroupData pSpawnGroupData) {
        gender = pickGender();
        name = pickRandomName(gender);
        surname = pickRandomSurname();
        textureVariant = pickTexture();
        return super.finalizeSpawn(pLevel, pDifficulty, pSpawnType, pSpawnGroupData);
    }

    public String getFName() {
        return name;
    }

    public String getSName() {
        return surname;
    }

    public String getGender() {
        return gender;
    }

    public int getTextureVariant() {
        return textureVariant;
    }

    private String pickRandomName(String gender) {
        String[] names = {"1", "2", "3", "4"};
        return names[getRandom().nextInt(names.length)];
    }

    private String pickRandomSurname() {
        String[] surnames = {"s1", "s2", "s3", "s4"};
        return surnames[getRandom().nextInt(surnames.length)];
    }

    private String pickGender() {
        String[] genders = {"Male", "Female"};
        return genders[this.getRandom().nextInt(genders.length)];
    }

    private int pickTexture() {
        return this.getRandom().nextInt(5) + 1;
    }

    public String getFullName() {
        return getFName() + " " + getSName() + " " + getGender();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("TextureVariant", getTextureVariant());
        pCompound.putString("Gender", getGender());
        pCompound.putString("Name", getFName());
        pCompound.putString("Surname", getSName());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);

        if (pCompound.contains("TextureVariant", 3)) {
            this.textureVariant = pCompound.getInt("TextureVariant");
        }

        if (pCompound.contains("Gender", 8)) {
            this.gender = pCompound.getString("Gender");
        }

        if (pCompound.contains("Name", 8)) {
            this.name = pCompound.getString("Name");
        }

        if (pCompound.contains("Surname", 8)) {
            this.surname = pCompound.getString("Surname");
        }

        if (this.name == null || this.name.isEmpty()) {
            this.gender = pickGender();
            this.name = pickRandomName(gender);
            this.surname = pickRandomSurname();
            this.textureVariant = pickTexture();
        }
    }
}
