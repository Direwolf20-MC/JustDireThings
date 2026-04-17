package com.direwolf20.justdirethings.common.blockentities.basebe;

import com.direwolf20.justdirethings.client.particles.gooexplodeparticle.GooExplodeParticleData;
import com.direwolf20.justdirethings.datagen.recipes.GooSpreadRecipe;
import com.direwolf20.justdirethings.datagen.recipes.GooSpreadRecipeTag;
import com.direwolf20.justdirethings.setup.Config;
import com.direwolf20.justdirethings.setup.Registration;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.direwolf20.justdirethings.common.blocks.gooblocks.GooBlock_Base.ALIVE;

public class GooBlockBE_Base extends BlockEntity {
    public final Map<Direction, Integer> sidedCounters = new Object2IntOpenHashMap<>();
    public final Map<Direction, Integer> sidedDurations = new Object2IntOpenHashMap<>();

    //Cache so we don't have to iterate recipes every time
    public final Map<BlockState, BlockState> outputCache = new HashMap<>();
    public final Map<BlockState, Integer> durationCache = new Object2IntOpenHashMap<>();

    public GooBlockBE_Base(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        for (Direction direction : Direction.values()) {
            sidedCounters.put(direction, -1); //Init the counters, -1 means we aren't operating on anything on that side
            sidedDurations.put(direction, -1); //Init the durations, -1 means we aren't operating on anything on that side
        }
    }

