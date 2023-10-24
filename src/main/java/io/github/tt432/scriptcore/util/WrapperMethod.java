package io.github.tt432.scriptcore.util;

/**
 * @author TT432
 */
public record WrapperMethod(
        String name,
        Class<?>[] parameterTypes,
        Class<?> returnType,
        MethodWrapper methodObject
) {
}
