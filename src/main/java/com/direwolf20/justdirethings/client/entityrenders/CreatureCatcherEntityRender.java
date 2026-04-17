package com.direwolf20.justdirethings.client.entityrenders;

import com.direwolf20.justdirethings.common.entities.CreatureCatcherEntity;
import com.direwolf20.justdirethings.setup.Registration;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.ThrownItemRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
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

    public static class CreatureCatcherRenderState extends ThrownItemRenderState {
        public int shrinkingTime;
        public int renderTick;
        public int animationTicks;
        public Vector3fc mobPosition = new Vector3f();
        public float bodyRot;
        public float headRot;
        public double entityX;
        public double entityY;
        public double entityZ;
        public double entityYMid; // y + boundingBox.getYsize() / 2
        public boolean capturing;
        public double cameraDistSqr;
        public int tickCount;
        public float partialTick;
        public @Nullable EntityRenderState capturedMobState;
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
        s.bodyRot = entity.getBodyRot();
        s.headRot = entity.getHeadRot();
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

        // Replicate the "returning" branch: when NOT capturing and renderTick > 0,
        // the shell item is drawn as an empty CreatureCatcher (captured mob has been released).
        s.showEmptyCatcherItem = !s.capturing && s.renderTick > 0;
        if (s.showEmptyCatcherItem) {
            ItemStack emptyCatcher = new ItemStack(Registration.CreatureCatcher.get());
            this.itemModelResolver.updateForNonLiving(s.emptyCatcherItem, emptyCatcher, ItemDisplayContext.GROUND, entity);
        }

        // Construct transient mob from the stack, apply body/head rotation, extract its render state.
        ItemStack mobStack = s.capturing ? entity.getItem() : entity.getReturnStack();
        Mob mob = CreatureCatcherEntity.getEntityFromItemStack(mobStack, entity.level());
        if (mob != null) {
            mob.yBodyRot = s.bodyRot;
            mob.yBodyRotO = s.bodyRot;
            mob.yHeadRot = s.headRot;
            mob.yHeadRotO = s.headRot;
            try {
                s.capturedMobState = this.dispatcher.extractEntity(mob, partialTicks);
            } catch (Throwable ignored) {
                s.capturedMobState = null;
            }
        } else {
            s.capturedMobState = null;
        }
    }

    @Override
    public void submit(ThrownItemRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (!(state instanceof CreatureCatcherRenderState s)) {
            super.submit(state, poseStack, submitNodeCollector, camera);
            return;
        }
        // Match the 1.21.1 early-exit: render when tickCount >= 2 OR camera is farther than 3.5 blocks.
        if (s.tickCount < 2 && s.cameraDistSqr < 12.25) return;

        // Billboard item, spinning around Y.
        poseStack.pushPose();
        poseStack.mulPose(camera.orientation);
        float multiplier = (10f * s.shrinkingTime) % 360f;
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F + multiplier));
        ItemStackRenderState itemToRender = s.showEmptyCatcherItem ? s.emptyCatcherItem : s.item;
        itemToRender.submit(poseStack, submitNodeCollector, s.lightCoords, OverlayTexture.NO_OVERLAY, s.outlineColor);
        poseStack.popPose();

        // Captured mob — interpolated position + shrinking/growing scale.
        if (s.capturedMobState != null) {
            Vector3f entityPos = new Vector3f((float) s.entityX, (float) s.entityYMid, (float) s.entityZ);
            Vector3f originalPos;
            float fraction;
            int current = Math.min(s.renderTick, s.animationTicks);
            int last = s.renderTick == 0 ? 0 : s.renderTick - 1;
            float interpolated = Mth.lerp(s.partialTick, last, current);

            if (s.capturing) {
                fraction = s.animationTicks <= 0 ? 1f : interpolated / (float) s.animationTicks;
                originalPos = new Vector3f(s.mobPosition);
            } else {
                fraction = s.animationTicks <= 0 ? 0f : (s.animationTicks - interpolated) / (float) s.animationTicks;
                originalPos = new Vector3f(entityPos);
            }
            // Cosine flatten near 0/1 so the shrink isn't linear.
            fraction = Mth.cos(fraction * Mth.PI) * -0.5f + 0.5f;

            Vector3f interpPos = originalPos.lerp(entityPos, fraction);
            poseStack.pushPose();
            poseStack.translate(interpPos.x() - s.entityX, interpPos.y() - s.entityY, interpPos.z() - s.entityZ);
            float scale = Mth.clampedLerp(1.0f, 0.2f, fraction);
            poseStack.scale(scale, scale, scale);
            // Render the mob at (0,0,0) since we've already translated to where it should be relative to the entity.
            this.dispatcher.submit(s.capturedMobState, camera, 0.0, 0.0, 0.0, poseStack, submitNodeCollector);
            poseStack.popPose();
        }

        // Skip the ThrownItemRenderer super.submit — we already drew the item manually above with the
        // rotation/billboard applied, and super would draw it again at the base transform.
    }
}