    public boolean updateSideCounter(Direction direction, int newCounter) {
        int oldCounter = sidedCounters.get(direction);
        sidedCounters.put(direction, newCounter);
        if (oldCounter >= 0 && newCounter == -1 && level.isClientSide()) {
            spawnParticles(direction);
        }
        int duration = sidedDurations.get(direction);
        if (newCounter <= 0 || duration <= 0)
            return false;
        // Every 3 seconds or so
        return (newCounter % (60 * counterReducer())) == 0;
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
                double randomX = 0.5 + (0.6 * direction.getUnitVec3i().getX()) + (direction.getUnitVec3i().getX() == 0 ? random.nextDouble() - 0.5 : 0);
                double randomY = 0.5 + (0.6 * direction.getUnitVec3i().getY()) + (direction.getUnitVec3i().getY() == 0 ? random.nextDouble() - 0.5 : 0);
                double randomZ = 0.5 + (0.6 * direction.getUnitVec3i().getZ()) + (direction.getUnitVec3i().getZ() == 0 ? random.nextDouble() - 0.5 : 0);
                level.addParticle(data, startPos.getX() + randomX, startPos.getY() + randomY, startPos.getZ() + randomZ, 0, 0, 0);
            }
        }
    }

    public void tickCounters() {
        boolean needsUpdate = false;
        for (Direction direction : Direction.values()) {
            int sideCounter = sidedCounters.get(direction);
            if (sideCounter > 0) {
                sideCounter = Math.max(sideCounter - counterReducer(), 0);
                if (updateSideCounter(direction, sideCounter))
                    needsUpdate = true;
            }
        }
        if (needsUpdate && !level.isClientSide())
            markDirtyClient();
    }

    public void checkSides() {
        if (level == null) return;
        for (Direction direction : Direction.values()) {
            BlockState input = level.getBlockState(getBlockPos().relative(direction));
            BlockState output = findOutput(input);
            int duration = findDuration(input);
            int sideCounter = sidedCounters.get(direction);
            if (!output.isAir()) {
                if (sideCounter == -1 && this.getBlockState().getValue(ALIVE)) { //Valid Recipe and not running yet
                    sideCounter = duration;
                    sidedDurations.put(direction, sideCounter);
                    updateSideCounter(direction, sideCounter);
                    markDirtyClient(); //Either way, update the client with the new sideCounters
                } else if (sideCounter == 0) { //Craftings done!
                    setBlockToTarget(output, direction);
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

    public void setBlockToTarget(BlockState output, Direction direction) {
        if (output.hasProperty(BlockStateProperties.FACING))
            level.setBlockAndUpdate(getBlockPos().relative(direction), output.setValue(BlockStateProperties.FACING, direction));
        else
            level.setBlockAndUpdate(getBlockPos().relative(direction), output);
        updateSideCounter(direction, -1);
        sidedDurations.put(direction, -1);
        level.playSound(null, getBlockPos(), SoundEvents.SCULK_BLOCK_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
        killGoo();
    }

    private void killGoo() {
        if (!Config.GOO_CAN_DIE.get()) return;
        if (level != null && !level.isClientSide()) {
            BlockState state = this.getBlockState();

            // Ensure the block is currently alive and perform the 10% chance check
            if (state.getValue(ALIVE) && level.getRandom().nextFloat() < Config.GOO_DEATH_CHANCE.get().floatValue()) {
                // Update the block state to dead
                level.setBlock(worldPosition, state.setValue(ALIVE, false), 3);
                level.playSound(null, getBlockPos(), SoundEvents.VEX_DEATH, SoundSource.BLOCKS, 1.0F, 0.25F);
            }
        }
    }

    public BlockState findOutput(BlockState input) {
        if (!outputCache.containsKey(input))
            populateCaches(input);
        return outputCache.get(input);
    }

    public int findDuration(BlockState input) {
        if (!durationCache.containsKey(input))
            populateCaches(input);
        return durationCache.get(input);
    }

    public void populateCaches(BlockState input) {
        BlockState output = Blocks.AIR.defaultBlockState();
        int duration = -1;
        GooSpreadRecipe gooSpreadRecipe = findRecipe(input);
        if (gooSpreadRecipe != null) {
            output = gooSpreadRecipe.getOutput();
            duration = gooSpreadRecipe.getCraftingDuration();
        } else {
            GooSpreadRecipeTag gooSpreadRecipeTag = findRecipeTag(input);
            if (gooSpreadRecipeTag != null) {
                output = gooSpreadRecipeTag.getOutput();
                duration = gooSpreadRecipeTag.getCraftingDuration();
            }
        }
        outputCache.put(input, output);
        durationCache.put(input, duration);
    }

    @Nullable
    private GooSpreadRecipe findRecipe(BlockState state) {
        RecipeManager recipeManager = getLevel().getRecipeManager();

        for (RecipeHolder<?> recipe : recipeManager.getAllRecipesFor(Registration.GOO_SPREAD_RECIPE_TYPE.get())) {
            if (recipe.value() instanceof GooSpreadRecipe gooSpreadRecipe && gooSpreadRecipe.matches(this, state)) {
                return gooSpreadRecipe;
            }
        }

        return null;
    }

    @Nullable
    private GooSpreadRecipeTag findRecipeTag(BlockState state) {
        RecipeManager recipeManager = getLevel().getRecipeManager();

        for (RecipeHolder<?> recipe : recipeManager.getAllRecipesFor(Registration.GOO_SPREAD_RECIPE_TYPE_TAG.get())) {
            if (recipe.value() instanceof GooSpreadRecipeTag gooSpreadRecipeTag && gooSpreadRecipeTag.matches(this, state)) {
                return gooSpreadRecipeTag;
            }
        }

        return null;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ValueOutput.ValueOutputList counterList = output.childrenList("sideCounters");
        for (Direction direction : Direction.values()) {
            ValueOutput counter = counterList.addChild();
            counter.putInt("side", direction.ordinal());
            counter.putInt("counter", sidedCounters.get(direction));
        }

        ValueOutput.ValueOutputList durationList = output.childrenList("sideDurations");
        for (Direction direction : Direction.values()) {
            ValueOutput duration = durationList.addChild();
            duration.putInt("side", direction.ordinal());
            duration.putInt("duration", sidedDurations.get(direction));
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        input.childrenList("sideCounters").ifPresent(list -> {
            for (ValueInput child : list) {
                int dir = child.getIntOr("side", 0);
                int counter = child.getIntOr("counter", -1);
                this.updateSideCounter(Direction.values()[dir], counter);
            }
        });
        input.childrenList("sideDurations").ifPresent(list -> {
            for (ValueInput child : list) {
                int dir = child.getIntOr("side", 0);
                int duration = child.getIntOr("duration", -1);
                this.sidedDurations.put(Direction.values()[dir], duration);
            }
        });
        super.loadAdditional(input);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(ValueInput input) {
        this.loadWithComponents(input);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return saveCustomOnly(provider);
    }

    @Override
    public void onDataPacket(Connection net, ValueInput input) {
        this.loadWithComponents(input);
    }

    public void markDirtyClient() {
        this.setChanged();
        if (this.getLevel() != null) {
            BlockState state = this.getLevel().getBlockState(this.getBlockPos());
            this.getLevel().sendBlockUpdated(this.getBlockPos(), state, state, 3);
        }
    }
}
