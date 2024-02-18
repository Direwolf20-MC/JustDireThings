package com.direwolf20.justdirethings.setup;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.direwolf20.justdirethings.JustDireThings.MODID;

public class ModSetup {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB_JUSTDIRETHINGS = CREATIVE_MODE_TABS.register(MODID, () -> CreativeModeTab.builder()
            .title(Component.literal("Just Dire Things"))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .icon(() -> Items.DIRT.getDefaultInstance()) //Todo Proper Item
            .displayItems((parameters, output) -> {
                Registration.ITEMS.getEntries().forEach(e -> {
                    Item item = e.get();
                    output.accept(item);
                });
            }).build());

}
