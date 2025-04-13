package net.nic.npc.client.renderer;

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
import net.nic.npc.entity.EntityNpcCitizen;

public class NpcCitizenRenderer extends MobRenderer<EntityNpcCitizen, HumanoidModel<EntityNpcCitizen>> {

    public NpcCitizenRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
        this.addLayer(new HumanoidArmorLayer<>(this,
                new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
                new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),
                context.getModelManager()));
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityNpcCitizen entity) {
        int textureValue = entity.getTextureVariant();
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(NpcMain.MOD_ID, "textures/entity/npc_citizen_" + textureValue + ".png");
        return resourceLocation;
    }

    @Override
    protected boolean shouldShowName(EntityNpcCitizen entity) {
        // Only show the name if the player is looking at the entity
        if (this.entityRenderDispatcher.crosshairPickEntity == entity) {
            return true;
        }
        return false;
    }

    @Override
    protected void renderNameTag(EntityNpcCitizen entity, Component displayName, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, float partialTicks) {
        if (this.shouldShowName(entity)) {
            super.renderNameTag(entity, Component.literal(entity.getFullName()), poseStack, bufferSource, packedLight, partialTicks);
        }
    }
}
