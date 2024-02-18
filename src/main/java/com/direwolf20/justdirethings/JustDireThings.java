package com.direwolf20.justdirethings;

import com.direwolf20.justdirethings.setup.ClientSetup;
import com.direwolf20.justdirethings.setup.Config;
import com.direwolf20.justdirethings.setup.ModSetup;
import com.direwolf20.justdirethings.setup.Registration;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(JustDireThings.MODID)
public class JustDireThings {
    public static final String MODID = "justdirethings";
    private static final Logger LOGGER = LogUtils.getLogger();

    public JustDireThings(IEventBus modEventBus) {

        Registration.init(modEventBus);
        Config.register();
        ModSetup.CREATIVE_MODE_TABS.register(modEventBus);

        if (FMLLoader.getDist().isClient()) {
            modEventBus.addListener(ClientSetup::init);
        }
    }
}
