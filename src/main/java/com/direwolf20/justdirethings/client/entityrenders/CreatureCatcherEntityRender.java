package com.direwolf20.justdirethings.client.entityrenders;

import com.direwolf20.justdirethings.common.entities.CreatureCatcherEntity;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.ThrownItemRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

public class CreatureCatcherEntityRender extends ThrownItemRenderer<CreatureCatcherEntity> {
    private final ItemModelResolver itemModelResolver;
    private final EntityRenderDispatcher dispatcher;

    public CreatureCatcherEntityRender(EntityRendererProvider.Context context) {
        super(context);
        this.itemModelResolver = context.getItemModelResolver();
        this.dispatcher = context.getEntityRenderDispatcher();
    }

    public static final class PreparedMob {
        public final LivingEntityRenderState state;
        public final EntityModel<LivingEntityRenderState> model;
        public final Identifier texture;
        public final LivingEntityRenderer<Mob, LivingEntityRenderState, EntityModel<LivingEntityRenderState>> renderer;

        public PreparedMob(LivingEntityRenderState state, EntityModel<LivingEntityRenderState> model, Identifier texture,
                           LivingEntityRenderer<Mob, LivingEntityRenderState, EntityModel<LivingEntityRenderState>> renderer) {
            this.state = state;
            this.model = model;
            this.texture = texture;
            this.renderer = renderer;
        }
    }

    public static class CreatureCatcherRenderState extends ThrownItemRenderState {
        public int shrinkingTime;
        public int renderTick;
        public int animationTicks;
        public Vector3fc mobPosition = new Vector3f();
        public double entityX;
        public double entityY;
        public double entityZ;
        public double entityYMid;
        public boolean capturing;
        public double cameraDistSqr;
        public int tickCount;
        public float partialTick;
        public @Nullable PreparedMob preparedMob;
        public boolean showEmptyCatcherItem;
        public final ItemStackRenderState emptyCatcherItem = new ItemStackRenderState();
    }

    @Override
    public CreatureCatcherRenderState createRenderState() {
        return new CreatureCatcherRenderState();
    }

    @Override
    public void extractRenderState(CreatureCatcherEntity entity, ThrownItemRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        if (!(state instanceof CreatureCatcherRenderState s)) return;

        s.shrinkingTime = entity.shrinkingTime();
        s.renderTick = entity.renderTick;
        s.animationTicks = entity.getAnimationTicks();
        s.mobPosition = new Vector3f(entity.getMobPosition());
        s.entityX = entity.getX();
        s.entityY = entity.getY();
        s.entityZ = entity.getZ();
        s.entityYMid = entity.getY() + entity.getBoundingBox().getYsize() / 2.0;
        s.capturing = entity.isCapturing();
        s.tickCount = entity.tickCount;
        s.partialTick = partialTicks;

        var camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        if (camera != null && camera.entity() != null) {
            s.cameraDistSqr = camera.entity().distanceToSqr(entity);
        } else {
            s.cameraDistSqr = Double.POSITIVE_INFINITY;
        }

        s.showEmptyCatcherItem = !s.capturing && s.renderTick > 0;
        if (s.showEmptyCatcherItem) {
            ItemStack emptyCatcher = new ItemStack(JDTRegistration.CreatureCatcher.get());
            this.itemModelResolver.updateForNonLiving(s.emptyCatcherItem, emptyCatcher, ItemDisplayContext.GROUND, entity);
        }

        ItemStack mobStack = s.capturing ? entity.getReturnStack() : entity.getItem();
        Mob mob = CreatureCatcherEntity.getEntityFromItemStack(mobStack, entity.level());

        if (mob != null) {
            mob.snapTo(0, 0, 0, 0, 0);
            mob.xOld = 0;
            mob.yOld = 0;
            mob.zOld = 0;
            mob.setYRot(0);
            mob.yRotO = 0;
            mob.setXRot(0);
            mob.xRotO = 0;
            mob.yBodyRot = entity.getBodyRot();
            mob.yBodyRotO = entity.getBodyRot();
            mob.yHeadRot = entity.getHeadRot();
            mob.yHeadRotO = entity.getHeadRot();
            s.preparedMob = prepareMob(mob, partialTicks);
        } else {
            s.preparedMob = null;
        }
    }

