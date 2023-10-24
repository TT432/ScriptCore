package io.github.tt432.scriptcore.util;

import net.minecraft.resources.ResourceLocation;

/**
 * @author TT432
 */
public record ScriptInfo(
        ResourceLocation location,
        Object script,
        ScriptWrapperObject wrapperObject
) {
}
