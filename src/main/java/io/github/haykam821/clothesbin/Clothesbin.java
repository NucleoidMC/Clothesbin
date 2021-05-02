package io.github.haykam821.clothesbin;

import io.github.haykam821.clothesbin.clothing.Clothing;
import io.github.haykam821.clothesbin.clothing.ClothingData;
import io.github.haykam821.clothesbin.clothing.ClothingRegistrationCallback;
import io.github.haykam821.clothesbin.clothing.ClothingType;
import io.github.haykam821.clothesbin.command.ClothesbinCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import xyz.nucleoid.plasmid.registry.TinyRegistry;

public class Clothesbin implements ModInitializer {
	public static final String MOD_ID = "clothesbin";

	@Override
	public void onInitialize() {
		ResourceManagerHelper serverDataHelper = ResourceManagerHelper.get(ResourceType.SERVER_DATA);
		serverDataHelper.registerReloadListener(ClothingData.INSTANCE);

		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			ClothesbinCommand.register(dispatcher);
		});

		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			ClothingRegistrationCallback.EVENT.register(Clothesbin::registerDevelopmentClothing);
		}
	}

	private static void registerDevelopmentClothing(TinyRegistry<Clothing> registry) {
		registry.register(new Identifier(MOD_ID, "test"), new Clothing(ClothingType.HEAD, new ItemStack(Items.END_ROD)));
	}
}
