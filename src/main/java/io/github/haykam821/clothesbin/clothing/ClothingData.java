package io.github.haykam821.clothesbin.clothing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DataResult.PartialResult;
import com.mojang.serialization.JsonOps;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.haykam821.clothesbin.Clothesbin;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import xyz.nucleoid.plasmid.registry.TinyRegistry;

public final class ClothingData implements SimpleSynchronousResourceReloadListener {
	public static final ClothingData INSTANCE = new ClothingData();
	private static final Logger LOGGER = LogManager.getLogger("Clothing Data");

	private static final String RESOURCE_TYPE = "clothing";
	private static final Identifier RESOURCE_ID = new Identifier(Clothesbin.MOD_ID, RESOURCE_TYPE);
	private static final String EXTENSION = ".json";

	private final TinyRegistry<Clothing> registry = TinyRegistry.newStable();

	private ClothingData() {
		return;
	}

	public Clothing get(Identifier id) {
		return this.registry.get(id);
	}

	public Identifier getId(Clothing clothing) {
		return this.registry.getIdentifier(clothing);
	}

	public Set<Identifier> getKeySet() {
		return this.registry.keySet();
	}

	@Override
	public Identifier getFabricId() {
		return RESOURCE_ID;
	}

	@Override
	public void apply(ResourceManager manager) {
		this.registry.clear();

		for (Identifier path : ClothingData.findResources(manager)) {
			try {
				Resource resource = manager.getResource(path);
				Reader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));

				JsonElement json = new JsonParser().parse(reader);
				DataResult<Clothing> result = Clothing.CODEC.decode(JsonOps.INSTANCE, json).map(Pair::getFirst);

				if (result.result().isPresent()) {
					Identifier id = getClothingId(path);
					this.registry.register(id, result.result().get());
				} else {
					PartialResult<Clothing> error = result.error().get();
					LOGGER.error("Couldn't decode clothing from {}: {}", path, error.toString());
				}
			} catch (IOException exception) {
				LOGGER.error("Couldn't read clothing from {}", path, exception);
			}
		}

		ClothingRegistrationCallback.EVENT.invoker().register(this.registry);
	}

	private static Collection<Identifier> findResources(ResourceManager manager) {
		return manager.findResources(RESOURCE_TYPE, path -> {
			return path.endsWith(EXTENSION);
		});
	}

	private static Identifier getClothingId(Identifier path) {
		String pathPath = path.getPath();
		
		int beginIndex = (RESOURCE_TYPE + "/").length();
		int endReverseIndex = EXTENSION.length();

		return new Identifier(path.getNamespace(), pathPath.substring(beginIndex, pathPath.length() - endReverseIndex));
	}
}
