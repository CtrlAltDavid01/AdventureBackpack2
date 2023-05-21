package com.darkona.adventurebackpack.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;

import com.darkona.adventurebackpack.util.BackpackUtils;
import com.darkona.adventurebackpack.util.Wearing;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class EquipUnequipBackWearablePacket
        implements IMessageHandler<EquipUnequipBackWearablePacket.Message, EquipUnequipBackWearablePacket.Message> {

    public static final byte EQUIP_WEARABLE = 0;
    public static final byte UNEQUIP_WEARABLE = 1;

    @Override
    public Message onMessage(Message message, MessageContext ctx) {
        if (ctx.side.isServer()) {
            EntityPlayer player = ctx.getServerHandler().playerEntity;

            if (player == null || player.isDead) return null;

            if (message.action == EQUIP_WEARABLE && Wearing.isHoldingWearable(player)) {
                if (Wearing.isWearingWearable(player)) {
                    Wearing.WearableType wtype = Wearing.getWearingWearableType(player);
                    if (wtype != Wearing.WearableType.UNKNOWN) player.addChatComponentMessage(
                            new ChatComponentTranslation(
                                    "adventurebackpack:messages.already.equipped." + wtype.name().toLowerCase()));
                } else if (BackpackUtils.equipWearable(player.getCurrentEquippedItem(), player)
                        == BackpackUtils.Reasons.SUCCESSFUL) {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                            player.inventoryContainer.detectAndSendChanges();
                        }
            } else if (message.action == UNEQUIP_WEARABLE) {
                BackpackUtils.unequipWearable(player);
            }
        }
        return null;
    }

    public static class Message implements IMessage {

        private byte action;

        public Message() {}

        public Message(byte action) {
            this.action = action;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            action = buf.readByte();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeByte(action);
        }
    }
}
