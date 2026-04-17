package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class JustDireSounds extends SoundDefinitionsProvider {
    public JustDireSounds(PackOutput output) {
        super(output, JustDireThings.MODID);
    }

    @Override
    public void registerSounds() {
        add(Registration.PORTAL_GUN_OPEN.get(), SoundDefinition.definition()
                .with(sound("minecraft:mob/endermen/portal2"))
                .subtitle("sound.justdirethings.portal_gun_open")
        );
        add(Registration.PORTAL_GUN_CLOSE.get(), SoundDefinition.definition()
                .with(sound("minecraft:mob/endermen/portal"))
                .subtitle("sound.justdirethings.portal_gun_close")
        );
        add(Registration.PARADOX_AMBIENT.get(), SoundDefinition.definition()
                .with(sound("justdirethings:paradox_running", SoundDefinition.SoundType.SOUND)
                        .volume(1f)
                        .pitch(1f)
                        .weight(2)
                        .attenuationDistance(16)
                        .stream(true)
                        .preload(true))
                .subtitle("sound.justdirethings.paradox_ambient")
        );
        add(Registration.BEEP.get(), SoundDefinition.definition()
                .with(sound("justdirethings:beep"))
                .subtitle("sound.justdirethings.beep")
        );
    }
}
