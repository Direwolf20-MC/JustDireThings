package com.direwolf20.justdirethings.client.entityrenders;

import com.direwolf20.justdirethings.common.entities.CreatureCatcherEntity;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.*;
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
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

public class CreatureCatcherEntityRender extends ThrownItemRenderer<CreatureCatcherEntity> {
    // HotSwap-friendly: non-final so IDE "Evaluate Expression" can toggle at runtime without restart.
    public static boolean DEBUG_ALWAYS_COW = false;
    public static boolean DEBUG_VERBOSE = false;

    private final ItemModelResolver itemModelResolver;
    private final EntityRenderDispatcher dispatcher;

    public CreatureCatcherEntityRender(EntityRendererProvider.Context context) {
        super(context);
        this.itemModelResolver = context.getItemModelResolver();
        this.dispatcher = context.getEntityRenderDispatcher();
        if (DEBUG_VERBOSE) System.out.println("[CCR] constructor called");
    }

    public static final class PreparedMob {
        public final LivingEntityRenderState state;
        public final EntityModel<LivingEntityRenderState> model;
        public final Identifier texture;

        public PreparedMob(LivingEntityRenderState state, EntityModel<LivingEntityRenderState> model, Identifier texture) {
            this.state = state;
            this.model = model;
            this.texture = texture;
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
        if (!(state instanceof CreatureCatcherRenderState s)) {
            if (DEBUG_VERBOSE)
                System.out.println("[CCR] extract: state is NOT CreatureCatcherRenderState, class=" + state.getClass().getName());
            return;
        }

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

        Mob mob;
        if (DEBUG_ALWAYS_COW) {
            mob = EntityType.COW.create(entity.level(), EntitySpawnReason.LOAD);
            if (DEBUG_VERBOSE) System.out.println("[CCR] extract: DEBUG_ALWAYS_COW=true, cow=" + (mob != null));
        } else {
            // Capturing: mob-data lives in getReturnStack (built after the hit). Releasing: mob-data is on getItem.
            ItemStack mobStack = s.capturing ? entity.getReturnStack() : entity.getItem();
            if (DEBUG_VERBOSE) System.out.println("[CCR] extract: capturing=" + s.capturing
                    + " getItem=" + entity.getItem().getItem() + "(count=" + entity.getItem().getCount() + ")"
                    + " getReturnStack=" + entity.getReturnStack().getItem() + "(count=" + entity.getReturnStack().getCount() + ")"
                    + " tick=" + s.tickCount);
            mob = CreatureCatcherEntity.getEntityFromItemStack(mobStack, entity.level());
        }

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
            if (DEBUG_VERBOSE) System.out.println("[CCR] extract: preparedMob=" + (s.preparedMob != null)
                    + (s.preparedMob != null
                    ? " tex=" + s.preparedMob.texture + " scale=" + s.preparedMob.state.scale
                    : ""));
        } else {
            s.preparedMob = null;
        }
    }

    @SuppressWarnings("unchecked")
    private @Nullable PreparedMob prepareMob(Mob mob, float partialTicks) {
        EntityRenderer<?, ?> rawRenderer = this.dispatcher.getRenderer(mob);
        if (DEBUG_VERBOSE)
            System.out.println("[CCR] prepareMob: mob=" + mob.getType() + " rendererClass=" + (rawRenderer == null ? "null" : rawRenderer.getClass().getName()));
        if (!(rawRenderer instanceof LivingEntityRenderer<?, ?, ?> livingRenderer)) {
            if (DEBUG_VERBOSE) System.out.println("[CCR] prepareMob: renderer is NOT a LivingEntityRenderer, aborting");
            return null;
        }
        try {
            var state = ((EntityRenderer<Mob, LivingEntityRenderState>) rawRenderer).createRenderState(mob, partialTicks);
            var renderer = (LivingEntityRenderer<Mob, LivingEntityRenderState, EntityModel<LivingEntityRenderState>>) livingRenderer;
            EntityModel<LivingEntityRenderState> model = renderer.getModel();
            Identifier texture = renderer.getTextureLocation(state);
            if (DEBUG_VERBOSE)
                System.out.println("[CCR] prepareMob: state.scale=" + state.scale + " texture=" + texture + " model=" + (model == null ? "null" : model.getClass().getName()));
            if (texture == null) return null;
            return new PreparedMob(state, model, texture);
        } catch (Throwable t) {
            if (DEBUG_VERBOSE)
                System.out.println("[CCR] prepareMob: EXCEPTION " + t.getClass().getName() + ": " + t.getMessage());
            t.printStackTrace(System.out);
            return null;
        }
    }

    @Override
    public void submit(ThrownItemRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (!(state instanceof CreatureCatcherRenderState s)) {
            if (DEBUG_VERBOSE) System.out.println("[CCR] submit: state is NOT CreatureCatcherRenderState");
            super.submit(state, poseStack, submitNodeCollector, camera);
            return;
        }
        if (s.tickCount < 2 && s.cameraDistSqr < 12.25) {
            if (DEBUG_VERBOSE)
                System.out.println("[CCR] submit: early-exit tickCount=" + s.tickCount + " distSq=" + s.cameraDistSqr);
            return;
        }

        poseStack.pushPose();
        poseStack.mulPose(camera.orientation);
        float multiplier = (10f * s.shrinkingTime) % 360f;
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F + multiplier));
        ItemStackRenderState itemToRender = s.showEmptyCatcherItem ? s.emptyCatcherItem : s.item;
        itemToRender.submit(poseStack, submitNodeCollector, s.lightCoords, OverlayTexture.NO_OVERLAY, s.outlineColor);
        poseStack.popPose();

        if (DEBUG_VERBOSE)
            System.out.println("[CCR] submit: preparedMob=" + (s.preparedMob != null) + " tick=" + s.tickCount + " shrink=" + s.shrinkingTime + " renderTick=" + s.renderTick + " animTicks=" + s.animationTicks + " partial=" + s.partialTick);

        if (s.preparedMob == null) return;
        PreparedMob mob = s.preparedMob;

        if (DEBUG_ALWAYS_COW) {
            // Static cow at the catcher's position, no shrink, no extra transforms beyond the standard
            // LivingEntityRenderer chain. If this doesn't appear, the submitModel path itself is broken.
            poseStack.pushPose();
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
            poseStack.popPose();
            if (DEBUG_VERBOSE) System.out.println("[CCR] submit: DEBUG_ALWAYS_COW static cow submitted");
            return;
        }

        // Match main: target is catcher's Y-midpoint; mob's feet land on that at full shrink.
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

        poseStack.popPose();
    }
}
