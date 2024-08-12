package com.direwolf20.justdirethings.common.blockentities.basebe;

import com.direwolf20.justdirethings.client.particles.gooexplodeparticle.GooExplodeParticleData;
import com.direwolf20.justdirethings.datagen.recipes.GooSpreadRecipe;
import com.direwolf20.justdirethings.setup.Config;
import com.direwolf20.justdirethings.setup.Registration;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Random;

import static com.direwolf20.justdirethings.common.blocks.gooblocks.GooBlock_Base.ALIVE;

public class GooBlockBE_Base extends BlockEntity {
    public final Map<Direction, Integer> sidedCounters = new Object2IntOpenHashMap<>();
    public final Map<Direction, Integer> sidedDurations = new Object2IntOpenHashMap<>();

    public GooBlockBE_Base(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        for (Direction direction : Direction.values()) {
            sidedCounters.put(direction, -1); //Init the counters, -1 means we aren't operating on anything on that side
            sidedDurations.put(direction, -1); //Init the durations, -1 means we aren't operating on anything on that side
        }
    }

    public void updateSideCounter(Direction direction, int newCounter) {
        int oldCounter = sidedCounters.get(direction);
        sidedCounters.put(direction, newCounter);
        if (oldCounter >= 0 && newCounter == -1 && level.isClientSide) {
            spawnParticles(direction);
        }
    }

    public int counterReducer() {
        return 1;
    }

    public int getTier() {
        return 0;
    }

    public int getCraftingDuration(Direction direction) {
        return sidedDurations.get(direction);
    }

    public int getRemainingTimeFor(Direction direction) {
        return sidedCounters.get(direction);
    }

    public void tickClient() {
        tickCounters(); //We tick on client side too, just for rendering of course!
    }

    public void tickServer() {
        checkSides();
        tickCounters();
        this.setChanged();
    }

    public void spawnParticles(Direction side) {
        Random random = new Random();
        BlockPos startPos = getBlockPos().relative(side);
        ItemStack itemStack = new ItemStack(getBlockState().getBlock());
        GooExplodeParticleData data = new GooExplodeParticleData(itemStack);
        for (Direction direction : Direction.values()) {
            for (int i = 0; i < 100; i++) {
                double randomX = 0.5 + (0.6 * direction.getNormal().getX()) + (direction.getNormal().getX() == 0 ? random.nextDouble() - 0.5 : 0);
                double randomY = 0.5 + (0.6 * direction.getNormal().getY()) + (direction.getNormal().getY() == 0 ? random.nextDouble() - 0.5 : 0);
                double randomZ = 0.5 + (0.6 * direction.getNormal().getZ()) + (direction.getNormal().getZ() == 0 ? random.nextDouble() - 0.5 : 0);
                level.addParticle(data, startPos.getX() + randomX, startPos.getY() + randomY, startPos.getZ() + randomZ, 0, 0, 0);
            }
        }
    }

    public void tickCounters() {
        for (Direction direction : Direction.values()) {
            int sideCounter = sidedCounters.get(direction);
            if (sideCounter > 0) {
                sideCounter = Math.max(sideCounter - counterReducer(), 0);
                updateSideCounter(direction, sideCounter);
            }
        }
    }

    public void checkSides() {
        for (Direction direction : Direction.values()) {
            GooSpreadRecipe gooSpreadRecipe = findRecipe(getBlockPos().relative(direction));
            int sideCounter = sidedCounters.get(direction);
            if (gooSpreadRecipe != null) {
                if (sideCounter == -1 && this.getBlockState().getValue(ALIVE)) { //Valid Recipe and not running yet
                    sideCounter = gooSpreadRecipe.getCraftingDuration();
                    updateSideCounter(direction, sideCounter);
                    sidedDurations.put(direction, sideCounter);
                    markDirtyClient(); //Either way, update the client with the new sideCounters
                } else if (sideCounter == 0) { //Craftings done!
                    setBlockToTarget(gooSpreadRecipe, direction);
                    markDirtyClient(); //Either way, update the client with the new sideCounters
                }
            } else { //If the recipe is null, it means this isn't a valid input block (or its already been converted!)
                if (sideCounter != -1) { //If we have a timer running, cancel it
                    sideCounter = -1;
                    updateSideCounter(direction, sideCounter);
                    sidedDurations.put(direction, sideCounter);
                    markDirtyClient();
                }
            }
        }
    }

