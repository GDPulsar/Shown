package com.pulsar.shown.neoforge;

import com.pulsar.shown.Shown;
import net.neoforged.fml.common.Mod;

@Mod(Shown.MOD_ID)
public final class ShownNeoForge {
    public ShownNeoForge() {
        // Run our common setup.
        Shown.init();
    }
}
