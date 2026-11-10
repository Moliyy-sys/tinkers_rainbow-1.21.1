package org.moliyy.tinkers_rainbow.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.moliyy.tinkers_rainbow.TinkersRainbow;
import org.moliyy.tinkers_rainbow.model.MuffinEntity;
import org.moliyy.tinkers_rainbow.model.MuffinModel;

public class MuffinRenderer extends MobRenderer<MuffinEntity, MuffinModel<MuffinEntity>> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID,"textures/entity/muffin.png");

    public MuffinRenderer(EntityRendererProvider.Context context) {
        super(context, new MuffinModel<>(context.bakeLayer(MuffinModel.LAYER_LOCATION)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(MuffinEntity entity) {
        return TEXTURE;
    }
}
