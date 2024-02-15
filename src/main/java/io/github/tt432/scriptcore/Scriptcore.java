package io.github.tt432.scriptcore;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Scriptcore.MOD_ID)
public class Scriptcore {
    public static final String MOD_ID = "scriptcore";

    public Scriptcore(IEventBus bus) {
        new GroovyDirLoadManager("scripts").onStart(bus);
    }
}
