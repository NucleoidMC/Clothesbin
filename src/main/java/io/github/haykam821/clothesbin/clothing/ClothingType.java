package io.github.haykam821.clothesbin.clothing;

import com.mojang.serialization.Codec;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.StringIdentifiable;

public enum ClothingType implements StringIdentifiable {
	HEAD("head", EquipmentSlot.HEAD),
	CHEST("chest", EquipmentSlot.CHEST),
	LEGS("legs", EquipmentSlot.LEGS),
	FEET("feet", EquipmentSlot.FEET);

	public static final ClothingType[] VALUES = values();
	public static final Codec<ClothingType> CODEC = StringIdentifiable.createCodec(ClothingType::values, ClothingType::byName);

	private final String name;
	private final EquipmentSlot slot;

	private ClothingType(String name, EquipmentSlot slot) {
		this.name = name;
		this.slot = slot;
	}

	@Override
	public String asString() {
		return this.name;
	}

	public Text getText() {
		return new TranslatableText("text.clothesbin.clothing_type." + this.name);
	}

	public static ClothingType byName(String name) {
		for (ClothingType type : VALUES) {
			if (type.name.equals(name)) return type;
		}
		return null;
	}

	public static ClothingType bySlot(EquipmentSlot slot) {
		for (ClothingType type : VALUES) {
			if (type.slot == slot) return type;
		}
		return null;
	}
}
