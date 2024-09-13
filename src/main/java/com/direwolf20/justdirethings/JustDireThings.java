package com.direwolf20.justdirethings;

import com.direwolf20.justdirethings.common.blockentities.*;
import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.FluidMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.PoweredMachineBE;
import com.direwolf20.justdirethings.common.capabilities.EnergyStorageItemStackNoReceive;
import com.direwolf20.justdirethings.common.capabilities.EnergyStorageItemstack;
import com.direwolf20.justdirethings.common.capabilities.ExperienceHolderFluidTank;
import com.direwolf20.justdirethings.common.containers.handlers.PotionCanisterHandler;
import com.direwolf20.justdirethings.common.entities.DecoyEntity;
import com.direwolf20.justdirethings.common.fluids.xpfluid.XPFluid;
import com.direwolf20.justdirethings.common.items.FluidCanister;
import com.direwolf20.justdirethings.common.items.PortalGunV2;
import com.direwolf20.justdirethings.common.items.TimeWand;
import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.direwolf20.justdirethings.common.items.interfaces.PoweredItem;
import com.direwolf20.justdirethings.common.network.PacketHandler;
import com.direwolf20.justdirethings.setup.ClientSetup;
import com.direwolf20.justdirethings.setup.Config;
import com.direwolf20.justdirethings.setup.ModSetup;
import com.direwolf20.justdirethings.setup.Registration;
import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.world.chunk.RegisterTicketControllersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;
import net.neoforged.neoforge.items.ComponentItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(JustDireThings.MODID)
public class JustDireThings {
    public static final String MODID = "justdirethings";
    private static final Logger LOGGER = LogUtils.getLogger();

    public JustDireThings(IEventBus modEventBus, ModContainer container) {
        Registration.init(modEventBus);
        Config.register(container);

        modEventBus.addListener(ModSetup::init);
        ModSetup.CREATIVE_MODE_TABS.register(modEventBus);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(PacketHandler::registerNetworking);
        modEventBus.addListener(this::registerChunkLoaders);
        modEventBus.addListener(this::registerEntityAttributes);
        modEventBus.addListener(this::registerCustomAttributes);
        if (FMLLoader.getDist().isClient()) {
            modEventBus.addListener(ClientSetup::init);
        }
    }

