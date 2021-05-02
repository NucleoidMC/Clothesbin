package io.github.haykam821.clothesbin.ui;

import io.github.haykam821.clothesbin.clothing.Clothing;
import io.github.haykam821.clothesbin.clothing.ClothingData;
import io.github.haykam821.clothesbin.component.ClothesbinComponentInitializer;
import io.github.haykam821.clothesbin.component.ClothingComponent;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemStack.TooltipSection;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import xyz.nucleoid.plasmid.shop.ShopEntry;
import xyz.nucleoid.plasmid.shop.ShopUi;

public final class ClothesbinUi {
	public static final ClothesbinUi INSTANCE = new ClothesbinUi();

	private static final Text TITLE = new TranslatableText("text.clothesbin.title");

	private ClothesbinUi() {
		return;
	}

	private void select(Clothing clothing, ClothingComponent component) {
		component.equipClothing(clothing);
	}

	private ItemStack getIcon(Clothing clothing, ClothingComponent component) {
		ItemStack stack = clothing.createFullIcon();

		if (component.hasClothing(clothing.getType(), clothing)) {
			stack.addEnchantment(Enchantments.BINDING_CURSE, 1);
			stack.addHideFlag(TooltipSection.ENCHANTMENTS);
		}

		return stack;
	}

	private ShopUi getShop(ClothingComponent component) {
		return ShopUi.create(TITLE, builder -> {
			for (Identifier id : ClothingData.INSTANCE.getKeySet()) {
				Clothing clothing = ClothingData.INSTANCE.get(id);

				ShopEntry entry = ShopEntry.ofIcon(this.getIcon(clothing, component));
				entry.onBuy(player -> this.select(clothing, component));
				entry.noCost();

				builder.add(entry);
			}
		});
	}

	public void open(ServerPlayerEntity player) {
		ClothingComponent component = ClothesbinComponentInitializer.CLOTHING.get(player);
		player.openHandledScreen(this.getShop(component));
	}
}
