package com.edgeburnmedia.incantare.incantare.enchantment;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ShatterproofEnchantment extends Enchantment {

	public ShatterproofEnchantment() {
		super(Rarity.RARE, EnchantmentCategory.BREAKABLE,
			new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.HEAD, EquipmentSlot.CHEST,
				EquipmentSlot.LEGS, EquipmentSlot.FEET});
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onItemUse(LivingEntityUseItemEvent.Start event) {
		if (!event.getEntity().level().isClientSide()) {
			ItemStack stack = event.getItem();
			if (stack != null) {
				event.setCanceled(protectItem(stack));
			}

		}
	}

	@SubscribeEvent
	public void onBlockDamage(PlayerEvent.BreakSpeed event) {
		ItemStack stack = event.getEntity().getMainHandItem();
		if (stack != null) {
			if (protectItem(stack)) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onArmourEquip(LivingEquipmentChangeEvent event) {
		ItemStack stack = event.getTo();
		if (stack != null) {
			if (protectItem(stack)) {
				event.setResult(Event.Result.DENY);
			}
		}
	}

	@SubscribeEvent
	public void onEntityHurt(LivingHurtEvent event) {
		if (!event.getEntity().level().isClientSide()) {
			Entity entity = event.getEntity();
			for (ItemStack armorSlot : entity.getArmorSlots()) {
				if (protectItem(armorSlot)) {
					if (entity instanceof ServerPlayer player) {
						if (!player.getInventory().add(armorSlot)) {
							ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(),
								player.getY(), player.getZ(), armorSlot);
							player.level().addFreshEntity(itemEntity);
							player.getInventory().removeItem(armorSlot);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onAttackEntity(AttackEntityEvent event) {
		if (!event.getEntity().level().isClientSide()) {
			ItemStack stack = event.getEntity().getMainHandItem();
			if (stack != null) {
				if (protectItem(stack)) {
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public void onShieldDefend(ShieldBlockEvent event) {
		if (!event.getEntity().level().isClientSide()) {
			ItemStack stack = event.getEntity().getUseItem();
			if (stack != null) {
				if (protectItem(stack)) {
					event.setShieldTakesDamage(false);
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public void renderTooltip(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		if (stack != null) {
			int level = stack.getEnchantmentLevel(this);
			if (level > 0 && stack.getMaxDamage() - stack.getDamageValue() <= 2) {
				event.getToolTip().add(
					Component.literal("Broken").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
			}
		}
	}

	protected boolean protectItem(ItemStack stack) {
		int level = stack.getEnchantmentLevel(this);
		if (level > 0) {
			int durabilityRemaining = stack.getMaxDamage() - stack.getDamageValue();
			if (durabilityRemaining <= 2) {
				stack.setDamageValue(Math.abs(2 - stack.getMaxDamage()));
				return true;
			}
		}
		return false;
	}

}
