package net.nic.npc.entity.Renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import net.nic.npc.NpcMain;
import net.nic.npc.entity.EntityNpc;

public class NpcRenderer extends MobRenderer<EntityNpc, HumanoidModel<EntityNpc>> {

    public NpcRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM)), 0.5f);
        this.addLayer(new HumanoidArmorLayer<>(this,
                new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM_INNER_ARMOR)),
                new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM_OUTER_ARMOR)),
                context.getModelManager()));
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityNpc entity) {
        int textureValue = entity.getTextureVariant();
        String gender = entity.getGender();

        ResourceLocation resourceLocation;
        if ("Female".equals(gender)) {
            resourceLocation = ResourceLocation.fromNamespaceAndPath(NpcMain.MOD_ID, "textures/entity/female/npc_citizen_" + textureValue + ".png");
        } else {
            resourceLocation = ResourceLocation.fromNamespaceAndPath(NpcMain.MOD_ID, "textures/entity/male/npc_citizen_" + textureValue + ".png");
        }

        return resourceLocation;
    }

    @Override
    protected boolean shouldShowName(EntityNpc entity) {
        // Only show the name if the player is looking at the entity
        if (this.entityRenderDispatcher.crosshairPickEntity == entity) {
            return true;
        }
        return false;
    }

    @Override
    protected void renderNameTag(EntityNpc entity, Component displayName, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, float partialTicks) {
        if (this.shouldShowName(entity)) {
            super.renderNameTag(entity, Component.literal(entity.getFullName()), poseStack, bufferSource, packedLight, partialTicks);
        }
    }
}
