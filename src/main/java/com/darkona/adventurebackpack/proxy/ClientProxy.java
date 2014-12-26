package com.darkona.adventurebackpack.proxy;

import com.darkona.adventurebackpack.block.TileAdventureBackpack;
import com.darkona.adventurebackpack.client.render.*;
import com.darkona.adventurebackpack.config.Keybindings;
import com.darkona.adventurebackpack.handlers.KeybindHandler;
import com.darkona.adventurebackpack.init.ModItems;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.client.MinecraftForgeClient;

/**
 * Created by Darkona on 10/10/2014.
 */
public class ClientProxy implements IProxy
{

    public static RendererItemAdventureBackpack rendererItemAdventureBackpack;
    public static RendererItemAdventureHat rendererItemAdventureHat;
    public static RendererHose rendererHose;
    public static RendererBackpackArmor rendererBackpackArmor;

    public void init()
    {
        initRenderers();
        registerKeybindings();
    }

    public void initNetwork()
    {
//
//        ModNetwork.networkWrapper.registerMessage(NyanCatMessage.NyanCatMessageClientHandler.class, NyanCatMessage.class, ModNetwork.messages++, Side.CLIENT);
//        ModNetwork.networkWrapper.registerMessage(CowAbilityPacket.CowAbilityMessageClientHandler.class, CowAbilityPacket.class, ModNetwork.messages++, Side.CLIENT);
    }

    public void initRenderers()
    {
        rendererItemAdventureBackpack = new RendererItemAdventureBackpack();
        rendererItemAdventureHat = new RendererItemAdventureHat();
        rendererHose = new RendererHose();
        rendererBackpackArmor = new RendererBackpackArmor();

        MinecraftForgeClient.registerItemRenderer(ModItems.adventureHat, rendererItemAdventureHat);
        MinecraftForgeClient.registerItemRenderer(ModItems.adventureBackpack, rendererItemAdventureBackpack);
        MinecraftForgeClient.registerItemRenderer(ModItems.hose, rendererHose);

        ClientRegistry.bindTileEntitySpecialRenderer(TileAdventureBackpack.class, new RendererAdventureBackpackBlock());
    }

    public void registerKeybindings()
    {
        ClientRegistry.registerKeyBinding(Keybindings.openBackpack);
        ClientRegistry.registerKeyBinding(Keybindings.toggleHose);
        FMLCommonHandler.instance().bus().register(new KeybindHandler());

    }

}
