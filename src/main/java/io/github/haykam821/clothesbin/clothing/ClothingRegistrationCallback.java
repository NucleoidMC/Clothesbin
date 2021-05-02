package io.github.haykam821.clothesbin.clothing;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import xyz.nucleoid.plasmid.registry.TinyRegistry;

public interface ClothingRegistrationCallback {
	Event<ClothingRegistrationCallback> EVENT = EventFactory.createArrayBacked(ClothingRegistrationCallback.class, callbacks -> {
		return registry -> {
			for (ClothingRegistrationCallback callback : callbacks) {
				callback.register(registry);
			}
		};
	});

	void register(TinyRegistry<Clothing> registry);
}