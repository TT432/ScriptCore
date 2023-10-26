package io.github.tt432.scriptcore.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author TT432
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ScriptInstance {
    /**
     * "{modid}:{filename}". "scriptcore:test" -> data/scriptcore/scripts/test.{loader};
     *
     * @return ResourceLocation
     */
    String value();
}
