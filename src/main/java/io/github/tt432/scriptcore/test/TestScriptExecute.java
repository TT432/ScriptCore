package io.github.tt432.scriptcore.test;

import io.github.tt432.scriptcore.annotation.ScriptInstance;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
