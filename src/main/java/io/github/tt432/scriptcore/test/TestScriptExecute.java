package io.github.tt432.scriptcore.test;

import io.github.tt432.scriptcore.annotation.ScriptInstance;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

/**
 * @author TT432
 */
@Mod.EventBusSubscriber
public class TestScriptExecute {
    @ScriptInstance("scriptcore:test")
    private static TestScriptClass script;

    @SubscribeEvent
    public static void onEvent(ClientPlayerNetworkEvent.LoggingIn event) {
        script.test();
    }
}
