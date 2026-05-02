package com.direwolf20.justdirethings.common.fluids.timefluid;

import com.direwolf20.justdirethings.setup.JDTRegistration;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public abstract class TimeFluid extends BaseFlowingFluid {
    public static final Properties PROPERTIES = new Properties(
            JDTRegistration.TIME_FLUID_TYPE,
            JDTRegistration.TIME_FLUID_FLOWING,
            JDTRegistration.TIME_FLUID_SOURCE
    ).bucket(JDTRegistration.TIME_FLUID_BUCKET).block(JDTRegistration.TIME_FLUID_BLOCK);

    protected TimeFluid(Properties properties) {
        super(properties);
    }

    @Override
    public Fluid getFlowing() {
        return JDTRegistration.TIME_FLUID_FLOWING.get();
    }

    @Override
    public Fluid getSource() {
        return JDTRegistration.TIME_FLUID_SOURCE.get();
    }

    @Override
    public Item getBucket() {
        return JDTRegistration.TIME_FLUID_BUCKET.get();
    }

    @Override
    protected boolean canConvertToSource(ServerLevel pLevel) {
        return false;
    }

    public static class Flowing extends TimeFluid {
        public Flowing() {
            super(PROPERTIES);
        }

        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> pBuilder) {
            super.createFluidStateDefinition(pBuilder);
            pBuilder.add(LEVEL);
        }

        @Override
        public int getAmount(FluidState pState) {
            return pState.getValue(LEVEL);
        }

        @Override
        public boolean isSource(FluidState pState) {
            return false;
        }
    }

    public static class Source extends TimeFluid {
        public Source() {
            super(PROPERTIES);
        }

        @Override
        public int getAmount(FluidState pState) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState pState) {
            return true;
        }
    }
}