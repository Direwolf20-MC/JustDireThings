package com.direwolf20.justdirethings.common.fluids.refinedt4fuel;

import com.direwolf20.justdirethings.common.fluids.basefluids.RefinedFuel;
import com.direwolf20.justdirethings.setup.Config;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public abstract class RefinedT4Fuel extends BaseFlowingFluid implements RefinedFuel {
    public static final Properties PROPERTIES = new Properties(
            JDTRegistration.REFINED_T4_FLUID_TYPE,
            JDTRegistration.REFINED_T4_FLUID_FLOWING,
            JDTRegistration.REFINED_T4_FLUID_SOURCE
    ).bucket(JDTRegistration.REFINED_T4_FLUID_BUCKET).block(JDTRegistration.REFINED_T4_FLUID_BLOCK);

    protected RefinedT4Fuel(Properties properties) {
        super(properties);
    }

    @Override
    public int fePerMb() {
        return Config.FUEL_TIER4_FE_PER_MB.get();
    }

    @Override
    public Fluid getFlowing() {
        return JDTRegistration.REFINED_T4_FLUID_FLOWING.get();
    }

    @Override
    public Fluid getSource() {
        return JDTRegistration.REFINED_T4_FLUID_SOURCE.get();
    }

    @Override
    public Item getBucket() {
        return JDTRegistration.REFINED_T4_FLUID_BUCKET.get();
    }

    @Override
    protected boolean canConvertToSource(ServerLevel pLevel) {
        return false;
    }

    public static class Flowing extends RefinedT4Fuel {
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

    public static class Source extends RefinedT4Fuel {
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