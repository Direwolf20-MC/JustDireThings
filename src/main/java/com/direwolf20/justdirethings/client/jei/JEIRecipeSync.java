package com.direwolf20.justdirethings.client.jei;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

/**
 * JEI in 26.1 cannot look up our custom recipe types via {@code Level#getRecipeManager()} because
 * the vanilla recipe manager is server-only — {@link net.minecraft.world.item.crafting.RecipeAccess}
 * on the client only exposes property sets and stonecutter recipes. NeoForge 26.1 instead routes
 * mod-declared recipe types through an opt-in sync driven by {@link OnDatapackSyncEvent#sendRecipes}
 * on the server; the client receives them as a {@link RecipeMap} via {@link RecipesReceivedEvent}.
 * <p>
 * JEI waits for {@link RecipesReceivedEvent} before firing {@code registerRecipes(...)} on its plugins,
 * so by the time {@link JEIIntegration#registerRecipes} runs, {@link #CLIENT_RECIPES} is populated.
 */
@EventBusSubscriber(modid = JustDireThings.MODID)
public class JEIRecipeSync {
    /**
     * The most recent {@link RecipeMap} received from the server — only populated on the client.
     */
    public static volatile RecipeMap CLIENT_RECIPES = RecipeMap.EMPTY;

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        event.sendRecipes(
                Registration.GOO_SPREAD_RECIPE_TYPE.get(),
                Registration.GOO_SPREAD_RECIPE_TYPE_TAG.get(),
                Registration.FLUID_DROP_RECIPE_TYPE.get()
        );
    }

    @SubscribeEvent
    public static void onRecipesReceived(RecipesReceivedEvent event) {
        CLIENT_RECIPES = event.getRecipeMap();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends net.minecraft.world.item.crafting.Recipe<?>>
    java.util.Collection<net.minecraft.world.item.crafting.RecipeHolder<T>> byType(RecipeType<T> type) {
        return (java.util.Collection<net.minecraft.world.item.crafting.RecipeHolder<T>>)
                (java.util.Collection) CLIENT_RECIPES.byType((RecipeType) type);
    }
}
