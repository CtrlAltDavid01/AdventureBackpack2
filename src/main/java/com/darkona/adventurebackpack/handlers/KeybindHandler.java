package com.darkona.adventurebackpack.handlers;

import com.darkona.adventurebackpack.AdventureBackpack;
import com.darkona.adventurebackpack.config.Keybindings;
import com.darkona.adventurebackpack.network.GuiBackpackMessage;
import com.darkona.adventurebackpack.network.MessageConstants;
import com.darkona.adventurebackpack.reference.Key;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Darkona on 11/10/2014.
 */
public class KeybindHandler {

    private static Key getPressedKeyBinding() {
        if (Keybindings.openBackpack.isPressed()) {
            return Key.OPEN_BACKPACK_INVENTORY;
        }
        return Key.UNKNOWN;
    }

    @SubscribeEvent
    public void handleKeyInputEvent(InputEvent.KeyInputEvent event) {
        Key keypressed = getPressedKeyBinding();
        //LogHelper.info(keypressed);
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;
        int playerX = (int) player.posX;
        int playerY = (int) player.posY;
        int playerZ = (int) player.posZ;


        if (keypressed == Key.OPEN_BACKPACK_INVENTORY) {
            AdventureBackpack.networkWrapper.sendToServer(new GuiBackpackMessage(MessageConstants.NORMAL_GUI, MessageConstants.FROM_KEYBIND));
        }
    }
}
