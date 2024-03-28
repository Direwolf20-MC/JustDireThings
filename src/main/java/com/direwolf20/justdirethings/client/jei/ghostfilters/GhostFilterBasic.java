package com.direwolf20.justdirethings.client.jei.ghostfilters;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseScreen;
import com.direwolf20.justdirethings.common.containers.slots.FilterBasicSlot;
import com.direwolf20.justdirethings.common.network.data.GhostSlotPayload;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class GhostFilterBasic implements IGhostIngredientHandler<BaseScreen> {
    @Override
    public <I> List<Target<I>> getTargetsTyped(BaseScreen gui, ITypedIngredient<I> ingredient, boolean doStart) {
        List<Target<I>> targets = new ArrayList<>();

        for (Slot slot : gui.getMenu().slots) {
            Rect2i bounds = new Rect2i(gui.getGuiLeft() + slot.x, gui.getGuiTop() + slot.y, 16, 16);

            if (ingredient.getIngredient() instanceof ItemStack && (slot instanceof FilterBasicSlot)) {
                targets.add(new Target<I>() {
                    @Override
                    public Rect2i getArea() {
                        return bounds;
                    }

                    @Override
                    public void accept(I ingredient) {
                        slot.set((ItemStack) ingredient);
                        PacketDistributor.SERVER.noArg().send(new GhostSlotPayload(slot.index, (ItemStack) ingredient, ((ItemStack) ingredient).getCount(), -1));
                    }
                });
            }
        }
        return targets;
    }

    @Override
    public void onComplete() {
        // NO OP
    }
}