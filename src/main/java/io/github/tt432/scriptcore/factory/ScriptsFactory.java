package io.github.tt432.scriptcore.factory;

import io.github.tt432.scriptcore.proxy.ScriptObjectInvocationHandler;
import io.github.tt432.scriptcore.resources.ScriptsResourceManager;
import io.github.tt432.scriptcore.test.TestScriptClass;
import io.github.tt432.scriptcore.util.ScriptInfo;
import io.github.tt432.scriptcore.util.ScriptWrapperObject;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Proxy;

/**
 * @author TT432
 */
public class ScriptsFactory {
    @NotNull
    public static <S> S createInstance(Class<S> scriptClass, String namespace, String fileName) {
        ScriptInfo script = ScriptsResourceManager.getInstance().getScript(scriptClass, namespace, fileName);
        ScriptWrapperObject obj;

        if (script != null) {
            obj = script.wrapperObject();
        } else {
            obj = ScriptWrapperObject.EMPTY;
        }

        return (S) Proxy.newProxyInstance(
                obj.getClass().getClassLoader(),
                new Class[]{TestScriptClass.class},
                new ScriptObjectInvocationHandler(obj));
    }
}
