package com.direwolf20.justdirethings.client.events;

import com.direwolf20.justdirethings.client.renderactions.MiscRenders;
import com.direwolf20.justdirethings.client.renderactions.ThingFinder;
import com.direwolf20.justdirethings.client.renderers.OurRenderTypes;
import com.direwolf20.justdirethings.client.renderers.RenderHelpers;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool;
import com.direwolf20.justdirethings.util.NBTHelpers;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

import java.awt.*;

public class RenderLevelLast {
    @SubscribeEvent
    static void renderWorldLastEvent(RenderLevelStageEvent evt) {
        if (evt.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }
        Player player = Minecraft.getInstance().player;
        if (player == null)
            return;

        ItemStack heldItemMain = player.getMainHandItem();
        ItemStack heldItemOff = player.getOffhandItem();

        if (heldItemMain.getItem() instanceof ToggleableTool toggleableTool) {
            ThingFinder.render(evt, player, heldItemMain);
            if (toggleableTool.canUseAbilityAndDurability(heldItemMain, Ability.VOIDSHIFT) && ToggleableTool.getCustomSetting(heldItemMain, Ability.VOIDSHIFT.getName()) == 0)
                MiscRenders.renderTransparentPlayer(evt, player, heldItemMain);
            if (toggleableTool.canUseAbility(heldItemMain, Ability.DROPTELEPORT) && ToggleableTool.getCustomSetting(heldItemMain, Ability.DROPTELEPORT.getName()) == 0) {
                NBTHelpers.BoundInventory boundInventory = ToggleableTool.getBoundInventory(heldItemMain);
                if (boundInventory != null && player.level().dimension().equals(boundInventory.globalPos().dimension())) {
                    renderSelectedBlock(evt, boundInventory.globalPos().pos(), boundInventory.direction());
                }
            }
        }
        if (heldItemOff.getItem() instanceof ToggleableTool toggleableTool) {
            ThingFinder.render(evt, player, heldItemOff);
            if (toggleableTool.canUseAbilityAndDurability(heldItemOff, Ability.VOIDSHIFT) && ToggleableTool.getCustomSetting(heldItemOff, Ability.VOIDSHIFT.getName()) == 0)
                MiscRenders.renderTransparentPlayer(evt, player, heldItemOff);
            if (toggleableTool.canUseAbility(heldItemOff, Ability.DROPTELEPORT) && ToggleableTool.getCustomSetting(heldItemMain, Ability.DROPTELEPORT.getName()) == 0) {
                NBTHelpers.BoundInventory boundInventory = ToggleableTool.getBoundInventory(heldItemOff);
                if (boundInventory != null && player.level().dimension().equals(boundInventory.globalPos().dimension())) {
                    renderSelectedBlock(evt, boundInventory.globalPos().pos(), boundInventory.direction());
                }
            }
        }
    }

    public static void renderSelectedBlock(RenderLevelStageEvent event, BlockPos pos, Direction direction) {
        final Minecraft mc = Minecraft.getInstance();

        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();

        Vec3 view = mc.gameRenderer.getMainCamera().getPosition();

        PoseStack matrix = event.getPoseStack();
        matrix.pushPose();
        matrix.translate(-view.x(), -view.y(), -view.z());

        matrix.pushPose();
        matrix.translate(pos.getX(), pos.getY(), pos.getZ());
        matrix.translate(-0.005f, -0.005f, -0.005f);
        matrix.scale(1.01f, 1.01f, 1.01f);
        //matrix.mulPose(Axis.YP.rotationDegrees(-90.0F));

        Matrix4f positionMatrix = matrix.last().pose();
        RenderHelpers.renderBoxSolid(matrix, positionMatrix, buffer, BlockPos.ZERO, 0, 1, 0, 0.25f);
        RenderHelpers.renderFaceSolid(matrix, positionMatrix, buffer, BlockPos.ZERO, direction, 0, 0, 1, 0.25f);
        RenderHelpers.renderLines(matrix, BlockPos.ZERO, BlockPos.ZERO, Color.WHITE, buffer);
        matrix.popPose();

        matrix.popPose();
        buffer.endBatch(OurRenderTypes.TRANSPARENT_BOX);
    }
}
