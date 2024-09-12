package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.client.particles.itemparticle.ItemFlowParticleData;
import com.direwolf20.justdirethings.common.blockentities.basebe.AreaAffectingBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.RedstoneControlledBE;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.interfacehelpers.AreaAffectingData;
import com.direwolf20.justdirethings.util.interfacehelpers.FilterData;
import com.direwolf20.justdirethings.util.interfacehelpers.RedstoneControlData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ExperienceHolderBE extends BaseMachineBE implements AreaAffectingBE, RedstoneControlledBE {
    public FilterData filterData = new FilterData();
    public AreaAffectingData areaAffectingData = new AreaAffectingData();
    public RedstoneControlData redstoneControlData = new RedstoneControlData();

    public ExperienceHolderBE(BlockPos pPos, BlockState pBlockState) {
        super(Registration.ExperienceHolderBE.get(), pPos, pBlockState);
    }

    @Override
    public BlockEntity getBlockEntity() {
        return this;
    }

    @Override
    public RedstoneControlData getRedstoneControlData() {
        return redstoneControlData;
    }

    @Override
    public AreaAffectingData getAreaAffectingData() {
        return areaAffectingData;
    }

    public void tickClient() {
    }

    public void tickServer() {
        super.tickServer();
        handleExperience();
    }

    public void doParticles(ItemStack itemStack, Vec3 sourcePos) {
        Direction direction = getBlockState().getValue(BlockStateProperties.FACING);
        BlockPos blockPos = getBlockPos();
        ItemFlowParticleData data = new ItemFlowParticleData(itemStack, blockPos.getX() + 0.5f - (0.3 * direction.getStepX()), blockPos.getY() + 0.5f - (0.3 * direction.getStepY()), blockPos.getZ() + 0.5f - (0.3 * direction.getStepZ()), 5);
        double d0 = sourcePos.x();
        double d1 = sourcePos.y();
        double d2 = sourcePos.z();
        ((ServerLevel) level).sendParticles(data, d0, d1, d2, 10, 0, 0, 0, 0);
    }

    private void handleExperience() {
        if (!isActiveRedstone() || !canRun()) return;
        assert level != null;
        AABB searchArea = getAABB(getBlockPos());

        List<Player> entityList = level.getEntitiesOfClass(Player.class, searchArea, entity -> true)
                .stream().toList();

        if (entityList.isEmpty()) return;

        for (Player player : entityList) {

        }
    }
}
