package io.github.tt432.scriptcore.test;

import io.github.tt432.scriptcore.Scriptcore;
import io.github.tt432.scriptcore.factory.ScriptsFactory;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author TT432
 */
@Mod.EventBusSubscriber
public class TestScriptExecute {
    @SubscribeEvent
    public static void onEvent(ClientPlayerNetworkEvent.LoggingIn event) {
        TestScriptClass test = ScriptsFactory.createInstance(TestScriptClass.class, Scriptcore.MOD_ID, "test");
        test.test();
    }
}
