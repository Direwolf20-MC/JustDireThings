package com.direwolf20.justdirethings.client.itemcustomrenders;

import com.direwolf20.justdirethings.common.entities.CreatureCatcherEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class CreatureCatcherSpecialRenderer implements SpecialModelRenderer<CreatureCatcherSpecialRenderer.PreparedMob> {

    public static final class PreparedMob {
        public final LivingEntityRenderState state;
        public final EntityModel<LivingEntityRenderState> model;
        public final Identifier texture;
        public final LivingEntityRenderer<Mob, LivingEntityRenderState, EntityModel<LivingEntityRenderState>> renderer;
        public final float bbWidth;
        public final float bbHeight;

        PreparedMob(LivingEntityRenderState state,
                    EntityModel<LivingEntityRenderState> model,
                    Identifier texture,
                    LivingEntityRenderer<Mob, LivingEntityRenderState, EntityModel<LivingEntityRenderState>> renderer,
                    float bbWidth, float bbHeight) {
            this.state = state;
            this.model = model;
            this.texture = texture;
            this.renderer = renderer;
            this.bbWidth = bbWidth;
            this.bbHeight = bbHeight;
        }
    }

    @Override
    public @Nullable PreparedMob extractArgument(ItemStack stack) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return null;
        Mob mob = CreatureCatcherEntity.getEntityFromItemStack(stack, mc.level);
        if (mob == null) return null;

        mob.snapTo(0, 0, 0, 0, 0);
        mob.xOld = 0;
        mob.yOld = 0;
        mob.zOld = 0;
        mob.setYRot(0);
        mob.yRotO = 0;
        mob.setXRot(0);
        mob.xRotO = 0;
        mob.yBodyRot = 0;
        mob.yBodyRotO = 0;
        mob.yHeadRot = 0;
        mob.yHeadRotO = 0;

        return prepareMob(mc.getEntityRenderDispatcher(), mob);
    }

    @SuppressWarnings("unchecked")
    private static @Nullable PreparedMob prepareMob(EntityRenderDispatcher dispatcher, Mob mob) {
        EntityRenderer<?, ?> rawRenderer = dispatcher.getRenderer(mob);
        if (!(rawRenderer instanceof LivingEntityRenderer<?, ?, ?> livingRenderer)) return null;
        LivingEntityRenderState state = ((EntityRenderer<Mob, LivingEntityRenderState>) rawRenderer).createRenderState(mob, 0.0F);
        LivingEntityRenderer<Mob, LivingEntityRenderState, EntityModel<LivingEntityRenderState>> renderer =
                (LivingEntityRenderer<Mob, LivingEntityRenderState, EntityModel<LivingEntityRenderState>>) livingRenderer;
        EntityModel<LivingEntityRenderState> model = renderer.getModel();
        Identifier texture = renderer.getTextureLocation(state);
        if (texture == null) return null;
        AABB box = mob.getBoundingBox();
        float width = (float) Math.max(box.getXsize(), box.getZsize());
        float height = (float) box.getYsize();
        return new PreparedMob(state, model, texture, renderer, width, height);
    }

    @Override
    public void submit(@Nullable PreparedMob mob,
                       PoseStack poseStack,
                       SubmitNodeCollector submitNodeCollector,
                       int lightCoords,
                       int overlayCoords,
                       boolean hasFoil,
                       int outlineColor) {
        if (mob == null) return;

        poseStack.pushPose();
        // The base item model's display transforms are already applied by the LayerRenderState; we're
        // rendering inside a 1x1x1 item cube in local item-model space. Center on the cube, then scale
        // the mob to fit while keeping a consistent visual size regardless of mob dimensions.
        poseStack.translate(0.5F, 0.125F, 0.5F);

        float maxDim = Math.max(mob.bbWidth, mob.bbHeight);
        float scale = maxDim <= 0.0F ? 0.5F : 0.5F / maxDim;
        poseStack.scale(scale, scale, scale);

        // Slight forward/upward rotation to show the mob's front and a bit of its top — matches the 1.21.1 look.
        poseStack.mulPose(Axis.YP.rotationDegrees(-30.0F));

        float modelScale = mob.state.scale;
        poseStack.scale(modelScale, modelScale, modelScale);
        if (!mob.state.hasPose(Pose.SLEEPING)) {
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - mob.state.bodyRot));
        }
        // Entity models are built y-down; flip into item space.
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0F, -1.501F, 0.0F);

        RenderType renderType = RenderTypes.entityCutout(mob.texture);
        int overlay = LivingEntityRenderer.getOverlayCoords(mob.state, 0.0F);
        submitNodeCollector.submitModel(mob.model, mob.state, poseStack, renderType,
                lightCoords, overlay, -1, null, 0, null);

        if (!mob.renderer.layers.isEmpty()) {
            mob.model.setupAnim(mob.state);
            for (RenderLayer<LivingEntityRenderState, EntityModel<LivingEntityRenderState>> layer : mob.renderer.layers) {
                layer.submit(poseStack, submitNodeCollector, lightCoords, mob.state, mob.state.yRot, mob.state.xRot);
            }
        }

        poseStack.popPose();
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        // The base creaturecatcher model provides the actual extents; the captured mob renders on top of it.
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked<PreparedMob> {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(Unbaked::new);

        @Override
        public SpecialModelRenderer<PreparedMob> bake(BakingContext context) {
            return new CreatureCatcherSpecialRenderer();
        }

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
