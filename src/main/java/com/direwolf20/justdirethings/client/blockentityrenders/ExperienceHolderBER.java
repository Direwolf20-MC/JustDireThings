package com.direwolf20.justdirethings.client.blockentityrenders;

import com.direwolf20.justdirethings.client.blockentityrenders.baseber.AreaAffectingBER;
import com.direwolf20.justdirethings.common.blockentities.ExperienceHolderBE;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class ExperienceHolderBER extends AreaAffectingBER<ExperienceHolderBE, ExperienceHolderBER.ExperienceHolderRenderState> {
    public static final ItemStack itemStack = new ItemStack(Items.EXPERIENCE_BOTTLE);

    public static class ExperienceHolderRenderState extends AreaAffectingRenderState {
        public Direction facing = Direction.UP;
        public long millis;
        public final ItemStackRenderState item = new ItemStackRenderState();
    }

    private final ItemModelResolver itemModelResolver;

    public ExperienceHolderBER(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public ExperienceHolderRenderState createRenderState() {
        return new ExperienceHolderRenderState();
    }

    @Override
    public void extractRenderState(ExperienceHolderBE blockEntity, ExperienceHolderRenderState state, float partialTicks, Vec3 cameraPosition,
                                    ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.facing = blockEntity.getBlockState().getValue(BlockStateProperties.FACING).getOpposite();
        state.millis = System.currentTimeMillis();
        this.itemModelResolver.updateForTopItem(state.item, itemStack, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
    }

    @Override
    public void submit(ExperienceHolderRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
        Direction direction = state.facing;
        poseStack.pushPose();
        poseStack.translate(0.5f + (direction.getStepX() * 0.3f), 0.5f + (direction.getStepY() * 0.3f), 0.5f + (direction.getStepZ() * 0.3f));
        poseStack.mulPose(Axis.XP.rotationDegrees(direction.getStepZ() * -90));
        poseStack.mulPose(Axis.ZP.rotationDegrees(direction.getStepX() * 90));
        poseStack.mulPose(Axis.XP.rotationDegrees(direction.getStepY() == 1 ? 0 : 180));
        float angle = ((state.millis / 15) % 360);
        poseStack.mulPose(Axis.YP.rotationDegrees(angle));
        poseStack.scale(.15f, .15f, .15f);
        state.item.submit(poseStack, submitNodeCollector, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }
}
