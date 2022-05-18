package com.darkona.adventurebackpack.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import com.darkona.adventurebackpack.client.gui.GuiAdvBackpack;
import com.darkona.adventurebackpack.reference.ModInfo;

public class NEIConfig implements IConfigureNEI {
    @Override
    public void loadConfig() {
        API.registerGuiOverlay(GuiAdvBackpack.class, "crafting", new BackpackStackPositioner());
        API.registerGuiOverlayHandler(GuiAdvBackpack.class, new BackpackOverlayHandler(), "crafting");
    }

    @Override
    public String getName() {
        return ModInfo.MOD_NAME;
    }

    @Override
    public String getVersion() {
        return ModInfo.MOD_VERSION;
    }
}
