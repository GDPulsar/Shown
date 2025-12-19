package com.pulsar.shown;

import com.mojang.blaze3d.platform.InputConstants;
import com.pulsar.shown.screen.SplitScreen;
import com.pulsar.shown.screen.TabbedScreen;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.KeyMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Shown {
    public static final String MOD_ID = "assets/split";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {

    }
}
