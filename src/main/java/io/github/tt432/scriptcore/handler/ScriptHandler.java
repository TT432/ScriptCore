package io.github.tt432.scriptcore.handler;

import io.github.tt432.scriptcore.util.WrapperMethod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * @author TT432
 */
public interface ScriptHandler<S /* Script Type */ > {
    List<WrapperMethod> loadAllMethod(Object script);
    void setup(ResourceManager resourceManager);
    @Nullable
    S loadScript(ResourceLocation id);
    Map<ResourceLocation, S> loadAllScript(String dir);
}
