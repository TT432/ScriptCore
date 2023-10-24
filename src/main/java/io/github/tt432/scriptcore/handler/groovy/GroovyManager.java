package io.github.tt432.scriptcore.handler.groovy;

import groovy.lang.Binding;
import groovy.lang.MetaMethod;
import groovy.lang.Script;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import io.github.tt432.scriptcore.annotation.ScriptHandlerClass;
import io.github.tt432.scriptcore.handler.ScriptHandler;
import io.github.tt432.scriptcore.util.WrapperMethod;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.codehaus.groovy.reflection.CachedClass;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author TT432
 */
@Slf4j
@ScriptHandlerClass("groovy")
public class GroovyManager implements ScriptHandler<Script> {
    private GroovyScriptEngine engine;
    private ResourceManager resourceManager;

    @Override
    public void setup(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        engine = new GroovyScriptEngine(new ResourceManagerConnector(resourceManager), GroovyManager.class.getClassLoader());
    }

    @Override
    public Script loadScript(ResourceLocation id) {
        try {
            Script script = engine.createScript(id.toString(), new Binding());
            log.info("load script {} successful.", id);
            return script;
        } catch (ResourceException | ScriptException e) {
            log.warn("load script {} failed.", id);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map<ResourceLocation, Script> loadAllScript(String dir) {
        Map<ResourceLocation, Script> result = new HashMap<>();
        FileToIdConverter converter = new FileToIdConverter(dir, ".groovy");

        Map<ResourceLocation, Resource> resourceLocationResourceMap = converter.listMatchingResources(resourceManager);

        resourceLocationResourceMap.forEach((key, value) -> result.put(key, loadScript(key)));

        return result;
    }

    @Override
    public List<WrapperMethod> loadAllMethod(Object object) {
        List<WrapperMethod> result = new ArrayList<>();

        if (object instanceof Script script) {
            for (MetaMethod metaMethod : script.getMetaClass().getMethods()) {
                String name = metaMethod.getName();
                CachedClass[] parameterTypes = metaMethod.getParameterTypes();
                Class<?>[] array = Arrays.stream(parameterTypes).map(CachedClass::getTheClass).toArray(Class[]::new);
                Class<?> returnType = metaMethod.getReturnType();

                result.add(new WrapperMethod(name, array, returnType, (method, args) -> {
                    try {
                        return script.invokeMethod(name, args);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return Array.get(Array.newInstance(returnType, 1), 0);
                    }
                }));
            }
        }

        return result;
    }
}
