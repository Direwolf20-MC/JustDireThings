package com.direwolf20.justdirethings;

import com.direwolf20.justdirethings.common.blockentities.*;
import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.FluidMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.PoweredMachineBE;
import com.direwolf20.justdirethings.common.capabilities.EnergyStorageItemStackNoReceive;
import com.direwolf20.justdirethings.common.capabilities.ExperienceHolderFluidTank;
import com.direwolf20.justdirethings.common.capabilities.FluidHandlerItemStack;
import com.direwolf20.justdirethings.common.containers.handlers.PotionCanisterHandler;
import com.direwolf20.justdirethings.common.entities.DecoyEntity;
import com.direwolf20.justdirethings.common.items.*;
import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.direwolf20.justdirethings.common.items.interfaces.PoweredItem;
import com.direwolf20.justdirethings.common.network.PacketHandler;
import com.direwolf20.justdirethings.setup.ClientSetup;
import com.direwolf20.justdirethings.setup.Config;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import com.direwolf20.justdirethings.setup.ModSetup;
import com.direwolf20.justdirethings.util.ModTags;
import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.world.chunk.RegisterTicketControllersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.transfer.EmptyResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.ItemAccessEnergyHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemAccessItemHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(JustDireThings.MODID)
public class JustDireThings {
    public static final String MODID = "justdirethings";
    private static final Logger LOGGER = LogUtils.getLogger();

    public JustDireThings(IEventBus modEventBus, ModContainer container) {
        JDTRegistration.init(modEventBus);
        Config.register(container);

        modEventBus.addListener(ModSetup::init);
        ModSetup.CREATIVE_MODE_TABS.register(modEventBus);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(PacketHandler::registerNetworking);
        modEventBus.addListener(this::registerChunkLoaders);
        modEventBus.addListener(this::registerEntityAttributes);
        modEventBus.addListener(this::registerCustomAttributes);
        if (FMLEnvironment.getDist().isClient()) {
            modEventBus.addListener(ClientSetup::init);
        }
    }

