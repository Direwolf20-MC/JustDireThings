package com.direwolf20.justdirethings.client;


import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.items.tools.utils.ToggleableTool;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.IKeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class KeyBindings {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeyBindings.class);
    private static final KeyConflictContextGadget CONFLICT_CONTEXT_GADGET = new KeyConflictContextGadget();

    private static final List<KeyMapping> keyMappings = new ArrayList<>();

    public static KeyMapping toggleTool = createBinding("toggle_tool", GLFW.GLFW_KEY_V);
    /*public static KeyMapping undo = createBinding("undo", GLFW.GLFW_KEY_U);
    public static KeyMapping anchor = createBinding("anchor", GLFW.GLFW_KEY_H);
    public static KeyMapping range = createBinding("range", GLFW.GLFW_KEY_R);*/

    private static KeyMapping createBinding(String name, int key) {
        KeyMapping keyBinding = new KeyMapping(getKey(name), CONFLICT_CONTEXT_GADGET, InputConstants.Type.KEYSYM.getOrCreate(key), getKey("category"));
        keyMappings.add(keyBinding);
        return keyBinding;
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        LOGGER.debug("Registering {} keybinding for {}", keyMappings.size(), JustDireThings.MODID);
        keyMappings.forEach(event::register);
    }

    private static String getKey(String name) {
        return String.join(".", JustDireThings.MODID, "key", name);
    }

    public static void onClientInput(InputEvent.Key event) {
        /*if (menuSettings.consumeClick()) {
            PacketHandler.sendToServer(new GadgetModeSwitchPacket(new ResourceLocation("x:x"), true));
        }*/
    }

    public static class KeyConflictContextGadget implements IKeyConflictContext {
        @Override
        public boolean isActive() {
            Player player = Minecraft.getInstance().player;
            return !KeyConflictContext.GUI.isActive() && player != null
                    && (!ToggleableTool.getToggleableTool(player).isEmpty());
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return other == this || other == KeyConflictContext.IN_GAME;
        }
    }
}
