package com.direwolf20.justdirethings.common.capabilities;

import com.direwolf20.justdirethings.common.blockentities.ExperienceHolderBE;
import com.direwolf20.justdirethings.setup.Registration;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.function.Predicate;

public class ExperienceHolderFluidTank extends FluidTank {
    private final ExperienceHolderBE experienceHolderBE;

    public ExperienceHolderFluidTank(ExperienceHolderBE experienceHolderBE, Predicate<FluidStack> validator) {
        super(Integer.MAX_VALUE, validator);
        this.experienceHolderBE = experienceHolderBE;
        fluid = new FluidStack(Registration.XP_FLUID_SOURCE.get(), getFluidAmount());
    }

    public int getFluidAmount() {
        // Prevent overflow by capping experienceHolderBE.exp at Integer.MAX_VALUE / 20
        if (experienceHolderBE.exp > Integer.MAX_VALUE / 20) {
            return Integer.MAX_VALUE;  // If multiplying by 20 would overflow, return max int value
        }

        // Safe to multiply without overflow
        return Math.min(experienceHolderBE.exp * 20, getCapacity());
    }

    public int getCapacity() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || !isFluidValid(resource)) {
            return 0;
        }
        if (action.simulate()) {
            if (fluid.isEmpty()) {
                return Math.min(capacity, resource.getAmount() - resource.getAmount() % 20);
            }
            return Math.min(capacity - getFluidAmount() - ((capacity - getFluidAmount()) % 20), resource.getAmount() - resource.getAmount() % 20);
        }
        if (fluid.isEmpty()) {
            int returnAmt = resource.getAmount() - insertFluid(resource.getAmount());
            fluid = new FluidStack(Registration.XP_FLUID_SOURCE.get(), returnAmt);
            onContentsChanged();
            return returnAmt;
        }
        int filled = resource.getAmount() - insertFluid(resource.getAmount());
        if (filled > 0)
            onContentsChanged();
        return filled;
    }

    public int insertFluid(int amt) {
        int remaining = experienceHolderBE.addExp(amt / 20);
        int excessFluid = amt % 20;  // Calculate remainder fluid (less than 1 XP)
        return (remaining * 20) + excessFluid;
    }

    public int extractFluid(int amt) {
        int expNeeded = amt / 20;
        int unAvailable = experienceHolderBE.subExp(expNeeded);
        return (unAvailable * 20) + (amt % 20);
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || !FluidStack.isSameFluidSameComponents(resource, fluid)) {
            return FluidStack.EMPTY;
        }
        return drain(resource.getAmount(), action);
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        int drained = maxDrain - (maxDrain % 20); //Trim remainder
        if (getFluidAmount() < drained) {
            drained = getFluidAmount();
        }
        FluidStack stack = fluid.copyWithAmount(drained);
        if (action.execute() && drained > 0) {
            extractFluid(drained);
            onContentsChanged();
        }
        return stack;
    }

    @Override
    protected void onContentsChanged() {
        experienceHolderBE.markDirtyClient();
    }
}
