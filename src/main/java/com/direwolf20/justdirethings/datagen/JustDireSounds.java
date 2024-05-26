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
    }
}