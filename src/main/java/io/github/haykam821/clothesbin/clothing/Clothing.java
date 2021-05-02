package io.github.haykam821.clothesbin.clothing;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;

public class Clothing {
	public static final Codec<Clothing> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
			ClothingType.CODEC.fieldOf("type").forGetter(Clothing::getType),
			ItemStack.CODEC.fieldOf("icon").forGetter(Clothing::getIcon)
		).apply(instance, Clothing::new);
	});

	private final ClothingType type;
	private final ItemStack icon;
	private String translationKey;

	public Clothing(ClothingType type, ItemStack icon) {
		this.type = type;
		this.icon = icon;
	}

	public ClothingType getType() {
		return this.type;
	}

	public ItemStack getIcon() {
		return this.icon;
	}

	public ItemStack createFullIcon() {
		ItemStackBuilder builder = ItemStackBuilder.of(this.icon)
			.setName(this.getNonItalicizedName());

		List<Text> tooltip = new ArrayList<>();
		this.appendTooltip(tooltip);
		for (Text line : tooltip) {
			builder.addLore(line.shallowCopy().styled(style -> {
				return style.withItalic(false).withColor(Formatting.GRAY);
			}));
		}

		return builder.build();
	}

	public void appendTooltip(List<Text> tooltip) {
		tooltip.add(new TranslatableText("text.clothesbin.recommended_clothing_type", this.type.getText()));
	}

	public Text getName() {
		return new TranslatableText(this.getTranslationKey());
	}

	private Text getNonItalicizedName() {
		return this.getName().shallowCopy().styled(style -> {
			return style.withItalic(false);
		});
	}

	public String getTranslationKey() {
		if (this.translationKey == null) {
			this.translationKey = Util.createTranslationKey("clothing", this.getId());
		}
		return this.translationKey;
	}

	public Identifier getId() {
		return ClothingData.INSTANCE.getId(this);
	}
}
