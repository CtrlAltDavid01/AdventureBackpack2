package com.darkona.adventurebackpack.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.darkona.adventurebackpack.CreativeTabAB;
import com.darkona.adventurebackpack.fluids.FluidEffectRegistry;
import com.darkona.adventurebackpack.init.ModFluids;
import com.darkona.adventurebackpack.util.Resources;

/**
 * Created on 19/10/2014
 *
 * @author Darkona
 */
public class ItemJuiceBottle extends ItemAB {

    public ItemJuiceBottle() {
        super();
        setCreativeTab(CreativeTabAB.TAB_AB);
        setFull3D();
        setUnlocalizedName("melonJuiceBottle");
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
        FluidEffectRegistry.executeFluidEffectsForFluid(ModFluids.melonJuice, player, world);
        return new ItemStack(Items.glass_bottle, 1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
        return stack;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack p_77661_1_) {
        return EnumAction.drink;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack p_77626_1_) {
        return 28;
    }

    @Override
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(Resources.getIconString("melonJuiceBottle"));
    }
}
