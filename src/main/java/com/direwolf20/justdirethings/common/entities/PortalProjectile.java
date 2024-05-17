package com.direwolf20.justdirethings.common.entities;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class PortalProjectile extends Projectile {
    public PortalProjectile(EntityType<? extends Projectile> entityType, Level world) {
        super(entityType, world);
    }

    public PortalProjectile(Level world, Player player) {
        this(Registration.PortalProjectile.get(), world);
        setOwner(player);
        setPos(player.getEyePosition());
    }

    @Override
    public void tick() {
        super.tick();
        System.out.println("I'm here: " + this.tickCount);
        if (tickCount > 60)
            spawnPortal(this.getX(), this.getY(), this.getZ());
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        spawnPortal(result.getLocation().x, result.getLocation().y, result.getLocation().z);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    protected void spawnPortal(double x, double y, double z) {
        Level level = this.level();
        if (!level.isClientSide) {
            PortalEntity portal = new PortalEntity(Registration.PortalEntity.get(), level);
            portal.setPos(x, y, z);
            level.addFreshEntity(portal);
            this.discard();
        }
    }
}
