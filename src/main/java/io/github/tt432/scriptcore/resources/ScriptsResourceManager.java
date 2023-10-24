package io.github.tt432.scriptcore.resources;

import io.github.tt432.scriptcore.annotation.ScriptClass;
import io.github.tt432.scriptcore.annotation.ScriptHandlerClass;
import io.github.tt432.scriptcore.handler.ScriptHandler;
import io.github.tt432.scriptcore.util.ScriptInfo;
import io.github.tt432.scriptcore.util.ScriptWrapperObject;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TT432
 */
@Mod.EventBusSubscriber
public class ScriptsResourceManager extends SimplePreparableReloadListener<Integer> {
    @Getter
    private static ScriptsResourceManager instance;

    @SubscribeEvent
    public static void onEvent(AddReloadListenerEvent event) {
        instance = new ScriptsResourceManager();
        event.addListener(instance);
    }

    private static final Type SCRIPT_CLASS_ANNO = Type.getType(ScriptClass.class);
    private static final Type SCRIPT_HANDLER_CLASS_ANNO = Type.getType(ScriptHandlerClass.class);

    private boolean loadedHandlers;

    @Override
    protected @NotNull Integer prepare(@NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        if (!loadedHandlers) {
            loadedHandlers = true;
            loadHandlers();
        }

        return 0;
    }

    @Override
    protected void apply(@NotNull Integer pObject, @NotNull ResourceManager pResourceManager, @NotNull ProfilerFiller pProfiler) {
        loadScripts(pResourceManager);
    }

    private record Info(
            String path,
            String loader
    ) {
        public static final Info EMPTY = new Info("", "");
    }

    private final Map<Class<?>, Info> scriptClassInfos = new HashMap<>();
    private final Map<String, ScriptHandler<?>> handlers = new HashMap<>();

    private final Map<String, Map<ResourceLocation, ScriptInfo>> scripts = new HashMap<>();

    @Nullable
    public ScriptInfo getScript(Class<?> scriptClass, String namespace, String fileName) {
        Info info = scriptClassInfos.getOrDefault(scriptClass, Info.EMPTY);
        String loader = info.loader;
        return scripts.getOrDefault(loader, new HashMap<>())
                .get(new ResourceLocation(namespace, "scripts/" + info.path + "/" + fileName + "." + loader));
    }

    private void loadScripts(ResourceManager resourceManager) {
        scripts.clear();

        handlers.forEach((id, scriptHandler) -> scriptHandler.setup(resourceManager));

        scriptClassInfos.forEach((aClass, info) -> {
            Map<ResourceLocation, ScriptInfo> scriptInfo = scripts.computeIfAbsent(info.loader, x -> new HashMap<>());
            ScriptHandler<?> scriptHandler = handlers.get(info.loader);
            scriptHandler.loadAllScript("scripts/" + info.path).forEach((rl, script) ->
                    scriptInfo.put(rl, new ScriptInfo(rl, script, new ScriptWrapperObject(scriptHandler.loadAllMethod(script)))));
        });
    }

    private void loadHandlers() {
        scriptClassInfos.clear();
        handlers.clear();

        for (ModFileScanData allScanDatum : ModList.get().getAllScanData()) {
            List<ModFileScanData.AnnotationData> list = allScanDatum.getAnnotations().stream()
                    .filter(annotationData -> SCRIPT_CLASS_ANNO.equals(annotationData.annotationType()))
                    .toList();

            for (ModFileScanData.AnnotationData annotationData : list) {
                String bindingPath = (String) annotationData.annotationData().get("bindingPath");
                String loader = (String) annotationData.annotationData().get("value");

                try {
                    scriptClassInfos.put(Class.forName(annotationData.memberName()), new Info(bindingPath, loader));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            list = allScanDatum.getAnnotations().stream()
                    .filter(annotationData -> SCRIPT_HANDLER_CLASS_ANNO.equals(annotationData.annotationType()))
                    .toList();

            for (ModFileScanData.AnnotationData annotationData : list) {
                String id = (String) annotationData.annotationData().get("value");

                try {
                    handlers.put(id, Class.forName(annotationData.memberName())
                            .asSubclass(ScriptHandler.class)
                            .getConstructor()
                            .newInstance());
                } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
                         IllegalAccessException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
