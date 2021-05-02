package io.github.haykam821.clothesbin.component;

import java.util.EnumMap;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import io.github.haykam821.clothesbin.clothing.Clothing;
import io.github.haykam821.clothesbin.clothing.ClothingData;
import io.github.haykam821.clothesbin.clothing.ClothingType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

public class ClothingComponent implements PlayerComponent<Component> {
	protected ClothingComponent(PlayerEntity player) {
		return;
	}

	private final EnumMap<ClothingType, Clothing> clothing = new EnumMap<>(ClothingType.class);

	public ItemStack getClothingStack(EquipmentSlot slot) {
		ClothingType type = ClothingType.bySlot(slot);
		if (type == null) return ItemStack.EMPTY;
		
		Clothing clothing = this.getClothing(type);
		if (clothing == null) return ItemStack.EMPTY;

		return clothing.getIcon();
	}

	public Clothing getClothing(ClothingType type) {
		return this.clothing.get(type);
	}

	/**
	 * Determines whether clothing is equipped to a specified type.
	 */
	public boolean hasClothing(ClothingType type, Clothing clothing) {
		return this.getClothing(type) == clothing;
	}

	/**
	 * Equips clothing to a specified type.
	 */
	public void setClothing(ClothingType type, Clothing clothing) {
		this.clothing.put(type, clothing);
	}
	
	/**
	 * Equips clothing to its recommended type.
	 */
	public void equipClothing(Clothing clothing) {
		this.setClothing(clothing.getType(), clothing);
	}

	// Serialization
	@Override
	public void readFromNbt(CompoundTag tag) {
		this.clothing.clear();
		
		CompoundTag clothingTag = tag.getCompound("Clothing");
		for (ClothingType type : ClothingType.VALUES) {
			Identifier id = Identifier.tryParse(clothingTag.getString(type.asString()));
			if (id == null) continue;

			Clothing clothing = ClothingData.INSTANCE.get(id);
			if (clothing == null) continue;

			this.setClothing(type, clothing);
		}
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		CompoundTag clothingTag = new CompoundTag();
		for (ClothingType type : ClothingType.VALUES) {
			Clothing clothing = this.getClothing(type);
			if (clothing == null) continue;

			Identifier id = ClothingData.INSTANCE.getId(clothing);
			if (id == null) continue;

			clothingTag.putString(type.asString(), id.toString());
		}

		tag.put("Clothing", clothingTag);
	}
}
