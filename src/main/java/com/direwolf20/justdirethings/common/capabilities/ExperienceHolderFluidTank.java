package com.direwolf20.justdirethings.common.capabilities;

import com.direwolf20.justdirethings.common.blockentities.ExperienceHolderBE;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.function.Predicate;

public class ExperienceHolderFluidTank implements ResourceHandler<FluidResource> {
    private static final int MB_PER_XP = 20;

    private final ExperienceHolderBE experienceHolderBE;
    private final Predicate<FluidResource> validator;
    private final Journal journal = new Journal();

    public ExperienceHolderFluidTank(ExperienceHolderBE experienceHolderBE, Predicate<FluidResource> validator) {
        this.experienceHolderBE = experienceHolderBE;
        this.validator = validator;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public FluidResource getResource(int index) {
        if (experienceHolderBE.exp <= 0) return FluidResource.EMPTY;
        return FluidResource.of(JDTRegistration.XP_FLUID_SOURCE.get());
    }

    @Override
    public long getAmountAsLong(int index) {
        if (experienceHolderBE.exp > Integer.MAX_VALUE / MB_PER_XP) {
            return Integer.MAX_VALUE;
        }
        return Math.min((long) experienceHolderBE.exp * MB_PER_XP, getCapacityAsLong(index, FluidResource.EMPTY));
    }

    @Override
    public long getCapacityAsLong(int index, FluidResource resource) {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        if (resource.isEmpty()) return true;
        return validator.test(resource);
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount);
        if (!isValid(index, resource)) return 0;
        int acceptable = amount - (amount % MB_PER_XP);
        if (acceptable <= 0) return 0;
        int xpToAdd = acceptable / MB_PER_XP;
        journal.updateSnapshots(transaction);
        int leftoverXp = experienceHolderBE.addExp(xpToAdd);
        int insertedXp = xpToAdd - leftoverXp;
        return insertedXp * MB_PER_XP;
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount);
        if (!resource.is(JDTRegistration.XP_FLUID_SOURCE.get())) return 0;
        int drainable = amount - (amount % MB_PER_XP);
        if (drainable <= 0) return 0;
        int xpNeeded = drainable / MB_PER_XP;
        journal.updateSnapshots(transaction);
        int unavailableXp = experienceHolderBE.subExp(xpNeeded);
        int extractedXp = xpNeeded - unavailableXp;
        return extractedXp * MB_PER_XP;
    }

    private class Journal extends SnapshotJournal<Integer> {
        @Override
        protected Integer createSnapshot() {
            return experienceHolderBE.exp;
        }

        @Override
        protected void revertToSnapshot(Integer snapshot) {
            experienceHolderBE.exp = snapshot;
        }

        @Override
        protected void onRootCommit(Integer originalState) {
            experienceHolderBE.markDirtyClient();
        }
    }
}
