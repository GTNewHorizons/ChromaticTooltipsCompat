package com.slprime.chromatictooltipscompat.event;

import java.util.ArrayList;
import java.util.List;

import com.slprime.chromatictooltips.event.FluidInfoEnricherEvent;
import com.slprime.chromatictooltips.util.TooltipUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.common.items.ItemFluidDisplay;

public class GregTechHandler {

    public static void registerHandler() {
        TooltipUtils.registerEventListener(new GregTechHandler());
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
