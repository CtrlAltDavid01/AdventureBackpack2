package com.darkona.adventurebackpack.util;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.darkona.adventurebackpack.init.ModItems;
import com.darkona.adventurebackpack.inventory.IInventoryTanks;
import com.darkona.adventurebackpack.inventory.InventoryBackpack;
import com.darkona.adventurebackpack.inventory.InventoryCoalJetpack;
import com.darkona.adventurebackpack.inventory.InventoryCopterPack;
import com.darkona.adventurebackpack.item.IBackWearableItem;
import com.darkona.adventurebackpack.item.ItemAdventureBackpack;
import com.darkona.adventurebackpack.item.ItemAdventureHat;
import com.darkona.adventurebackpack.item.ItemCoalJetpack;
import com.darkona.adventurebackpack.item.ItemCopterPack;
import com.darkona.adventurebackpack.item.ItemHose;
import com.darkona.adventurebackpack.item.ItemPistonBoots;
import com.darkona.adventurebackpack.playerProperties.BackpackProperty;
import com.darkona.adventurebackpack.reference.BackpackTypes;

public class Wearing {

    public enum WearableType {

        BACKPACK,
        COPTERPACK,
        JETPACK,
        UNKNOWN;

        public static WearableType get(@Nonnull ItemStack stack) {
            Item item = stack.getItem();
            if (item == ModItems.adventureBackpack) return BACKPACK;
            if (item == ModItems.copterPack) return COPTERPACK;
            if (item == ModItems.coalJetpack) return JETPACK;
            return UNKNOWN;
        }
    }

    // Wearable
    public static boolean isWearingWearable(EntityPlayer player) {
        final ItemStack wearable = BackpackProperty.get(player).getWearable();
        if (wearable != null) {
            return wearable.getItem() instanceof IBackWearableItem;
        }
        return false;
    }

    public static ItemStack getWearingWearable(EntityPlayer player) {
        final ItemStack wearable = BackpackProperty.get(player).getWearable();
        if (wearable != null && wearable.getItem() instanceof IBackWearableItem) {
            return wearable;
        }
        return null;
    }

    public static WearableType getWearingWearableType(EntityPlayer player) {
        final ItemStack wearable = BackpackProperty.get(player).getWearable();
        if (wearable != null && wearable.getItem() instanceof IBackWearableItem) {
            return WearableType.get(wearable);
        }
        return WearableType.UNKNOWN;
    }

    public static IInventoryTanks getWearingWearableInv(EntityPlayer player) {
        final ItemStack wearable = Wearing.getWearingWearable(player);
        if (wearable != null) {
            if (wearable.getItem() instanceof ItemAdventureBackpack) return new InventoryBackpack(wearable);
            if (wearable.getItem() instanceof ItemCoalJetpack) return new InventoryCoalJetpack(wearable);
            if (wearable.getItem() instanceof ItemCopterPack) return new InventoryCopterPack(wearable);
        }
        return null;
    }

    public static boolean isHoldingWearable(EntityPlayer player) {
        return player.inventory.getCurrentItem() != null
                && player.inventory.getCurrentItem().getItem() instanceof IBackWearableItem;
    }

    // Backpack
    public static boolean isWearingBackpack(EntityPlayer player) {
        final ItemStack wearable = BackpackProperty.get(player).getWearable();
        if (wearable != null) {
            return wearable.getItem() instanceof ItemAdventureBackpack;
        }
        return false;
    }

    public static boolean isWearingTheRightBackpack(EntityPlayer player, BackpackTypes... backpacks) {
        final ItemStack backpack = Wearing.getWearingBackpack(player);
        if (backpack != null) {
            final BackpackTypes backPackType = BackpackTypes.getType(backpack);
            for (BackpackTypes type : backpacks) {
                if (backPackType == type) {
                    return true;
                }
            }
        }
        return false;
    }

