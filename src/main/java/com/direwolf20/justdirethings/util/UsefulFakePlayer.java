package com.direwolf20.justdirethings.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.ITeleporter;

import java.util.OptionalInt;

/**
 * This class completely and shamelessly stolen from Shadows from his mod Click Machine :)
 */
public class UsefulFakePlayer extends FakePlayer {

    public UsefulFakePlayer(Level world, GameProfile name) {
        super((ServerLevel) world, name);
    }

    @Override
    public void initMenu(AbstractContainerMenu p_143400_) {
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
    public Entity changeDimension(ServerLevel server, ITeleporter teleporter) {
        return createPlayer(server, this.getGameProfile());
    }

    /**
     * Creates a new UsefulFakePlayer.
     */
    public static UsefulFakePlayer createPlayer(Level world, GameProfile profile) {
        return new UsefulFakePlayer(world, profile);
    }
}
