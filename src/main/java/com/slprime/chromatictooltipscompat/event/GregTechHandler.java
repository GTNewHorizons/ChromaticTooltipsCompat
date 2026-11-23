package com.slprime.chromatictooltipscompat.event;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fluids.FluidStack;

import com.slprime.chromatictooltips.event.FluidInfoEnricherEvent;
import com.slprime.chromatictooltips.util.TooltipUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.common.items.ItemFluidDisplay;

public class GregTechHandler {

    private static boolean registered = false;

    public static void registerHandler() {
        registered = true;
        TooltipUtils.registerEventListener(new GregTechHandler());
    }

    public static void removeFluidMaterialLines(FluidStack fluid, List<String> lines) {
        if (!registered || fluid == null || fluid.getFluid() == null || lines.size() < 2) {
            return;
        }

        final List<String> materialLines = new ArrayList<>();
        ItemFluidDisplay.addTooltipForFluid(fluid.getFluid(), materialLines);

        final List<String> infoLines = lines.subList(1, lines.size());
        for (String line : materialLines) {
            infoLines.remove(line);
        }
    }

    @SubscribeEvent
    public void onFluidInfoEnricherEvent(FluidInfoEnricherEvent event) {
        final List<String> lines = new ArrayList<>();
        ItemFluidDisplay.addTooltipForFluid(
            event.target.getFluid()
                .getFluid(),
            lines);

        event.tooltip.addAll(0, lines);
    }

}
