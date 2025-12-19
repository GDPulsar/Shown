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

    private static final KeyMapping TEST_SCREEN = new KeyMapping(
            "key.shown.test_screen",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_K,
            "key.categories.ui"
    );

    public static void init() {
        if (Platform.isDevelopmentEnvironment()) {
            KeyMappingRegistry.register(TEST_SCREEN);
            ClientTickEvent.CLIENT_POST.register(minecraft -> {
                while (TEST_SCREEN.consumeClick()) {
                    minecraft.setScreen(new SplitScreen());
                }
            });
        }
    }
}
