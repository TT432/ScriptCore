# Script Core

---

## 功能列表

### 脚本加载

会自动加载 `.minecraft/scripts` 下的所有脚本。

在 Mod 加载时尝试调用所有脚本的 `start()`，如果你需要 ModEventBus，就将签名改为 `start(IEventBus bus)`。

案例：

```groovy
package io.github.tt432

import groovy.transform.Field
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.TickEvent

import java.util.function.Consumer

/**
 * @author TT432
 */

@Field
int tickCount = 0

void start() {
    NeoForge.EVENT_BUS.addListener(new Consumer<TickEvent.ServerTickEvent>() {
        @Override
        void accept(TickEvent.ServerTickEvent e) {
            if (e.phase == TickEvent.Phase.START
                    && tickCount++ % 20 == 0) {
                println("Hello Groovy")
            }
        }
    })
}
```