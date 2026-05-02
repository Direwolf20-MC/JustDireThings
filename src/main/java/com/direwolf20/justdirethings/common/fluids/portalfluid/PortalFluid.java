package com.direwolf20.justdirethings.common.fluids.portalfluid;

import com.direwolf20.justdirethings.setup.JDTRegistration;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public abstract class PortalFluid extends BaseFlowingFluid {
    public static final BaseFlowingFluid.Properties PROPERTIES = new BaseFlowingFluid.Properties(
            JDTRegistration.PORTAL_FLUID_TYPE,
            JDTRegistration.PORTAL_FLUID_FLOWING,
            JDTRegistration.PORTAL_FLUID_SOURCE
    ).bucket(JDTRegistration.PORTAL_FLUID_BUCKET).block(JDTRegistration.PORTAL_FLUID_BLOCK);

    protected PortalFluid(Properties properties) {
        super(properties);
    }

    @Override
    public Fluid getFlowing() {
        return JDTRegistration.PORTAL_FLUID_FLOWING.get();
    }

    @Override
    public Fluid getSource() {
        return JDTRegistration.PORTAL_FLUID_SOURCE.get();
    }

    @Override
    public Item getBucket() {
        return JDTRegistration.PORTAL_FLUID_BUCKET.get();
    }

    @Override
    protected boolean canConvertToSource(ServerLevel pLevel) {
        return false;
    }

    public static class Flowing extends PortalFluid {
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

    public static class Source extends PortalFluid {
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