    public void setBlockToTarget(GooSpreadRecipe gooSpreadRecipe, Direction direction) {
        if (gooSpreadRecipe.getOutput().hasProperty(BlockStateProperties.FACING))
            level.setBlockAndUpdate(getBlockPos().relative(direction), gooSpreadRecipe.getOutput().setValue(BlockStateProperties.FACING, direction));
        else
            level.setBlockAndUpdate(getBlockPos().relative(direction), gooSpreadRecipe.getOutput());
        updateSideCounter(direction, -1);
        sidedDurations.put(direction, -1);
        level.playSound(null, getBlockPos(), SoundEvents.SCULK_BLOCK_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
        killGoo();
    }

    private void killGoo() {
        if (!Config.GOO_CAN_DIE.get()) return;
        if (level != null && !level.isClientSide) {
            BlockState state = this.getBlockState();

            // Ensure the block is currently alive and perform the 10% chance check
            if (state.getValue(ALIVE) && level.random.nextFloat() < 0.1f) {
                // Update the block state to dead
                level.setBlock(worldPosition, state.setValue(ALIVE, false), 3);
                level.playSound(null, getBlockPos(), SoundEvents.VEX_DEATH, SoundSource.BLOCKS, 1.0F, 0.25F);
            }
        }
    }

    @Nullable
    private GooSpreadRecipe findRecipe(BlockPos coords) {
        BlockState state = getLevel().getBlockState(coords);
        RecipeManager recipeManager = getLevel().getRecipeManager();

        for (RecipeHolder<?> recipe : recipeManager.getAllRecipesFor(Registration.GOO_SPREAD_RECIPE_TYPE.get())) {
            if (recipe.value() instanceof GooSpreadRecipe gooSpreadRecipe && gooSpreadRecipe.matches(getLevel(), coords, this, state)) {
                return gooSpreadRecipe;
            }
        }

        return null;
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        ListTag counterListTag = new ListTag();
        for (Direction direction : Direction.values()) {
            CompoundTag counterTag = new CompoundTag();
            counterTag.putInt("side", direction.ordinal());
            counterTag.putInt("counter", sidedCounters.get(direction));
            counterListTag.add(counterTag);
        }
        tag.put("sideCounters", counterListTag);

        ListTag durationListTag = new ListTag();
        for (Direction direction : Direction.values()) {
            CompoundTag durationTag = new CompoundTag();
            durationTag.putInt("side", direction.ordinal());
            durationTag.putInt("duration", sidedDurations.get(direction));
            durationListTag.add(durationTag);
        }
        tag.put("sideDurations", durationListTag);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        if (tag.contains("sideCounters")) {
            ListTag listNBT = tag.getList("sideCounters", Tag.TAG_COMPOUND);
            for (int i = 0; i < listNBT.size(); i++) {
                CompoundTag sideCounterTag = listNBT.getCompound(i);
                int direction = sideCounterTag.getInt("side");
                int counter = sideCounterTag.getInt("counter");
                this.updateSideCounter(Direction.values()[direction], counter);
            }
        }
        if (tag.contains("sideDurations")) {
            ListTag listNBT = tag.getList("sideDurations", Tag.TAG_COMPOUND);
            for (int i = 0; i < listNBT.size(); i++) {
                CompoundTag sideCounterTag = listNBT.getCompound(i);
                int direction = sideCounterTag.getInt("side");
                int duration = sideCounterTag.getInt("duration");
                this.sidedDurations.put(Direction.values()[direction], duration);
            }
        }
        super.loadAdditional(tag, provider);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        // Vanilla uses the type parameter to indicate which type of tile entity (command block, skull, or beacon?) is receiving the packet, but it seems like Forge has overridden this behavior
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        this.loadAdditional(tag, lookupProvider);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider p_323910_) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, p_323910_);
        return tag;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        this.loadAdditional(pkt.getTag(), lookupProvider);
    }

    public void markDirtyClient() {
        this.setChanged();
        if (this.getLevel() != null) {
            BlockState state = this.getLevel().getBlockState(this.getBlockPos());
            this.getLevel().sendBlockUpdated(this.getBlockPos(), state, state, 3);
        }
    }
}
