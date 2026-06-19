package com.slprime.chromatictooltipscompat.event;

import net.minecraft.item.ItemStack;

import com.darkona.adventurebackpack.item.ItemAdventureBackpack;
import com.slprime.chromatictooltips.api.TooltipModifier;
import com.slprime.chromatictooltips.util.TooltipUtils;
import com.slprime.chromatictooltipscompat.util.TooltipResolverEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class AdventureBackpackHandler {

    public static void registerHandler() {
        TooltipUtils.registerEventListener(new AdventureBackpackHandler());
    }

    @SubscribeEvent
    public void onTooltip(TooltipResolverEvent event) {
        final ItemStack stack = event.getItemStack();
        if (stack.getItem() instanceof ItemAdventureBackpack) {
            event.supports(TooltipModifier.SHIFT, TooltipModifier.CTRL);
        }
    }

}