    private void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(JDTRegistration.DecoyEntity.get(),
                DecoyEntity.createAttributes().build());
    }

    private void registerCustomAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER,
                JDTRegistration.PHASE);
    }

    private void registerChunkLoaders(RegisterTicketControllersEvent event) {
        event.register(JDTRegistration.TICKET_CONTROLLER);
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        //Items
        event.registerItem(Capabilities.Item.ITEM, (itemStack, context) -> new ItemAccessItemHandler(
                        context != null ? context : ItemAccess.forStack(itemStack),
                        JustDireDataComponents.ITEMSTACK_HANDLER.get(), 1),
                JDTRegistration.Pocket_Generator.get()
        );
        event.registerItem(Capabilities.Item.ITEM, (itemStack, context) -> new ItemAccessItemHandler(
                        context != null ? context : ItemAccess.forStack(itemStack),
                        JustDireDataComponents.TOOL_CONTENTS.get(), 1),
                JDTRegistration.FerricoreBow.get()
        );
        event.registerItem(Capabilities.Item.ITEM, (itemStack, context) -> new ItemAccessItemHandler(
                        context != null ? context : ItemAccess.forStack(itemStack),
                        JustDireDataComponents.TOOL_CONTENTS.get(), 2),
                JDTRegistration.BlazegoldBow.get()
        );
        event.registerItem(Capabilities.Item.ITEM, (itemStack, context) -> new ItemAccessItemHandler(
                        context != null ? context : ItemAccess.forStack(itemStack),
                        JustDireDataComponents.TOOL_CONTENTS.get(), 3),
                JDTRegistration.CelestigemBow.get()
        );
        event.registerItem(Capabilities.Item.ITEM, (itemStack, context) -> new ItemAccessItemHandler(
                        context != null ? context : ItemAccess.forStack(itemStack),
                        JustDireDataComponents.TOOL_CONTENTS.get(), 4),
                JDTRegistration.EclipseAlloyBow.get()
        );
        event.registerItem(Capabilities.Item.ITEM, (itemStack, context) -> new PotionCanisterHandler(
                        context != null ? context : ItemAccess.forStack(itemStack),
                        JustDireDataComponents.TOOL_CONTENTS.get(), 1),
                JDTRegistration.PotionCanister.get()
        );
        // Pocket Generator: external insert blocked (maxInsert=0), internal fill handled via its own BE logic (forceReceiveEnergy). See TRANSFER_API §3 "one-way energy".
        event.registerItem(Capabilities.Energy.ITEM, (itemStack, access) -> {
                    int capacity = 1000000; //Default
                    if (itemStack.getItem() instanceof PoweredItem poweredItem) {
                        capacity = poweredItem.getMaxEnergy();
                    }
                    return new EnergyStorageItemStackNoReceive(
                            access != null ? access : ItemAccess.forStack(itemStack),
                            capacity);
                },
                JDTRegistration.Pocket_Generator.get()
        );
        event.registerItem(Capabilities.Energy.ITEM, (itemStack, access) -> {
                    int capacity = 1000000; //Default
                    if (itemStack.getItem() instanceof PoweredItem poweredItem) {
                        capacity = poweredItem.getMaxEnergy();
                    }
                    return new ItemAccessEnergyHandler(
                            access != null ? access : ItemAccess.forStack(itemStack),
                            JustDireDataComponents.FORGE_ENERGY.get(),
                            capacity);
                },
                JDTRegistration.CelestigemSword.get(),
                JDTRegistration.CelestigemPickaxe.get(),
                JDTRegistration.CelestigemAxe.get(),
                JDTRegistration.CelestigemShovel.get(),
                JDTRegistration.CelestigemHoe.get(),
                JDTRegistration.EclipseAlloySword.get(),
                JDTRegistration.EclipseAlloyPickaxe.get(),
                JDTRegistration.EclipseAlloyAxe.get(),
                JDTRegistration.EclipseAlloyShovel.get(),
                JDTRegistration.EclipseAlloyHoe.get(),
                JDTRegistration.CelestigemPaxel.get(),
                JDTRegistration.EclipseAlloyPaxel.get(),
                JDTRegistration.VoidshiftWand.get(),
                JDTRegistration.EclipsegateWand.get(),
                JDTRegistration.PortalGun.get(),
                JDTRegistration.PortalGunV2.get(),
                JDTRegistration.CelestigemBoots.get(),
                JDTRegistration.CelestigemLeggings.get(),
                JDTRegistration.CelestigemChestplate.get(),
                JDTRegistration.CelestigemHelmet.get(),
                JDTRegistration.EclipseAlloyBoots.get(),
                JDTRegistration.EclipseAlloyLeggings.get(),
                JDTRegistration.EclipseAlloyChestplate.get(),
                JDTRegistration.EclipseAlloyHelmet.get(),
                JDTRegistration.CelestigemBow.get(),
                JDTRegistration.EclipseAlloyBow.get(),
                JDTRegistration.TimeWand.get(),
                JDTRegistration.PolymorphicWandV2.get()
        );

        event.registerItem(Capabilities.Fluid.ITEM, (itemStack, access) -> {
                    ItemAccess ia = access != null ? access : ItemAccess.forStack(itemStack);
                    if (itemStack.getItem() instanceof PortalGunV2) {
                        return new FluidHandlerItemStack(ia, JustDireDataComponents.FLUID_CONTAINER.get(), PortalGunV2.maxMB) {
                            @Override
                            public boolean isFluidValid(FluidResource resource) {
                                return resource.is(JDTRegistration.PORTAL_FLUID_TYPE.get());
                            }
                        };
                    }
                    if (itemStack.getItem() instanceof TimeWand timeWand) {
                        return new FluidHandlerItemStack(ia, JustDireDataComponents.FLUID_CONTAINER.get(), timeWand.getMaxMB()) {
                            @Override
                            public boolean isFluidValid(FluidResource resource) {
                                return resource.is(JDTRegistration.TIME_FLUID_TYPE.get());
                            }
                        };
                    }
                    if (itemStack.getItem() instanceof PolymorphicWand polymorphicWand) {
                        return new FluidHandlerItemStack(ia, JustDireDataComponents.FLUID_CONTAINER.get(), polymorphicWand.getMaxMB()) {
                            @Override
                            public boolean isFluidValid(FluidResource resource) {
                                return resource.is(JDTRegistration.POLYMORPHIC_FLUID_TYPE.get());
                            }
                        };
                    }
                    if (itemStack.getItem() instanceof PolymorphicWandV2 polymorphicWandv2) {
                        return new FluidHandlerItemStack(ia, JustDireDataComponents.FLUID_CONTAINER.get(), polymorphicWandv2.getMaxMB()) {
                            @Override
                            public boolean isFluidValid(FluidResource resource) {
                                return resource.is(JDTRegistration.POLYMORPHIC_FLUID_TYPE.get());
                            }
                        };
                    }
                    if (itemStack.getItem() instanceof FluidCanister fluidCanister) {
                        return new FluidHandlerItemStack(ia, JustDireDataComponents.FLUID_CONTAINER.get(), fluidCanister.getMaxMB());
                    }
                    return null;
                },
                JDTRegistration.PortalGunV2.get(),
                JDTRegistration.FluidCanister.get(),
                JDTRegistration.TimeWand.get(),
                JDTRegistration.PolymorphicWand.get(),
                JDTRegistration.PolymorphicWandV2.get()
        );

        //Blocks
        event.registerBlock(Capabilities.Item.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof BaseMachineBE)
                        return be.getData(JDTRegistration.MACHINE_HANDLER);
                    return null;
                },
                JDTRegistration.BlockBreakerT1.get(),
                JDTRegistration.BlockBreakerT2.get(),
                JDTRegistration.BlockPlacerT1.get(),
                JDTRegistration.BlockPlacerT2.get(),
                JDTRegistration.ClickerT1.get(),
                JDTRegistration.ClickerT2.get(),
                JDTRegistration.DropperT1.get(),
                JDTRegistration.DropperT2.get(),
                JDTRegistration.EnergyTransmitter.get(),
                JDTRegistration.FluidPlacerT1.get(),
                JDTRegistration.FluidPlacerT2.get(),
                JDTRegistration.FluidCollectorT1.get(),
                JDTRegistration.FluidCollectorT2.get()
        );
        event.registerBlock(Capabilities.Item.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof InventoryHolderBE inventoryHolderBE)
                        return inventoryHolderBE.getInventoryHolderHandler();
                    return null;
                },
                JDTRegistration.InventoryHolder.get()
        );
        event.registerBlock(Capabilities.Item.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof BaseMachineBE)
                        return be.getData(JDTRegistration.GENERATOR_ITEM_HANDLER);
                    return null;
                },
                JDTRegistration.GeneratorT1.get()
        );
        event.registerBlock(Capabilities.Item.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof BaseMachineBE)
                        return be.getData(JDTRegistration.GENERATOR_FLUID_ITEM_HANDLER);
                    return null;
                },
                JDTRegistration.GeneratorFluidT1.get()
        );
        event.registerBlock(Capabilities.Item.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof PlayerAccessorBE playerAccessorBE) {
                        if (be.getLevel().isClientSide()) {
                            return EmptyResourceHandler.<ItemResource>instance();
                        } else {
                            return playerAccessorBE.getPlayerHandler(side);
                        }
                    }
                    return null;
                },
                JDTRegistration.PlayerAccessor.get()
        );
        event.registerBlock(Capabilities.Energy.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof PoweredMachineBE)
                        return be.getData(JDTRegistration.ENERGYSTORAGE_MACHINES);
                    return null;
                },
                JDTRegistration.BlockBreakerT2.get(),
                JDTRegistration.BlockPlacerT2.get(),
                JDTRegistration.ClickerT2.get(),
                JDTRegistration.SensorT2.get(),
                JDTRegistration.DropperT2.get(),
                JDTRegistration.BlockSwapperT2.get(),
                JDTRegistration.FluidPlacerT2.get(),
                JDTRegistration.FluidCollectorT2.get(),
                JDTRegistration.ParadoxMachine.get()
        );
        event.registerBlock(Capabilities.Energy.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof PoweredMachineBE)
                        return be.getData(JDTRegistration.ENERGYSTORAGE_GENERATORS);
                    return null;
                },
                JDTRegistration.GeneratorT1.get(),
                JDTRegistration.GeneratorFluidT1.get()
        );
        event.registerBlock(Capabilities.Energy.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof EnergyTransmitterBE && side != null && side.equals(state.getValue(BlockStateProperties.FACING))) {
                        return be.getData(JDTRegistration.ENERGYSTORAGE_TRANSMITTERS);
                    }
                    return null;
                },
                JDTRegistration.EnergyTransmitter.get()
        );
        event.registerBlock(Capabilities.Fluid.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof FluidMachineBE) {
                        return be.getData(JDTRegistration.MACHINE_FLUID_HANDLER);
                    }
                    return null;
                },
                JDTRegistration.FluidPlacerT1.get(),
                JDTRegistration.FluidPlacerT2.get(),
                JDTRegistration.FluidCollectorT1.get(),
                JDTRegistration.FluidCollectorT2.get()
        );
        event.registerBlock(Capabilities.Fluid.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof FluidMachineBE) {
                        return be.getData(JDTRegistration.GENERATOR_FLUID_HANDLER);
                    }
                    return null;
                },
                JDTRegistration.GeneratorFluidT1.get()
        );
        event.registerBlock(Capabilities.Fluid.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof ParadoxMachineBE) {
                        return be.getData(JDTRegistration.PARADOX_FLUID_HANDLER);
                    }
                    return null;
                },
                JDTRegistration.ParadoxMachine.get()
        );
        event.registerBlock(Capabilities.Fluid.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof ExperienceHolderBE experienceHolderBE) {
                        return new ExperienceHolderFluidTank(experienceHolderBE, fluidResource -> fluidResource.is(ModTags.Fluids.EXPERIENCE));
                    }
                    return null;
                },
                JDTRegistration.ExperienceHolder.get()
        );
    }
}