    public static ItemStack getWearingBackpack(EntityPlayer player) {
        final ItemStack wearable = BackpackProperty.get(player).getWearable();
        if (wearable != null && wearable.getItem() instanceof ItemAdventureBackpack) {
            return wearable;
        }
        return null;
    }

    public static InventoryBackpack getWearingBackpackInv(EntityPlayer player) {
        return new InventoryBackpack(BackpackProperty.get(player).getWearable());
    }

    public static boolean isHoldingBackpack(EntityPlayer player) {
        return player.inventory.getCurrentItem() != null
                && player.inventory.getCurrentItem().getItem() instanceof ItemAdventureBackpack;
    }

    public static ItemStack getHoldingBackpack(EntityPlayer player) {
        return isHoldingBackpack(player) ? player.inventory.getCurrentItem() : null;
    }

    public static InventoryBackpack getHoldingBackpackInv(EntityPlayer player) {
        return new InventoryBackpack(player.getCurrentEquippedItem());
    }

    // Copter
    public static boolean isWearingCopter(EntityPlayer player) {
        final ItemStack wearable = BackpackProperty.get(player).getWearable();
        if (wearable != null) {
            return wearable.getItem() instanceof ItemCopterPack;
        }
        return false;
    }

    public static ItemStack getWearingCopter(EntityPlayer player) {
        final ItemStack wearable = BackpackProperty.get(player).getWearable();
        if (wearable != null && wearable.getItem() instanceof ItemCopterPack) {
            return wearable;
        }
        return null;
    }

    public static boolean isHoldingCopter(EntityPlayer player) {
        return player.inventory.getCurrentItem() != null
                && player.inventory.getCurrentItem().getItem() instanceof ItemCopterPack;
    }

    public static ItemStack getHoldingCopter(EntityPlayer player) {
        return isHoldingCopter(player) ? player.inventory.getCurrentItem() : null;
    }

    // Jetpack
    public static boolean isWearingJetpack(EntityPlayer player) {
        final ItemStack wearable = BackpackProperty.get(player).getWearable();
        if (wearable != null) {
            return wearable.getItem() instanceof ItemCoalJetpack;
        }
        return false;
    }

    public static ItemStack getWearingJetpack(EntityPlayer player) {
        final ItemStack wearable = BackpackProperty.get(player).getWearable();
        if (wearable != null && wearable.getItem() instanceof ItemCoalJetpack) {
            return wearable;
        }
        return null;
    }

    public static boolean isHoldingJetpack(EntityPlayer player) {
        return player.inventory.getCurrentItem() != null
                && player.inventory.getCurrentItem().getItem() instanceof ItemCoalJetpack;
    }

    public static ItemStack getHoldingJetpack(EntityPlayer player) {
        return isHoldingJetpack(player) ? player.inventory.getCurrentItem() : null;
    }

    // Hose
    public static boolean isHoldingHose(EntityPlayer player) {
        return player.inventory.getCurrentItem() != null
                && player.inventory.getCurrentItem().getItem() instanceof ItemHose;
    }

    public static ItemStack getHoldingHose(EntityPlayer player) {
        return isHoldingHose(player) ? player.inventory.getCurrentItem() : null;
    }

    // Armor
    public static boolean isWearingHat(EntityPlayer player) {
        return player.inventory.armorInventory[3] != null
                && player.inventory.armorInventory[3].getItem() instanceof ItemAdventureHat;
    }

    public static ItemStack getWearingHat(EntityPlayer player) {
        return isWearingHat(player) ? player.inventory.armorInventory[3] : null;
    }

    public static boolean isWearingBoots(EntityPlayer player) {
        return player.inventory.armorInventory[0] != null
                && player.inventory.armorInventory[0].getItem() instanceof ItemPistonBoots;
    }

    public static ItemStack getWearingBoots(EntityPlayer player) {
        return isWearingBoots(player) ? player.inventory.armorInventory[0] : null;
    }
}
