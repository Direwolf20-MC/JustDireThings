package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.client.particles.itemparticle.ItemFlowParticleData;
import com.direwolf20.justdirethings.common.blockentities.basebe.AreaAffectingBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.RedstoneControlledBE;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.ExperienceUtils;
import com.direwolf20.justdirethings.util.interfacehelpers.AreaAffectingData;
import com.direwolf20.justdirethings.util.interfacehelpers.FilterData;
import com.direwolf20.justdirethings.util.interfacehelpers.RedstoneControlData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
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
    public int exp;

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

    public void storeExp(Player player, int levelChange) {
        if (levelChange == -1) {
            // Move all experience from player
            int totalExp = ExperienceUtils.getPlayerTotalExperience(player);
            this.exp += totalExp;
            player.giveExperiencePoints(-totalExp); // Removes all levels
            player.giveExperienceLevels(-1); //Handles dangling Floating Point Math (RAGE!) Consider it a tax on storing exp :)
        } else if (levelChange > 0) {
            // Handle fractional progress first, if the player is in the middle of a level
            int expInCurrentLevel = (int) (player.experienceProgress * player.getXpNeededForNextLevel());

            // If the player has partial progress within the current level, remove that first
            if (expInCurrentLevel > 0) {
                int expRemoved = ExperienceUtils.removePoints(player, expInCurrentLevel);
                this.exp += expRemoved;
                levelChange--;  // We've already removed part of a level
            }

            if (levelChange > 0) {
                // Now remove the specified number of full levels
                int expRemoved = ExperienceUtils.removeLevels(player, levelChange);
                this.exp += expRemoved;
            }
        }

        markDirtyClient();
    }

    public void extractExp(Player player, int levelChange) {
        if (exp == 0) return;  // No experience in the block, exit early

        if (levelChange == -1) {
            // Move all experience from block to player
            int expToGive = exp;
            player.giveExperiencePoints(expToGive);
            this.exp = 0;  // Remove all experience from the block
        } else if (levelChange > 0) {
            // Handle fractional progress first, if the player is in the middle of a level
            if (roundUpToNextLevel(player))
                levelChange--;

            if (levelChange > 0 && this.exp > 0) {
                // Give full levels based on the remaining levels requested
                int expForNextLevels = ExperienceUtils.getTotalExperienceForLevel(player.experienceLevel + levelChange) - ExperienceUtils.getPlayerTotalExperience(player);
                int expToGive = Math.min(this.exp, expForNextLevels);
                player.giveExperiencePoints(expToGive);
                this.exp -= expToGive;
                roundUpToNextLevel(player); //Thanks Floating point math!!
            }
        }

        markDirtyClient();
    }

    public boolean roundUpToNextLevel(Player player) {
        if (this.exp <= 0) return false; // No experience available to round up
        int expInCurrentLevel = (int) (player.experienceProgress * player.getXpNeededForNextLevel());
        if (expInCurrentLevel > 0) {
            int expToGive = Math.min(exp, ExperienceUtils.getExpNeededForNextLevel(player));
            player.giveExperiencePoints(expToGive);
            this.exp -= expToGive;
            return true;
        }
        return false;
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

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt("exp", exp);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        exp = tag.getInt("exp");
    }
}
