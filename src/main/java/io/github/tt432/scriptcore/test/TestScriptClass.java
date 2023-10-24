package io.github.tt432.scriptcore.test;

import io.github.tt432.scriptcore.annotation.ScriptClass;

/**
 * @author TT432
 */
@ScriptClass(value = "groovy", bindingPath = "test")
public interface TestScriptClass {
    void test();
    int test2(int a);
}
