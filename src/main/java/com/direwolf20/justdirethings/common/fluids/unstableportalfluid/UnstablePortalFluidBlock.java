package com.direwolf20.justdirethings.common.fluids.unstableportalfluid;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class UnstablePortalFluidBlock extends LiquidBlock {
    public static final Random random = new Random();

    public UnstablePortalFluidBlock(Properties properties) {
        super(Registration.UNSTABLE_PORTAL_FLUID_SOURCE.get(), properties);
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        double d0 = (double) pPos.getX() + random.nextDouble();
        double d1 = (double) pPos.getY() + 0.7;
        double d2 = (double) pPos.getZ() + random.nextDouble();
        double min = -0.01;
        double max = 0.01;
        double moveX = min + (max - min) * random.nextDouble();
        double moveZ = min + (max - min) * random.nextDouble();
        pLevel.addParticle(ParticleTypes.REVERSE_PORTAL, d0, d1, d2, moveX, 0.01, moveZ);
    }
}