    @SuppressWarnings("unchecked")
    private @Nullable PreparedMob prepareMob(Mob mob, float partialTicks) {
        EntityRenderer<?, ?> rawRenderer = this.dispatcher.getRenderer(mob);
        if (!(rawRenderer instanceof LivingEntityRenderer<?, ?, ?> livingRenderer)) return null;
        var state = ((EntityRenderer<Mob, LivingEntityRenderState>) rawRenderer).createRenderState(mob, partialTicks);
        var renderer = (LivingEntityRenderer<Mob, LivingEntityRenderState, EntityModel<LivingEntityRenderState>>) livingRenderer;
        EntityModel<LivingEntityRenderState> model = renderer.getModel();
        Identifier texture = renderer.getTextureLocation(state);
        if (texture == null) return null;
        return new PreparedMob(state, model, texture, renderer);
    }

    @Override
    public void submit(ThrownItemRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (!(state instanceof CreatureCatcherRenderState s)) {
            super.submit(state, poseStack, submitNodeCollector, camera);
            return;
        }
        if (s.tickCount < 2 && s.cameraDistSqr < 12.25) return;

        poseStack.pushPose();
        poseStack.mulPose(camera.orientation);
        float multiplier = (10f * s.shrinkingTime) % 360f;
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F + multiplier));
        ItemStackRenderState itemToRender = s.showEmptyCatcherItem ? s.emptyCatcherItem : s.item;
        itemToRender.submit(poseStack, submitNodeCollector, s.lightCoords, OverlayTexture.NO_OVERLAY, s.outlineColor);
        poseStack.popPose();

        if (s.preparedMob == null) return;
        PreparedMob mob = s.preparedMob;

        Vector3f entityPos = new Vector3f((float) s.entityX, (float) s.entityYMid, (float) s.entityZ);
        Vector3f originalPos;
        float fraction;
        int current = Math.min(s.renderTick, s.animationTicks);
        int last = s.renderTick == 0 ? 0 : s.renderTick - 1;
        float interpolated = Mth.lerp(s.partialTick, last, current);

        if (s.capturing) {
            fraction = s.animationTicks <= 0 ? 0f : (s.animationTicks - interpolated) / (float) s.animationTicks;
            originalPos = new Vector3f(s.mobPosition);
        } else {
            fraction = s.animationTicks <= 0 ? 1f : interpolated / (float) s.animationTicks;
            originalPos = new Vector3f(entityPos);
        }
        fraction = Mth.cos(fraction * Mth.PI) * -0.5f + 0.5f;

        Vector3f interpPos = s.capturing
                ? new Vector3f(entityPos).lerp(originalPos, fraction)
                : new Vector3f(originalPos).lerp(entityPos, fraction);
        float shrinkScale = Mth.clampedLerp(1.0f, 0.2f, fraction);

        poseStack.pushPose();
        poseStack.translate(interpPos.x() - s.entityX, interpPos.y() - s.entityY, interpPos.z() - s.entityZ);
        poseStack.scale(shrinkScale, shrinkScale, shrinkScale);

        float mobScale = mob.state.scale;
        poseStack.scale(mobScale, mobScale, mobScale);
        if (!mob.state.hasPose(Pose.SLEEPING)) {
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - mob.state.bodyRot));
        }
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0F, -1.501F, 0.0F);

        RenderType renderType = RenderTypes.entityCutout(mob.texture);
        int overlayCoords = LivingEntityRenderer.getOverlayCoords(mob.state, 0.0F);

        submitNodeCollector.submitModel(mob.model, mob.state, poseStack, renderType,
                s.lightCoords, overlayCoords, -1, null, 0, null);

        // Layers (e.g. sheep wool, armor, eyes) need their own submit pass after the base model.
        if (!mob.renderer.layers.isEmpty()) {
            mob.model.setupAnim(mob.state);
            for (RenderLayer<LivingEntityRenderState, EntityModel<LivingEntityRenderState>> layer : mob.renderer.layers) {
                layer.submit(poseStack, submitNodeCollector, s.lightCoords, mob.state, mob.state.yRot, mob.state.xRot);
            }
        }

        poseStack.popPose();
    }
}
