package io.github.tt432.scriptcore;

import groovy.lang.GroovyObject;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import net.neoforged.bus.api.IEventBus;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author TT432
 */
public class GroovyDirLoadManager {
    String dir;
    GroovyScriptEngine engine;

    public GroovyDirLoadManager(String dir) {
        this.dir = dir;
    }

    public void onStart(IEventBus bus) {
        try {
            engine = new GroovyScriptEngine(dir, getClass().getClassLoader());
            engine.getGroovyClassLoader();

            File dirFile = new File(this.dir);
            File[] files = dirFile.listFiles((d, name) -> name.endsWith(".groovy"));

            if (files != null) {
                for (File file : files) {
                    var scriptClass = engine.loadScriptByName(file.getName());
                    GroovyObject scriptInstance = (GroovyObject) scriptClass.newInstance();

                    boolean methodInvoked = false;

                    for (Method method : scriptClass.getMethods()) {
                        if (method.getName().equals("start") && method.getParameterCount() == 1
                                && method.getParameterTypes()[0].isAssignableFrom(IEventBus.class)) {
                            method.invoke(scriptInstance, bus);
                            methodInvoked = true;
                            break;
                        }
                    }

                    if (!methodInvoked) {
                        try {
                            scriptInstance.invokeMethod("start", null);
                        } catch (Exception e) {
                            System.out.println("The script " + file.getName() + " does not contain the method start or it cannot be invoked.");
                        }
                    }
                }
            }
        } catch (ResourceException | ScriptException | IOException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
