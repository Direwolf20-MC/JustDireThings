package com.direwolf20.justdirethings.common.capabilities;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;

import java.util.function.Predicate;

public class JustDireFluidTank extends FluidStacksResourceHandler {
    private final Predicate<FluidStack> validator;

    public JustDireFluidTank(int capacity) {
        this(capacity, fs -> true);
    }

    public JustDireFluidTank(int capacity, Predicate<FluidStack> validator) {
        super(1, capacity);
        this.validator = validator;
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        if (resource.isEmpty()) return true;
        return validator.test(resource.toStack(1));
    }
}
