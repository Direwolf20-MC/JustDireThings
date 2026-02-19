package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.FilterableBE;
import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.interfacehelpers.FilterData;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;

import java.util.*;

public class SensorT1BE extends BaseMachineBE implements FilterableBE {
    public FilterData filterData = new FilterData();
    protected List<BlockPos> positions = new ArrayList<>();
    public SENSE_TARGET sense_target = SENSE_TARGET.BLOCK;
    public boolean emitRedstone = false;
    public boolean strongSignal = false;
    public boolean newlyLoaded = true;
    public Map<Integer, Map<Property<?>, Comparable<?>>> blockStateProperties = new HashMap<>();
    public final Map<BlockState, Boolean> blockStateFilterCache = new Object2BooleanOpenHashMap<>();
    public int senseAmount = 0;
    public int equality = 0; //greaterthan, lessthan, equals

    public enum SENSE_TARGET {
        BLOCK,
        AIR,
        HOSTILE,
        PASSIVE,
        ADULT,
        CHILD,
        PLAYER,
        LIVING,
        ITEM;

        public SENSE_TARGET next() {
            SENSE_TARGET[] values = values();
            int nextOrdinal = (this.ordinal() + 1) % values.length;
            return values[nextOrdinal];
        }
    }

    public SensorT1BE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        ANYSIZE_FILTER_SLOTS = 1;
    }

    public SensorT1BE(BlockPos pPos, BlockState pBlockState) {
        this(Registration.SensorT1BE.get(), pPos, pBlockState);
    }

    public void addBlockStateProperty(int slot, Map<Property<?>, Comparable<?>> properties) {
        if (properties.isEmpty())
            blockStateProperties.remove(slot);
        else
            blockStateProperties.put(slot, properties);
        blockStateFilterCache.clear();
        markDirtyClient();
    }

    public Map<Property<?>, Comparable<?>> getBlockStateProperties(int slot) {
        return blockStateProperties.getOrDefault(slot, new HashMap<>());
    }

    @SuppressWarnings("unchecked")
    // Suppress the unchecked cast warning -- This is done this way in vanilla all over the place, so I guess its safe?
    private static <T extends Comparable<T>> String getName(Property<T> pProperty, Comparable<?> pValue) {
        return pProperty.getName((T) pValue);
    }

    private static <T extends Comparable<T>> Comparable<T> getValue(Property<T> pProperty, String pValue) {
        Optional<T> optional = pProperty.getValue(pValue);
        return optional.orElse(null);
    }

    public static ListTag saveBlockStateProperty(Map<Property<?>, Comparable<?>> propertiesList) {
        ListTag listTag = new ListTag();
        for (Map.Entry<Property<?>, Comparable<?>> value : propertiesList.entrySet()) {
            CompoundTag propertiesTag = new CompoundTag();
            propertiesTag.putString(value.getKey().getName(), getName(value.getKey(), value.getValue()));
            listTag.add(propertiesTag);
        }
        return listTag;
    }

    public CompoundTag saveBlockStateProperties() {
        CompoundTag compoundTag = new CompoundTag();
        blockStateProperties.forEach((index, propertiesList) -> {
            ListTag listTag = saveBlockStateProperty(propertiesList);
            compoundTag.put(index.toString(), listTag);
        });
        return compoundTag;
    }

    public static Map<Property<?>, Comparable<?>> loadBlockStateProperty(ListTag listTag, ItemStack stateStack) {
        Map<Property<?>, Comparable<?>> propertiesMap = new HashMap<>();
        if (stateStack.getItem() instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag propertiesTag = listTag.getCompound(i);
                propertiesTag.getAllKeys().forEach(propertyName -> {
                    Property<?> property = block.getStateDefinition().getProperty(propertyName);
                    if (property != null) {
                        String valueStr = propertiesTag.getString(propertyName);
                        Comparable<?> value = getValue(property, valueStr);
                        if (value != null)
                            propertiesMap.put(property, value);
                    }
                });

            }
        } else if (stateStack.getItem() instanceof BucketItem bucketItem) {
            BlockState defaultState = bucketItem.content.defaultFluidState().createLegacyBlock();
            Block block = defaultState.getBlock();
            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag propertiesTag = listTag.getCompound(i);
                propertiesTag.getAllKeys().forEach(propertyName -> {
                    Property<?> property = block.getStateDefinition().getProperty(propertyName);
                    if (property != null) {
                        String valueStr = propertiesTag.getString(propertyName);
                        Comparable<?> value = getValue(property, valueStr);
                        if (value != null)
                            propertiesMap.put(property, value);
                    }
                });

            }
        }
        return propertiesMap;
    }

    public void loadBlockStateProperties(CompoundTag nbt) {
        blockStateProperties.clear();
        blockStateFilterCache.clear();

        nbt.getAllKeys().forEach(key -> {
            int index = Integer.parseInt(key);
            ListTag listTag = nbt.getList(key, 10); // 10 for CompoundTag type
            ItemStack stateStack = getFilterHandler().getStackInSlot(index);
            Map<Property<?>, Comparable<?>> propertiesList = loadBlockStateProperty(listTag, stateStack);
            if (!propertiesList.isEmpty())
                blockStateProperties.put(index, propertiesList);
        });
    }

    @Override
    public FilterBasicHandler getFilterHandler() {
        return getData(Registration.HANDLER_BASIC_FILTER_ANYSIZE);
    }

    @Override
    public FilterData getFilterData() {
        return filterData;
    }

    @Override
    public BlockEntity getBlockEntity() {
        return this;
    }

    public void setSensorSettings(int senseTarget, boolean strongSignal, int senseAmount, int equality) {
        this.sense_target = SENSE_TARGET.values()[senseTarget];
        setRedstone(emitRedstone, strongSignal); //Gonna wanna update the neighbors if strongSignal changed
        this.senseAmount = senseAmount;
        this.equality = equality;
        positions.clear(); //Clear any clicks we have queue'd up
        blockStateFilterCache.clear();
        markDirtyClient();
    }

    @Override
    public void tickClient() {
    }

    @Override
    public void tickServer() {
        super.tickServer();
        if (newlyLoaded && level != null) {
            for (Direction direction : Direction.values()) {
                level.neighborChanged(getBlockPos().relative(direction), this.getBlockState().getBlock(), getBlockPos());
                level.updateNeighborsAtExceptFromFacing(getBlockPos().relative(direction), this.getBlockState().getBlock(), direction.getOpposite());
            }
            newlyLoaded = false;
        }
        sense();
    }

    public boolean canSense() {
        return true;
    }

    public void setRedstone(boolean emit, boolean strong) {
        if (emit != emitRedstone || strong != strongSignal || newlyLoaded) {
            emitRedstone = emit;
            strongSignal = strong;
            for (Direction direction : Direction.values()) {
                level.neighborChanged(getBlockPos().relative(direction), this.getBlockState().getBlock(), getBlockPos());
                level.updateNeighborsAtExceptFromFacing(getBlockPos().relative(direction), this.getBlockState().getBlock(), direction.getOpposite());
            }
        }
    }

    public boolean checkCount(int found) {
        if (equality == 0) // Greater Than
            return found > senseAmount;
        if (equality == 1) //Less Than
            return found < senseAmount;
        if (equality == 2) //Equals
            return found == senseAmount;
        return found > senseAmount; //Fallback
    }

    public void sense() {
        if (!canRun()) return;
        if (!canSense()) return;
        if ((sense_target.equals(SENSE_TARGET.BLOCK) || sense_target.equals(SENSE_TARGET.AIR))) {
            if (positions.isEmpty())
                positions = findPositions();
            if (positions.isEmpty())
                return;
            Iterator<BlockPos> iterator = positions.iterator();
            int foundMatchingBlocks = 0;
            while (iterator.hasNext()) {
                if (senseBlock(iterator.next()))
                    foundMatchingBlocks++;
                iterator.remove();
            }
            setRedstone(checkCount(foundMatchingBlocks), strongSignal);
        } else {
            List<Entity> entityList = findEntities(getAABB());
            setRedstone(senseEntity(entityList), strongSignal);
        }
    }

    public AABB getAABB() {
        return new AABB(getBlockPos().relative(getBlockState().getValue(BlockStateProperties.FACING)));
    }

    public List<Entity> findEntities(AABB aabb) {
        List<Entity> returnList = new ArrayList<>(level.getEntitiesOfClass(Entity.class, aabb, this::isValidEntity));

        return returnList;
    }

    public boolean isValidEntity(Entity entity) {
        if (sense_target.equals(SENSE_TARGET.HOSTILE) && !(entity instanceof Monster))
            return false;
        if (((sense_target.equals(SENSE_TARGET.PASSIVE)) || (sense_target.equals(SENSE_TARGET.ADULT)) || (sense_target.equals(SENSE_TARGET.CHILD))) && !(entity instanceof Animal))
            return false;
        if (sense_target.equals(SENSE_TARGET.ADULT) && (entity instanceof Animal animal) && (animal.isBaby()))
            return false;
        if (sense_target.equals(SENSE_TARGET.CHILD) && (entity instanceof Animal animal) && !(animal.isBaby()))
            return false;
        if (sense_target.equals(SENSE_TARGET.PLAYER) && !(entity instanceof Player))
            return false;
        if (sense_target.equals(SENSE_TARGET.ITEM) && !(entity instanceof ItemEntity))
            return false;
        if (sense_target.equals(SENSE_TARGET.LIVING) && !(entity instanceof Player) && !(entity instanceof Animal) && !(entity instanceof Monster))
            return false;
        return isEntityValidFilter(entity, this.level);
    }

    public boolean senseEntity(List<Entity> entityList) {
        int entityAmount = entityList.size();
        return checkCount(entityAmount);
    }

    public boolean senseBlock(BlockPos blockPos) {
        BlockState blockState = level.getBlockState(blockPos);
        if (sense_target.equals(SENSE_TARGET.AIR))
            return blockState.isAir();
        if (blockState.isAir()) //Checked above if we're sensing for air, so if its air, false!
            return false;
        if (blockState.getBlock() instanceof LiquidBlock liquidBlock)
            return handleBlockStates(blockPos, blockState, liquidBlock);
        return handleBlockStates(blockPos, blockState);
    }

    public boolean handleBlockStates(BlockPos blockPos, BlockState blockState, LiquidBlock liquidBlock) {
        ItemStack blockItemStack = new ItemStack(liquidBlock.fluid.getBucket());
        boolean allowList = filterData.allowlist;
        if (blockStateFilterCache.containsKey(blockState))
            return blockStateFilterCache.get(blockState);
        boolean returnValue = isStackValidFilter(blockItemStack);
        outerLoop:
        for (Map.Entry<Integer, Map<Property<?>, Comparable<?>>> propertyValues : blockStateProperties.entrySet()) {
            ItemStack filterStack = getFilterHandler().getStackInSlot(propertyValues.getKey());
            if (!ItemStack.isSameItemSameComponents(filterStack, blockItemStack)) //If the itemstack we are comparing in this slot doesn't match the blockItem
                continue;
            for (Map.Entry<Property<?>, Comparable<?>> prop : propertyValues.getValue().entrySet()) {
                Comparable<?> comparable = blockState.getValue(prop.getKey());
                boolean propertyMatch = comparable.equals(prop.getValue());
                if ((allowList && propertyMatch) || (!allowList && !propertyMatch)) {
                    returnValue = true; // Match found in allowlist mode, set return to true
                } else {
                    returnValue = false; // Mismatch found in denylist mode, set return to false and exit
                    break outerLoop;
                }
            }
        }

        blockStateFilterCache.put(blockState, returnValue);
        return blockStateFilterCache.get(blockState);
    }

    public boolean handleBlockStates(BlockPos blockPos, BlockState blockState) {
        ItemStack blockItemStack = blockState.getBlock().getCloneItemStack(level, blockPos, blockState);
        boolean allowList = filterData.allowlist;
        if (blockStateFilterCache.containsKey(blockState))
            return blockStateFilterCache.get(blockState);
        boolean returnValue = isStackValidFilter(blockItemStack);
        outerLoop:
        for (Map.Entry<Integer, Map<Property<?>, Comparable<?>>> propertyValues : blockStateProperties.entrySet()) {
            ItemStack filterStack = getFilterHandler().getStackInSlot(propertyValues.getKey());
            if (!ItemStack.isSameItemSameComponents(filterStack, blockItemStack)) //If the itemstack we are comparing in this slot doesn't match the blockItem
                continue;
            for (Map.Entry<Property<?>, Comparable<?>> prop : propertyValues.getValue().entrySet()) {
                Comparable<?> comparable = blockState.getValue(prop.getKey());
                boolean propertyMatch = comparable.equals(prop.getValue());
                if ((allowList && propertyMatch) || (!allowList && !propertyMatch)) {
                    returnValue = true; // Match found in allowlist mode, set return to true
                } else {
                    returnValue = false; // Mismatch found in denylist mode, set return to false and exit
                    break outerLoop;
                }
            }
        }

        blockStateFilterCache.put(blockState, returnValue);
        return blockStateFilterCache.get(blockState);
    }

    public boolean isBlockPosValid(BlockPos blockPos) {
        return true;
    }

    public List<BlockPos> findPositions() {
        List<BlockPos> returnList = new ArrayList<>();
        BlockPos blockPos = getBlockPos().relative(getBlockState().getValue(BlockStateProperties.FACING));
        if (isBlockPosValid(blockPos))
            returnList.add(blockPos);
        return returnList;
    }

    @Override
    public boolean isDefaultSettings() {
        if (!super.isDefaultSettings())
            return false;
        if (!sense_target.equals(SENSE_TARGET.BLOCK))
            return false;
        if (strongSignal)
            return false;
        if (!blockStateProperties.isEmpty())
            return false;
        if (senseAmount != 0)
            return false;
        if (equality != 0)
            return false;
        return true;
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt("senseTarget", sense_target.ordinal());
        tag.putBoolean("strongSignal", strongSignal);
        tag.put("blockStateProps", saveBlockStateProperties());
        tag.putInt("senseAmount", senseAmount);
        tag.putInt("equality", equality);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        this.sense_target = SENSE_TARGET.values()[tag.getInt("senseTarget")];
        this.strongSignal = tag.getBoolean("strongSignal");
        this.senseAmount = tag.getInt("senseAmount");
        this.equality = tag.getInt("equality");
        super.loadAdditional(tag, provider);
        loadBlockStateProperties(tag.getCompound("blockStateProps")); //Do this after the filter data comes in, so we know the itemstack in the filter
    }
}
