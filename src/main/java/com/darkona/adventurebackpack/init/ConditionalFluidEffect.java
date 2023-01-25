package com.darkona.adventurebackpack.init;

import adventurebackpack.api.FluidEffect;
import com.darkona.adventurebackpack.fluids.effects.FuelEffect;
import com.darkona.adventurebackpack.fluids.effects.OilEffect;
import com.darkona.adventurebackpack.reference.LoadedMods;

/**
 * Created on 28/12/2014
 *
 * @author Darkona
 */
public class ConditionalFluidEffect {
    public static FluidEffect oilEffect;
    public static FluidEffect fuelEffect;

    public static void init() {
        if (LoadedMods.BUILDCRAFT) {
            oilEffect = new OilEffect();
            fuelEffect = new FuelEffect();
        }
    }
}
