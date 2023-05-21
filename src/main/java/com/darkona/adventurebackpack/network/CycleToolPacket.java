package com.darkona.adventurebackpack.network;

import net.minecraft.entity.player.EntityPlayerMP;

import com.darkona.adventurebackpack.common.ServerActions;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class CycleToolPacket implements IMessageHandler<CycleToolPacket.CycleToolMessage, IMessage> {

    public static final byte TOGGLE_HOSE_TANK = 0;
    public static final byte SWITCH_HOSE_ACTION = 1;
    public static final byte CYCLE_TOOL_ACTION = 2;

    @Override
    public IMessage onMessage(CycleToolMessage message, MessageContext ctx) {
        if (ctx.side.isServer()) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;

            if (player == null || player.isDead) return null;

            switch (message.typeOfAction) {
                case CYCLE_TOOL_ACTION:
                    ServerActions.cycleTool(player, message.isWheelUp);
                    break;
                case TOGGLE_HOSE_TANK:
                    ServerActions.switchHose(player, message.isWheelUp, ServerActions.HOSE_TOGGLE);
                    break;
                case SWITCH_HOSE_ACTION:
                    ServerActions.switchHose(player, message.isWheelUp, ServerActions.HOSE_SWITCH);
                    break;
            }
        }
        return null;
    }

    public static class CycleToolMessage implements IMessage {

        private byte typeOfAction;
        private boolean isWheelUp;

        public CycleToolMessage() {}

        public CycleToolMessage(boolean isWheelUp, byte typeOfAction) {
            this.typeOfAction = typeOfAction;
            this.isWheelUp = isWheelUp;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            this.typeOfAction = buf.readByte();
            this.isWheelUp = buf.readBoolean();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeByte(typeOfAction);
            buf.writeBoolean(isWheelUp);
        }
    }
}
