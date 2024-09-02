package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class JustDireSounds extends SoundDefinitionsProvider {
    public JustDireSounds(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, JustDireThings.MODID, existingFileHelper);
    }

    @Override
    public void registerSounds() {
        add(Registration.PORTAL_GUN_OPEN, SoundDefinition.definition()
                .with(sound("minecraft:mob/endermen/portal2"))
                .subtitle("sound.justdirethings.portal_gun_open")
        );
        add(Registration.PORTAL_GUN_CLOSE, SoundDefinition.definition()
                .with(sound("minecraft:mob/endermen/portal"))
                .subtitle("sound.justdirethings.portal_gun_close")
        );
        add(Registration.PARADOX_AMBIENT, SoundDefinition.definition()
                .with(sound("justdirethings:paradox_running", SoundDefinition.SoundType.SOUND)
                        // Sets the volume. Also has a double counterpart.
                        .volume(1f)
                        // Sets the pitch. Also has a double counterpart.
                        .pitch(1f)
                        // Sets the weight.
                        .weight(2)
                        // Sets the attenuation distance.
                        .attenuationDistance(16)
                        // Enables streaming.
                        // Also has a parameterless overload that defers to stream(true).
                        .stream(true)
                        // Enables preloading.
                        // Also has a parameterless overload that defers to preload(true).
                        .preload(true))
                .subtitle("sound.justdirethings.paradox_ambient")
        );
    }
}