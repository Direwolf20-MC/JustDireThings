package com.direwolf20.justdirethings.util;

import com.direwolf20.justdirethings.client.particles.itemparticle.ItemFlowParticleData;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;

import java.util.OptionalInt;

/**
 * This class completely and shamelessly stolen from Shadows from his mod Click Machine :)
 */
public class UsefulFakePlayer extends FakePlayer {
    private double reach;

    public UsefulFakePlayer(Level world, GameProfile name) {
        super((ServerLevel) world, name);
        setReach(getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE));
    }

    @Override
    public OptionalInt openMenu(MenuProvider p_9033_) {
        return OptionalInt.empty();
    }

    @Override
    public float getAttackStrengthScale(float adjustTicks) {
        return 1; // Prevent the attack strength from always being 0.03 due to not ticking.
    }

    @Override
    public ItemCooldowns getCooldowns() {
        return new ItemCooldowns(); //Prevent item cool downs due to player not ticking
    }

    @Override
    public boolean canBeSeenByAnyone() {
        return false; //Prevent them being targetting by mobs?
    }

    @Override
    public Entity changeDimension(DimensionTransition dimensionTransition) {
        return createPlayer(dimensionTransition.newLevel(), this.getGameProfile());
    }

    /**
     * Used by Dire to simulate using an item
     */
    public void fakeupdateUsingItem(ItemStack itemStack) {
        this.updateUsingItem(itemStack);
    }

    /**
     * Used by Dire to check the reach of his fake players
     */
    public double getReach() {
        return reach;
    }

    public void setReach(double reach) {
        this.reach = reach;
    }

    /**
     * Creates a new UsefulFakePlayer.
     */
    public static UsefulFakePlayer createPlayer(Level world, GameProfile profile) {
        return new UsefulFakePlayer(world, profile);
    }

    public void drawParticles(ServerLevel serverLevel, ItemStack itemStack) {
        Vec3 base = new Vec3(getX(), getEyeY(), getZ());
        Vec3 look = getLookAngle();
        Vec3 target = base.add(look.x * 0.9, look.y * 0.9, look.z * 0.9);
        ItemFlowParticleData data = new ItemFlowParticleData(itemStack, target.x, target.y, target.z, 5);
        double d0 = base.x();
        double d1 = base.y();
        double d2 = base.z();
        serverLevel.sendParticles(data, d0, d1, d2, 10, 0, 0, 0, 0);
    }
}
