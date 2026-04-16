package com.direwolf20.justdirethings.client.entityrenders;

import com.direwolf20.justdirethings.common.entities.DecoyEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;

import java.util.UUID;

// TODO(port, stage-18): reimplement the decoy's player-lookalike rendering.
// 1.21.1 extended LivingEntityRenderer<DecoyEntity, PlayerModel<DecoyEntity>> and attached
// HumanoidArmorLayer / ArrowLayer / CustomHeadLayer / ElytraLayer with the owner's skin texture.
// In 26.1:
//   - LivingEntityRenderer is now LivingEntityRenderer<T, S extends LivingEntityRenderState, M extends EntityModel<? super S>>
//   - PlayerModel is parameterized by AvatarRenderState, not a generic LivingEntityRenderState.
//   - Armor / arrow / head / elytra layers all accept the generified RenderLayer<S, M> API.
//   - Skin lookup goes through Minecraft.getInstance().getSkinManager().getInsecureSkin(profile).texture() same as before.
// The clean path is probably to reuse AvatarRenderer / AvatarRenderState directly and swap the skin, instead of
// rebuilding a bespoke renderer. Deferred until we have the rest of the client compiling.
public class DecoyEntityRender extends EntityRenderer<DecoyEntity, DecoyEntityRender.DecoyRenderState> {

    public static final UUID defaultPlayerUUID = UUID.fromString("0192723f-b3dc-495a-959f-52c53fa63bff");

    public static class DecoyRenderState extends LivingEntityRenderState {
    }

    public DecoyEntityRender(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public DecoyRenderState createRenderState() {
        return new DecoyRenderState();
    }

    @Override
    public void extractRenderState(DecoyEntity entity, DecoyRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
    }

    @Override
    public void submit(DecoyRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
        // TODO(port, stage-18): actually submit a player-style model with the owner's skin.
    }
}
