package io.github.tt432.scriptcore.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for marking script classes.
 * <p>
 * Classes marked with this annotation will be bound to scripts located in a specific path.
 * </p>
 * Will check and binding script's method on load script.
 *
 * @author TT432
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ScriptClass {
    /**
     * @return script type id
     * @see ScriptHandlerClass
     */
    String value();

    /**
     * Retrieves the script binding path.
     * <p>
     * The location of the script will be determined by ResourceLocation(?, "data/scripts/{bindingPath}/**").
     * </p>
     *
     * @return The script binding path.
     */
    String bindingPath();
}
