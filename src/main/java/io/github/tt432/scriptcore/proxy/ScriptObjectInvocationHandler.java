package io.github.tt432.scriptcore.proxy;

import io.github.tt432.scriptcore.util.ScriptWrapperObject;
import io.github.tt432.scriptcore.util.WrapperMethod;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * <pre>{@code
 *
 *  ScriptWrapperObject obj = null; // instance
 *
 *  TestScriptClass test = (TestScriptClass) Proxy.newProxyInstance(
 *      obj.getClass().getClassLoader(),
 *      new Class[]{TestScriptClass.class},
 *      new ScriptObjectInvocationHandler(obj)
 *  );
 * }</pre>
 *
 * @author TT432
 */
public class ScriptObjectInvocationHandler implements InvocationHandler {
    ScriptWrapperObject wrapperObject;

    public ScriptObjectInvocationHandler(ScriptWrapperObject wrapperObject) {
        this.wrapperObject = wrapperObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String name = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Class<?> returnType = method.getReturnType();

        for (WrapperMethod wrapperMethod : wrapperObject.methods()) {
            if (wrapperMethod.name().equals(name)
                    && Arrays.equals(parameterTypes, wrapperMethod.parameterTypes())
                    && returnType.equals(wrapperMethod.returnType())) {
                return wrapperMethod.methodObject().invoke(wrapperMethod, args);
            }
        }

        if (returnType.equals(void.class))
            return null;

        return Array.get(Array.newInstance(returnType, 1), 0);
    }
}
