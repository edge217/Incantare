package com.edgeburnmedia.incantare.incantare.enchantment;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.concurrent.atomic.AtomicBoolean;

public class GroundingEnchantment extends Enchantment {
    public GroundingEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_FEET, new EquipmentSlot[]{EquipmentSlot.FEET});
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onCheckEffect(MobEffectEvent.Applicable event) {
        if (!event.getEntity().getLevel().isClientSide()) {
            MobEffectInstance potionEffect = event.getEffectInstance();
            ResourceLocation key = ForgeRegistries.MOB_EFFECTS.getKey(potionEffect.getEffect());
            if (bootsEnchanted(event.getEntity()) && key.equals(new ResourceLocation("minecraft", "levitation"))) {
                event.setResult(Event.Result.DENY);
            }
        }
    }

    protected boolean bootsEnchanted(LivingEntity entity) {
        final GroundingEnchantment enchantment = this; // for use in lambda
        AtomicBoolean enchanted = new AtomicBoolean(false);
        entity.getArmorSlots().forEach(stack -> {
            if (stack.getEnchantmentLevel(enchantment) > 0) {
                enchanted.set(true);
            }
        });
        return enchanted.get();
    }
}
