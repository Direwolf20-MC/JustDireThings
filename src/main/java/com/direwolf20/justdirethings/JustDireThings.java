package com.direwolf20.justdirethings;

import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.PoweredMachineBE;
import com.direwolf20.justdirethings.common.network.PacketHandler;
import com.direwolf20.justdirethings.setup.ClientSetup;
import com.direwolf20.justdirethings.setup.Config;
import com.direwolf20.justdirethings.setup.ModSetup;
import com.direwolf20.justdirethings.setup.Registration;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(JustDireThings.MODID)
public class JustDireThings {
    public static final String MODID = "justdirethings";
    private static final Logger LOGGER = LogUtils.getLogger();

    public JustDireThings(IEventBus modEventBus) {
        Registration.init(modEventBus);
        Config.register();

        modEventBus.addListener(ModSetup::init);
        ModSetup.CREATIVE_MODE_TABS.register(modEventBus);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(PacketHandler::registerNetworking);
        if (FMLLoader.getDist().isClient()) {
            modEventBus.addListener(ClientSetup::init);
        }
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.ItemHandler.ITEM, (itemStack, context) -> itemStack.getData(Registration.HANDLER),
                Registration.Pocket_Generator.get(),
                Registration.Pocket_GeneratorT2.get(),
                Registration.Pocket_GeneratorT3.get(),
                Registration.Pocket_GeneratorT4.get()
        );
        event.registerItem(Capabilities.EnergyStorage.ITEM, (itemStack, context) -> itemStack.getData(Registration.ENERGYSTORAGENORECEIVE),
                Registration.Pocket_Generator.get(),
                Registration.Pocket_GeneratorT2.get(),
                Registration.Pocket_GeneratorT3.get(),
                Registration.Pocket_GeneratorT4.get()
        );
        event.registerItem(Capabilities.EnergyStorage.ITEM, (itemStack, context) -> itemStack.getData(Registration.ENERGYSTORAGE),
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
                Registration.EclipseAlloyPaxel.get()
        );
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
                Registration.DropperT1.get()
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
                Registration.SensorT2.get()
        );
    }
}
