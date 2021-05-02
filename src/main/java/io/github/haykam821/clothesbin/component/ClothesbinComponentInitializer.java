package io.github.haykam821.clothesbin.component;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import io.github.haykam821.clothesbin.Clothesbin;
import nerdhub.cardinal.components.api.util.RespawnCopyStrategy;
import net.minecraft.util.Identifier;

public class ClothesbinComponentInitializer implements EntityComponentInitializer {
	private static final Identifier CLOTHING_ID = new Identifier(Clothesbin.MOD_ID, "clothing");
	public static final ComponentKey<ClothingComponent> CLOTHING = ComponentRegistry.getOrCreate(CLOTHING_ID, ClothingComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(CLOTHING, ClothingComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
	}
}