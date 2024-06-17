package com.direwolf20.justdirethings.client.entitymodels;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.entities.PortalProjectile;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class PortalProjectileModel extends HierarchicalModel<PortalProjectile> {

    public static ModelLayerLocation Portal_Projectile_Layer = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "portal_projectile"), "body");
    private static final String MAIN = "main";
    private final ModelPart root;
    private final ModelPart main;

    public PortalProjectileModel(ModelPart pRoot) {
        this.root = pRoot;
        this.main = pRoot.getChild("main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild(
                "main",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.0F, -4.0F, -1.0F, 8.0F, 8.0F, 2.0F)
                        .texOffs(0, 10)
                        .addBox(-1.0F, -4.0F, -4.0F, 2.0F, 8.0F, 8.0F)
                        .texOffs(20, 0)
                        .addBox(-4.0F, -1.0F, -4.0F, 8.0F, 2.0F, 8.0F),
                PartPose.ZERO
        );
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    /**
     * Sets this entity's model rotation angles
     */
    @Override
    public void setupAnim(PortalProjectile pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.main.yRot = pNetHeadYaw * (float) (Math.PI / 180.0);
        this.main.xRot = pHeadPitch * (float) (Math.PI / 180.0);
    }
}
