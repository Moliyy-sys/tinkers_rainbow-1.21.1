package org.moliyy.tinkers_rainbow.model;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import org.moliyy.tinkers_rainbow.TinkersRainbow;

public class MuffinModel<T extends MuffinEntity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "muffin"), "main");

    private final ModelPart Root;

    private final ModelPart body;

    private final ModelPart head;

    private final ModelPart ear_left;

    private final ModelPart ear_right;

    private final ModelPart eye_left;

    private final ModelPart eye_right;

    public MuffinModel(ModelPart root) {
        this.Root = root.getChild("Root");
        this.body = this.Root.getChild("body");
        this.head = this.body.getChild("head");
        this.ear_left = this.head.getChild("ear_left");
        this.ear_right = this.head.getChild("ear_right");
        this.eye_left = this.head.getChild("eye_left");
        this.eye_right = this.head.getChild("eye_right");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Root = partdefinition.addOrReplaceChild("Root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = Root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -2.0F, -6.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 15).addBox(-6.0F, -4.0F, -6.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 30).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -6.0F, 0.0F));

        PartDefinition ear_left = head.addOrReplaceChild("ear_left", CubeListBuilder.create().texOffs(0, 45).addBox(-0.5F, -1.5F, -1.5F, 2.5F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(26, 45).addBox(-0.5F, -2.5F, -1.0F, 2.0F, 1.5F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(49, 4).addBox(0.0F, -4.0F, -0.5F, 1.0F, 1.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 0.0F, 0.0F));

        PartDefinition ear_right = head.addOrReplaceChild("ear_right", CubeListBuilder.create().texOffs(13, 45).addBox(-2.0F, -1.5F, -1.5F, 2.5F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(35, 45).addBox(-1.5F, -2.5F, -1.0F, 2.0F, 1.5F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(49, 8).addBox(-1.0F, -4.0F, -0.5F, 1.0F, 1.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 0.0F, 0.0F));

        PartDefinition eye_left = head.addOrReplaceChild("eye_left", CubeListBuilder.create().texOffs(44, 45).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 1.5F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(49, 12).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(49, 15).addBox(-0.5F, 0.5F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(49, 18).addBox(1.5F, -0.75F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(49, 21).addBox(-2.5F, -0.75F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 3.0F, -6.0F));

        PartDefinition eye_right = head.addOrReplaceChild("eye_right", CubeListBuilder.create().texOffs(49, 0).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 1.5F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(49, 24).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(49, 27).addBox(-0.5F, 0.5F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(49, 30).addBox(1.5F, -0.75F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(49, 33).addBox(-2.5F, -0.75F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 3.0F, -6.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.Root.getAllParts().forEach(ModelPart::resetPose);

        if (entity.chargeAnimationState.isStarted()) {
            this.animate(entity.chargeAnimationState, MuffinAnimations.charge, ageInTicks);
        } else {
            this.animate(entity.idleAnimationState, MuffinAnimations.idle, ageInTicks);
        }
    }

   @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
       Root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
   }

    @Override
    public ModelPart root() {
        return Root;
    }
}
