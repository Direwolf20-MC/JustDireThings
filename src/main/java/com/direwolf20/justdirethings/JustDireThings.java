package com.direwolf20.justdirethings;

import com.direwolf20.justdirethings.common.blockentities.EnergyTransmitterBE;
import com.direwolf20.justdirethings.common.blockentities.PlayerAccessorBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.PoweredMachineBE;
import com.direwolf20.justdirethings.common.capabilities.EnergyStorageItemStackNoReceive;
import com.direwolf20.justdirethings.common.capabilities.EnergyStorageItemstack;
import com.direwolf20.justdirethings.common.containers.handlers.DataComponentHandler;
import com.direwolf20.justdirethings.common.items.FluidCanister;
import com.direwolf20.justdirethings.common.items.PortalGunV2;
import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.direwolf20.justdirethings.common.items.interfaces.PoweredItem;
import com.direwolf20.justdirethings.common.items.interfaces.PoweredTool;
import com.direwolf20.justdirethings.common.network.PacketHandler;
import com.direwolf20.justdirethings.setup.ClientSetup;
import com.direwolf20.justdirethings.setup.Config;
import com.direwolf20.justdirethings.setup.ModSetup;
import com.direwolf20.justdirethings.setup.Registration;
import com.mojang.logging.LogUtils;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.world.chunk.RegisterTicketControllersEvent;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;
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
        if (FMLLoader.getDist().isClient()) {
            modEventBus.addListener(ClientSetup::init);
        }
    }

    private void registerChunkLoaders(RegisterTicketControllersEvent event) {
        event.register(Registration.TICKET_CONTROLLER);
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        //Items
        event.registerItem(Capabilities.ItemHandler.ITEM, (itemStack, context) -> new DataComponentHandler(itemStack, 1),
                Registration.Pocket_Generator.get()
        );
        event.registerItem(Capabilities.EnergyStorage.ITEM, (itemStack, context) -> {
                    int capacity = 1000000; //Default
                    if (itemStack.getItem() instanceof PoweredTool poweredTool) {
                        capacity = poweredTool.getMaxEnergy();
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
                Registration.EclipsegateWand.get()
        );

        event.registerItem(Capabilities.FluidHandler.ITEM, (itemStack, context) -> {
                    if (itemStack.getItem() instanceof PortalGunV2) {
                        return new FluidHandlerItemStack(JustDireDataComponents.FLUID_CONTAINER, itemStack, PortalGunV2.maxMB);
                    }
                    if (itemStack.getItem() instanceof FluidCanister fluidCanister) {
                        return new FluidHandlerItemStack(JustDireDataComponents.FLUID_CONTAINER, itemStack, fluidCanister.getMaxMB());
                    }
                    return null;
                },
                Registration.PortalGunV2.get(),
                Registration.FluidCanister.get()
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
                Registration.GeneratorT1.get(),
                Registration.EnergyTransmitter.get()
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
                Registration.BlockSwapperT2.get()
        );
        event.registerBlock(Capabilities.EnergyStorage.BLOCK,
                (level, pos, state, be, side) -> {
                    if (be instanceof PoweredMachineBE)
                        return be.getData(Registration.ENERGYSTORAGE_GENERATORS);
                    return null;
                },
                Registration.GeneratorT1.get()
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
    }
}
