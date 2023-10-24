package io.github.tt432.scriptcore.util;

/**
 * @author TT432
 */
@FunctionalInterface
public interface MethodWrapper {
    Object invoke(WrapperMethod method, Object... args);
}
