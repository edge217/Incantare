package com.edgeburnmedia.incantare.incantare.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ExperienceProficiencyEnchantment extends Enchantment {
    public ExperienceProficiencyEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.DIGGER, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        int level = getLevel(event.getPlayer().getMainHandItem());
        int baseXpDrop = event.getExpToDrop();
        if (level <= 0) {
            return;
        }
        event.setExpToDrop(genXp(baseXpDrop, level, event.getPlayer()));
    }

    private int genXp(int base, int level, Player player) {
        return (base + 1) * (player.getRandom().nextInt(getMaxLevel() * (level + 1)));
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    private int getLevel(ItemStack stack) {
        return stack.getEnchantmentLevel(this);
    }
}
