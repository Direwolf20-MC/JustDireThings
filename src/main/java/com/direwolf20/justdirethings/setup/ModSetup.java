package com.direwolf20.justdirethings.setup;

import com.direwolf20.justdirethings.common.events.BlockEvents;
import com.direwolf20.justdirethings.common.events.EntityEvents;
import com.direwolf20.justdirethings.common.events.LivingEntityEvents;
import com.direwolf20.justdirethings.common.events.PlayerEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.direwolf20.justdirethings.JustDireThings.MODID;

public class ModSetup {
    public static void init(final FMLCommonSetupEvent event) {
        NeoForge.EVENT_BUS.register(BlockEvents.class);
        NeoForge.EVENT_BUS.register(EntityEvents.class);
        NeoForge.EVENT_BUS.register(LivingEntityEvents.class);
        NeoForge.EVENT_BUS.register(PlayerEvents.class);
    }

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB_JUSTDIRETHINGS = CREATIVE_MODE_TABS.register(MODID, () -> CreativeModeTab.builder()
            .title(Component.literal("Just Dire Things"))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .icon(() -> new ItemStack(JDTRegistration.Fuel_Canister.get()))
            .displayItems((parameters, output) -> {
                JDTRegistration.ITEMS.getEntries().forEach(e -> {
                    Item item = e.get();
                    output.accept(item);
                });
                JDTRegistration.BUCKET_ITEMS.getEntries().forEach(e -> {
                    Item item = e.get();
                    output.accept(item);
                });
                JDTRegistration.TOOLS.getEntries().forEach(e -> {
                    Item item = e.get();
                    output.accept(item);
                });
                JDTRegistration.BOWS.getEntries().forEach(e -> {
                    Item item = e.get();
                    output.accept(item);
                });
                JDTRegistration.ARMORS.getEntries().forEach(e -> {
                    Item item = e.get();
                    output.accept(item);
                });
                JDTRegistration.UPGRADES.getEntries().forEach(e -> {
                    Item item = e.get();
                    output.accept(item);
                });
            }).build());

}