    private void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(Registration.DecoyEntity.get(),
                DecoyEntity.createAttributes().build());
    }

    private void registerCustomAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER,
                Registration.PHASE);
    }

    private void registerChunkLoaders(RegisterTicketControllersEvent event) {
        event.register(Registration.TICKET_CONTROLLER);
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        //Items
        event.registerItem(Capabilities.ItemHandler.ITEM, (itemStack, context) -> new ComponentItemHandler(itemStack, JustDireDataComponents.ITEMSTACK_HANDLER.get(), 1),
                Registration.Pocket_Generator.get()
        );
        event.registerItem(Capabilities.ItemHandler.ITEM, (itemStack, context) -> new ComponentItemHandler(itemStack, JustDireDataComponents.TOOL_CONTENTS.get(), 1),
                Registration.FerricoreBow.get()
        );
        event.registerItem(Capabilities.ItemHandler.ITEM, (itemStack, context) -> new ComponentItemHandler(itemStack, JustDireDataComponents.TOOL_CONTENTS.get(), 2),
                Registration.BlazegoldBow.get()
        );
        event.registerItem(Capabilities.ItemHandler.ITEM, (itemStack, context) -> new ComponentItemHandler(itemStack, JustDireDataComponents.TOOL_CONTENTS.get(), 3),
                Registration.CelestigemBow.get()
        );
        event.registerItem(Capabilities.ItemHandler.ITEM, (itemStack, context) -> new ComponentItemHandler(itemStack, JustDireDataComponents.TOOL_CONTENTS.get(), 4),
                Registration.EclipseAlloyBow.get()
        );
        event.registerItem(Capabilities.ItemHandler.ITEM, (itemStack, context) -> new PotionCanisterHandler(itemStack, JustDireDataComponents.TOOL_CONTENTS.get(), 1),
                Registration.PotionCanister.get()
        );
        event.registerItem(Capabilities.EnergyStorage.ITEM, (itemStack, context) -> {
                    int capacity = 1000000; //Default
                    if (itemStack.getItem() instanceof PoweredItem poweredItem) {
                        capacity = poweredItem.getMaxEnergy();
                    }
                    return new EnergyStorageItemStackNoReceive(capacity, itemStack);
                },
                Registration.Pocket_Generator.get()
        );
        event.registerItem(Capabilities.EnergyStorage.ITEM, (itemStack, context) -> {
                    int capacity = 1000000; //Default
                    if (itemStack.getItem() instanceof PoweredItem poweredItem) {
                        capacity = poweredItem.getMaxEnergy();
                    }
                    return new EnergyStorageItemstack(capacity, itemStack);
                },
                Registration.CelestigemSword.get(),
                Registration.CelestigemPickaxe.get(),
                Registration.CelestigemAxe.get(),
                Registration.CelestigemShovel.get(),
                Registration.CelestigemHoe.get(),
                Registration.EclipseAlloySword.get(),
                Registration.EclipseAlloyPickaxe.get(),
                Registration.EclipseAlloyAxe.get(),
                Registration.EclipseAlloyShovel.get(),
                Registration.EclipseAlloyHoe.get(),
                Registration.CelestigemPaxel.get(),
                Registration.EclipseAlloyPaxel.get(),
                Registration.VoidshiftWand.get(),
                Registration.EclipsegateWand.get(),
                Registration.PortalGun.get(),
                Registration.PortalGunV2.get(),
                Registration.CelestigemBoots.get(),
                Registration.CelestigemLeggings.get(),
                Registration.CelestigemChestplate.get(),
                Registration.CelestigemHelmet.get(),
                Registration.EclipseAlloyBoots.get(),
                Registration.EclipseAlloyLeggings.get(),
                Registration.EclipseAlloyChestplate.get(),
                Registration.EclipseAlloyHelmet.get(),
                Registration.CelestigemBow.get(),
                Registration.EclipseAlloyBow.get(),
                Registration.TimeWand.get()
        );

        event.registerItem(Capabilities.FluidHandler.ITEM, (itemStack, context) -> {
                    if (itemStack.getItem() instanceof PortalGunV2) {
                        return new FluidHandlerItemStack(JustDireDataComponents.FLUID_CONTAINER, itemStack, PortalGunV2.maxMB) {
                            @Override
                            public boolean isFluidValid(int tank, FluidStack stack) {
                                return stack.is(Registration.PORTAL_FLUID_TYPE.get());
                            }

                            @Override
                            public boolean canFillFluidType(FluidStack fluid) {
                                return fluid.is(Registration.PORTAL_FLUID_TYPE.get());
                            }

                        };
                    }
                    if (itemStack.getItem() instanceof TimeWand timeWand) {
                        return new FluidHandlerItemStack(JustDireDataComponents.FLUID_CONTAINER, itemStack, timeWand.getMaxMB()) {
                            @Override
                            public boolean isFluidValid(int tank, FluidStack stack) {
                                return stack.is(Registration.TIME_FLUID_TYPE.get());
                            }

                            @Override
                            public boolean canFillFluidType(FluidStack fluid) {
                                return fluid.is(Registration.TIME_FLUID_TYPE.get());
                            }

                        };
                    }
                    if (itemStack.getItem() instanceof FluidCanister fluidCanister) {
                        return new FluidHandlerItemStack(JustDireDataComponents.FLUID_CONTAINER, itemStack, fluidCanister.getMaxMB());
                    }
                    return null;
                },
                Registration.PortalGunV2.get(),
                Registration.FluidCanister.get(),
                Registration.TimeWand.get()
        );

        //Blocks
        event.registerBlock(Capabilities.ItemHandler.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof BaseMachineBE)
                        return be.getData(Registration.MACHINE_HANDLER);
                    return null;
                },
                Registration.BlockBreakerT1.get(),
                Registration.BlockBreakerT2.get(),
                Registration.BlockPlacerT1.get(),
                Registration.BlockPlacerT2.get(),
                Registration.ClickerT1.get(),
                Registration.ClickerT2.get(),
                Registration.DropperT1.get(),
                Registration.DropperT2.get(),
                Registration.EnergyTransmitter.get(),
                Registration.FluidPlacerT1.get(),
                Registration.FluidPlacerT2.get(),
                Registration.FluidCollectorT1.get(),
                Registration.FluidCollectorT2.get()
        );
        event.registerBlock(Capabilities.ItemHandler.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof InventoryHolderBE inventoryHolderBE)
                        return inventoryHolderBE.getInventoryHolderHandler();
                    return null;
                },
                Registration.InventoryHolder.get()
        );
        event.registerBlock(Capabilities.ItemHandler.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof BaseMachineBE)
                        return be.getData(Registration.GENERATOR_ITEM_HANDLER);
                    return null;
                },
                Registration.GeneratorT1.get()
        );
        event.registerBlock(Capabilities.ItemHandler.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof BaseMachineBE)
                        return be.getData(Registration.GENERATOR_FLUID_ITEM_HANDLER);
                    return null;
                },
                Registration.GeneratorFluidT1.get()
        );
        event.registerBlock(Capabilities.ItemHandler.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof PlayerAccessorBE playerAccessorBE) {
                        if (be.getLevel().isClientSide) {
                            return new ItemStackHandler(1);
                        } else {
                            return playerAccessorBE.getPlayerHandler(side);
                        }
                    }
                    return null;
                },
                Registration.PlayerAccessor.get()
        );
        event.registerBlock(Capabilities.EnergyStorage.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof PoweredMachineBE)
                        return be.getData(Registration.ENERGYSTORAGE_MACHINES);
                    return null;
                },
                Registration.BlockBreakerT2.get(),
                Registration.BlockPlacerT2.get(),
                Registration.ClickerT2.get(),
                Registration.SensorT2.get(),
                Registration.DropperT2.get(),
                Registration.BlockSwapperT2.get(),
                Registration.FluidPlacerT2.get(),
                Registration.FluidCollectorT2.get(),
                Registration.ParadoxMachine.get()
        );
        event.registerBlock(Capabilities.EnergyStorage.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof PoweredMachineBE)
                        return be.getData(Registration.ENERGYSTORAGE_GENERATORS);
                    return null;
                },
                Registration.GeneratorT1.get(),
                Registration.GeneratorFluidT1.get()
        );
        event.registerBlock(Capabilities.EnergyStorage.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof EnergyTransmitterBE && side != null && side.equals(state.getValue(BlockStateProperties.FACING))) {
                        return be.getData(Registration.ENERGYSTORAGE_TRANSMITTERS);
                    }
                    return null;
                },
                Registration.EnergyTransmitter.get()
        );
        event.registerBlock(Capabilities.FluidHandler.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof FluidMachineBE) {
                        return be.getData(Registration.MACHINE_FLUID_HANDLER);
                    }
                    return null;
                },
                Registration.FluidPlacerT1.get(),
                Registration.FluidPlacerT2.get(),
                Registration.FluidCollectorT1.get(),
                Registration.FluidCollectorT2.get()
        );
        event.registerBlock(Capabilities.FluidHandler.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof FluidMachineBE) {
                        return be.getData(Registration.GENERATOR_FLUID_HANDLER);
                    }
                    return null;
                },
                Registration.GeneratorFluidT1.get()
        );
        event.registerBlock(Capabilities.FluidHandler.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof ParadoxMachineBE) {
                        return be.getData(Registration.PARADOX_FLUID_HANDLER);
                    }
                    return null;
                },
                Registration.ParadoxMachine.get()
        );
        event.registerBlock(Capabilities.FluidHandler.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof ExperienceHolderBE experienceHolderBE) {
                        return new ExperienceHolderFluidTank(experienceHolderBE, fluidstack -> fluidstack.getFluid() instanceof XPFluid); //TODO Tags?
                    }
                    return null;
                },
                Registration.ExperienceHolder.get()
        );
    }
}
