package io.github.tt432.scriptcore.util;

import java.util.List;

/**
 * @author TT432
 */
public record ScriptWrapperObject(
        List<WrapperMethod> methods
) {
    public static final ScriptWrapperObject EMPTY = new ScriptWrapperObject(List.of());
}
