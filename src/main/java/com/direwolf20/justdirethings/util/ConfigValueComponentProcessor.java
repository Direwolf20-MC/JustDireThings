package com.direwolf20.justdirethings.util;

import com.direwolf20.justdirethings.setup.Config;
import java.util.Set;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.ModConfigSpec;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class ConfigValueComponentProcessor implements IComponentProcessor {
    private String rawText;

    @Override
    public void setup(final Level level, final IVariableProvider iVariableProvider) {
        this.rawText = iVariableProvider.get("text", level.registryAccess()).asString();
    }

    @Override
    public IVariable process(final Level level, final String key) {
        if (!key.equals("text")) {
            return null;
        }

        final Set<ModConfigSpec.ConfigValue<?>> options = Set.of(Config.TIME_WAND_MAX_MULTIPLIER);

        String result = this.rawText;

        for (final ModConfigSpec.ConfigValue<?> option : options) {
            final Object value = option.get();
            result = result.replace("#" + String.join(".", option.getPath()) + "#", String.valueOf(value));
        }

        return IVariable.wrap(result, level.registryAccess());
    }
}
