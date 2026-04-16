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
import com.direwolf20.justdirethings.datagen.JustDireFluidTags;
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
        Registration.init(modEventBus);
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
        event.registerItem(Capabilities.Item.ITEM, (itemStack, context) -> new ItemAccessItemHandler(
                        context != null ? context : ItemAccess.forStack(itemStack),
                        JustDireDataComponents.ITEMSTACK_HANDLER.get(), 1),
                Registration.Pocket_Generator.get()
        );
        event.registerItem(Capabilities.Item.ITEM, (itemStack, context) -> new ItemAccessItemHandler(
                        context != null ? context : ItemAccess.forStack(itemStack),
                        JustDireDataComponents.TOOL_CONTENTS.get(), 1),
                Registration.FerricoreBow.get()
        );
        event.registerItem(Capabilities.Item.ITEM, (itemStack, context) -> new ItemAccessItemHandler(
                        context != null ? context : ItemAccess.forStack(itemStack),
                        JustDireDataComponents.TOOL_CONTENTS.get(), 2),
                Registration.BlazegoldBow.get()
        );
        event.registerItem(Capabilities.Item.ITEM, (itemStack, context) -> new ItemAccessItemHandler(
                        context != null ? context : ItemAccess.forStack(itemStack),
                        JustDireDataComponents.TOOL_CONTENTS.get(), 3),
                Registration.CelestigemBow.get()
        );
        event.registerItem(Capabilities.Item.ITEM, (itemStack, context) -> new ItemAccessItemHandler(
                        context != null ? context : ItemAccess.forStack(itemStack),
                        JustDireDataComponents.TOOL_CONTENTS.get(), 4),
                Registration.EclipseAlloyBow.get()
        );
        event.registerItem(Capabilities.Item.ITEM, (itemStack, context) -> new PotionCanisterHandler(
                        context != null ? context : ItemAccess.forStack(itemStack),
                        JustDireDataComponents.TOOL_CONTENTS.get(), 1),
                Registration.PotionCanister.get()
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
                Registration.Pocket_Generator.get()
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
                Registration.TimeWand.get(),
                Registration.PolymorphicWandV2.get()
        );

        event.registerItem(Capabilities.Fluid.ITEM, (itemStack, access) -> {
                    ItemAccess ia = access != null ? access : ItemAccess.forStack(itemStack);
                    if (itemStack.getItem() instanceof PortalGunV2) {
                        return new FluidHandlerItemStack(ia, JustDireDataComponents.FLUID_CONTAINER.get(), PortalGunV2.maxMB) {
                            @Override
                            public boolean isFluidValid(FluidResource resource) {
                                return resource.is(Registration.PORTAL_FLUID_TYPE.get());
                            }
                        };
                    }
                    if (itemStack.getItem() instanceof TimeWand timeWand) {
                        return new FluidHandlerItemStack(ia, JustDireDataComponents.FLUID_CONTAINER.get(), timeWand.getMaxMB()) {
                            @Override
                            public boolean isFluidValid(FluidResource resource) {
                                return resource.is(Registration.TIME_FLUID_TYPE.get());
                            }
                        };
                    }
                    if (itemStack.getItem() instanceof PolymorphicWand polymorphicWand) {
                        return new FluidHandlerItemStack(ia, JustDireDataComponents.FLUID_CONTAINER.get(), polymorphicWand.getMaxMB()) {
                            @Override
                            public boolean isFluidValid(FluidResource resource) {
                                return resource.is(Registration.POLYMORPHIC_FLUID_TYPE.get());
                            }
                        };
                    }
                    if (itemStack.getItem() instanceof PolymorphicWandV2 polymorphicWandv2) {
                        return new FluidHandlerItemStack(ia, JustDireDataComponents.FLUID_CONTAINER.get(), polymorphicWandv2.getMaxMB()) {
                            @Override
                            public boolean isFluidValid(FluidResource resource) {
                                return resource.is(Registration.POLYMORPHIC_FLUID_TYPE.get());
                            }
                        };
                    }
                    if (itemStack.getItem() instanceof FluidCanister fluidCanister) {
                        return new FluidHandlerItemStack(ia, JustDireDataComponents.FLUID_CONTAINER.get(), fluidCanister.getMaxMB());
                    }
                    return null;
                },
                Registration.PortalGunV2.get(),
                Registration.FluidCanister.get(),
                Registration.TimeWand.get(),
                Registration.PolymorphicWand.get(),
                Registration.PolymorphicWandV2.get()
        );

        //Blocks
        event.registerBlock(Capabilities.Item.BLOCK,
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
        event.registerBlock(Capabilities.Item.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof InventoryHolderBE inventoryHolderBE)
                        return inventoryHolderBE.getInventoryHolderHandler();
                    return null;
                },
                Registration.InventoryHolder.get()
        );
        event.registerBlock(Capabilities.Item.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof BaseMachineBE)
                        return be.getData(Registration.GENERATOR_ITEM_HANDLER);
                    return null;
                },
                Registration.GeneratorT1.get()
        );
        event.registerBlock(Capabilities.Item.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof BaseMachineBE)
                        return be.getData(Registration.GENERATOR_FLUID_ITEM_HANDLER);
                    return null;
                },
                Registration.GeneratorFluidT1.get()
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
                Registration.PlayerAccessor.get()
        );
        event.registerBlock(Capabilities.Energy.BLOCK,
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
        event.registerBlock(Capabilities.Energy.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof PoweredMachineBE)
                        return be.getData(Registration.ENERGYSTORAGE_GENERATORS);
                    return null;
                },
                Registration.GeneratorT1.get(),
                Registration.GeneratorFluidT1.get()
        );
        event.registerBlock(Capabilities.Energy.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof EnergyTransmitterBE && side != null && side.equals(state.getValue(BlockStateProperties.FACING))) {
                        return be.getData(Registration.ENERGYSTORAGE_TRANSMITTERS);
                    }
                    return null;
                },
                Registration.EnergyTransmitter.get()
        );
        event.registerBlock(Capabilities.Fluid.BLOCK,
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
        event.registerBlock(Capabilities.Fluid.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof FluidMachineBE) {
                        return be.getData(Registration.GENERATOR_FLUID_HANDLER);
                    }
                    return null;
                },
                Registration.GeneratorFluidT1.get()
        );
        event.registerBlock(Capabilities.Fluid.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof ParadoxMachineBE) {
                        return be.getData(Registration.PARADOX_FLUID_HANDLER);
                    }
                    return null;
                },
                Registration.ParadoxMachine.get()
        );
        event.registerBlock(Capabilities.Fluid.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof ExperienceHolderBE experienceHolderBE) {
                        return new ExperienceHolderFluidTank(experienceHolderBE, fluidResource -> fluidResource.is(JustDireFluidTags.EXPERIENCE));
                    }
                    return null;
                },
                Registration.ExperienceHolder.get()
        );
    }
}
