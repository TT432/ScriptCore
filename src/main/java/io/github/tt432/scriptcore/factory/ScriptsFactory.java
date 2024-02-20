package io.github.tt432.scriptcore.factory;

import com.google.common.reflect.Reflection;
import io.github.tt432.scriptcore.proxy.ScriptObjectInvocationHandler;
import io.github.tt432.scriptcore.resources.ScriptsResourceManager;
import io.github.tt432.scriptcore.util.ScriptInfo;
import io.github.tt432.scriptcore.util.ScriptWrapperObject;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * @author TT432
 */
public class ScriptsFactory {
    public static <S> S createInstance(Class<S> scriptClass, ResourceLocation fileName) {
        return createInstance(scriptClass, fileName.getNamespace(), fileName.getPath());
    }

    @NotNull
    public static <S> S createInstance(Class<S> scriptClass, String namespace, String fileName) {
        ScriptInfo script = ScriptsResourceManager.getInstance().getScript(scriptClass, namespace, fileName);
        ScriptWrapperObject obj;

        if (script != null) {
            obj = script.wrapperObject();
        } else {
            obj = ScriptWrapperObject.EMPTY;
        }

        return Reflection.newProxy(scriptClass, new ScriptObjectInvocationHandler(obj));
    }
}
