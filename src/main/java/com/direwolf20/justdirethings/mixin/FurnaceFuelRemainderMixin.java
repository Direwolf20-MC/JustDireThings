package com.direwolf20.justdirethings.mixin;

import com.direwolf20.justdirethings.common.items.FuelCanister;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceBlockEntity.class)
public class FurnaceFuelRemainderMixin {

    @Inject(method = "consumeFuel", at = @At("HEAD"), cancellable = true)
    private static void justdirethings$fuelCanisterRemainder(NonNullList<ItemStack> items, ItemStack fuel, CallbackInfo ci) {
        if (!(fuel.getItem() instanceof FuelCanister)) return;
        ItemStack remainder = fuel.copy();
        remainder.setCount(1);
        FuelCanister.decrementFuel(remainder);
        items.set(1, remainder);
        ci.cancel();
    }
}
