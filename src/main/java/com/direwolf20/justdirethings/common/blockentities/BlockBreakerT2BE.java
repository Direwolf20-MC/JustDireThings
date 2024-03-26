package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.AreaAffectingBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.FilterableBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.PoweredMachineBE;
import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.interfacehelpers.AreaAffectingData;
import com.direwolf20.justdirethings.util.interfacehelpers.FilterData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.Set;
import java.util.stream.Collectors;

public class BlockBreakerT2BE extends BlockBreakerT1BE implements PoweredMachineBE, AreaAffectingBE, FilterableBE {
    public FilterData filterData = new FilterData();
    public AreaAffectingData areaAffectingData = new AreaAffectingData(0, 0, 0, 0, 1, 0);

    public BlockBreakerT2BE(BlockPos pPos, BlockState pBlockState) {
        super(Registration.BlockBreakerT2BE.get(), pPos, pBlockState);
    }

    @Override
    public void setDirection() {
        direction = Direction.UP; //Todo UI Button
    }

    @Override
    public void tickServer() {
        super.tickServer();
    }

    @Override
    public AreaAffectingData getAreaAffectingData() {
        return areaAffectingData;
    }

    @Override
    public FilterBasicHandler getFilterHandler() {
        return getData(Registration.HANDLER_BASIC_FILTER);
    }

    @Override
    public FilterData getFilterData() {
        return filterData;
    }

    @Override
    public Set<BlockPos> findBlocksToMine() {
        AABB area = getAABB(getBlockPos());
        return BlockPos.betweenClosedStream((int) area.minX, (int) area.minY, (int) area.minZ, (int) area.maxX - 1, (int) area.maxY - 1, (int) area.maxZ - 1)
                .filter(blockPos -> {
                    if (blockPos.equals(getBlockPos()))
                        return false;
                    if (blockBreakingTracker.containsKey(blockPos))
                        return false;
                    return true;
                })
                .map(BlockPos::immutable)
                .collect(Collectors.toSet());
    }
}
