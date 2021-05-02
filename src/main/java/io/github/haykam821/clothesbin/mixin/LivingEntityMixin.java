package io.github.haykam821.clothesbin.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import io.github.haykam821.clothesbin.component.ClothesbinComponentInitializer;
import io.github.haykam821.clothesbin.component.ClothingComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@Redirect(method = "method_30129", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"))
	private ItemStack modifyEquippedStack(LivingEntity entity, EquipmentSlot slot) {
		if (entity instanceof ServerPlayerEntity) {
			ClothingComponent component = ClothesbinComponentInitializer.CLOTHING.get(entity);

			ItemStack stack = component.getClothingStack(slot);
			if (!stack.isEmpty()) {
				return stack;
			}
		}

		return entity.getEquippedStack(slot);
	}
}
