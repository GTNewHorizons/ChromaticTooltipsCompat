package com.slprime.chromatictooltipscompat.event;

import java.util.ArrayList;
import java.util.List;

import com.slprime.chromatictooltips.api.ITooltipComponent;
import com.slprime.chromatictooltips.api.TooltipContext;
import com.slprime.chromatictooltips.api.TooltipModifier;
import com.slprime.chromatictooltips.component.SectionComponent;
import com.slprime.chromatictooltips.event.TooltipEnricherEvent;
import com.slprime.chromatictooltips.util.TooltipUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ModularUI2Handler {

    public static final String FLUID_SLOT_CONTEXT = "modularui2.fluidslot";

    public static void registerHandler() {
        TooltipUtils.registerEventListener(new ModularUI2Handler());
    }

    @SubscribeEvent
    public void onTooltipEnricherEvent(TooltipEnricherEvent event) {
        final TooltipContext context = event.context;

        if (context.getActiveModifier() != TooltipModifier.SHIFT
            || !FLUID_SLOT_CONTEXT.equals(context.getContextName())) {
            return;
        }

        final List<ITooltipComponent> slotLines = context.getContextTooltip();

        if (slotLines.isEmpty()) {
            return;
        }

        for (SectionComponent section : context.getSections()) {
            if (!"body".equals(section.getSectionId())) {
                continue;
            }

            for (ITooltipComponent component : section.getComponents()) {
                if (component instanceof SectionComponent child && "contextInfo".equals(child.getSectionId())) {
                    return;
                }
            }

            section.addComponent(
                new SectionComponent(
                    "contextInfo",
                    context.getRenderer()
                        .getSectionBox("sections.contextInfo"),
                    new ArrayList<>(slotLines)));
            break;
        }
    }

}
