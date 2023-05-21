package com.darkona.adventurebackpack.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import com.darkona.adventurebackpack.config.ConfigHandler;

public class ItemPistonBoots extends ArmorAB {

    public ItemPistonBoots() {
        super(2, 3);
        setMaxDamage(Items.iron_boots.getMaxDamage() + 55);
        setUnlocalizedName("pistonBoots");
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (ConfigHandler.pistonBootsSprintBoost != 0 && player.isSprinting()) player.addPotionEffect(
                new PotionEffect(Potion.moveSpeed.getId(), 1, ConfigHandler.pistonBootsSprintBoost - 1));
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return repair.isItemEqual(new ItemStack(Items.leather));
    }
}
