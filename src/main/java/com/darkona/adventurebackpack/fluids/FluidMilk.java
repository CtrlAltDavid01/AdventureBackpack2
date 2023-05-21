package com.darkona.adventurebackpack.fluids;

import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.darkona.adventurebackpack.client.Icons;

public class FluidMilk extends Fluid {

    public FluidMilk() {
        super("milk");
        setDensity(1200);
        setViscosity(1200);
        setLuminosity(0);
    }

    @Override
    public IIcon getStillIcon() {
        return Icons.milkStill;
    }

    @Override
    public IIcon getIcon() {
        return Icons.milkStill;
    }

    @Override
    public IIcon getFlowingIcon() {
        return Icons.milkStill;
    }

    @Override
    public int getColor(FluidStack stack) {
        return 0xffffff;
    }

    @Override
    public boolean isGaseous(World world, int x, int y, int z) {
        return false;
    }
}
