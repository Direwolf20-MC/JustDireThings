package com.direwolf20.justdirethings.common.blocks.gooblocks;

import com.direwolf20.justdirethings.common.blockentities.basebe.GooBlockBE_Base;
import com.direwolf20.justdirethings.datagen.JustDireItemTags;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class GooBlock_Base extends Block implements EntityBlock {

    public static final BooleanProperty ALIVE = BooleanProperty.create("alive");

    public GooBlock_Base() {
        super(Properties.of()
                .sound(SoundType.FUNGUS)
                .strength(2.0f)
                .noOcclusion()
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(ALIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ALIVE);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        // Check if the item in hand is sugar and the block is in the dead state
        if (!state.getValue(ALIVE) && validRevivalItem(itemStack)) {
            if (!level.isClientSide) {
                // Convert the block to alive
                level.setBlock(pos, state.setValue(ALIVE, true), 3);

                // Play a sound effect to indicate the block has been revived
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.playSound(
                            null,  // No specific player, plays for all nearby
                            pos,  // Position of the sound
                            SoundEvents.SCULK_BLOCK_SPREAD,  // Sound event (you can choose a different one)
                            SoundSource.BLOCKS,  // Sound category
                            1.0f,  // Volume
                            0.5f   // Pitch
                    );
                }

                // Consume the sugar if the player is not in creative mode
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                }
            }
            return ItemInteractionResult.SUCCESS;
        }

        return super.useItemOn(itemStack, state, level, pos, player, hand, blockHitResult);
    }

    protected boolean validRevivalItem(ItemStack itemStack) {
        return itemStack.is(JustDireItemTags.GOO_REVIVE_TIER_1);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return (lvl, pos, blockState, t) -> {
                if (t instanceof GooBlockBE_Base tile) {
                    tile.tickClient();
                }
            };
        }
        return (lvl, pos, blockState, t) -> {
            if (t instanceof GooBlockBE_Base tile) {
                tile.tickServer();
            }
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GooBlockBE_Base(Registration.GooBlockBE_Tier1.get(), pos, state);
    }
}
