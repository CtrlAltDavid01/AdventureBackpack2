package com.darkona.adventurebackpack.network;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;

import com.darkona.adventurebackpack.AdventureBackpack;
import com.darkona.adventurebackpack.init.ModNetwork;
import com.darkona.adventurebackpack.playerProperties.BackpackProperty;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class SyncPropertiesPacket
        implements IMessageHandler<SyncPropertiesPacket.Message, SyncPropertiesPacket.Message> {

    public static final byte DATA_ONLY = 0;
    public static final byte TANKS_ONLY = 1;
    public static final byte INVENTORY_ONLY = 2;
    public static final byte FULL_DATA = 3;

    @Override
    public Message onMessage(Message message, MessageContext ctx) {
        if (ctx.side.isClient() && message.properties != null) {
            if (Minecraft.getMinecraft().theWorld == null) {
                ModNetwork.net.sendToServer(new SyncPropertiesPacket.Message());
            } else {
                AdventureBackpack.proxy.synchronizePlayer(message.ID, message.properties);
            }
        }
        if (ctx.side.isServer()) {
            BackpackProperty.sync(ctx.getServerHandler().playerEntity);
        }
        return null;
    }

    public static class Message implements IMessage {

        private int ID;
        private NBTTagCompound properties;

        public Message() {}

        public Message(int id, NBTTagCompound props) {
            ID = id;
            properties = props;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            ID = buf.readInt();
            properties = ByteBufUtils.readTag(buf);
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(ID);
            ByteBufUtils.writeTag(buf, properties);
        }
    }
}
