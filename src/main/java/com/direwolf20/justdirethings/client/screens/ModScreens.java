package com.direwolf20.justdirethings.client.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

public class ModScreens {
    public static void openMachineSettingsCopierScreen(ItemStack itemstack) {
        Minecraft.getInstance().setScreen(new MachineSettingsCopierScreen(itemstack));
    }
}
