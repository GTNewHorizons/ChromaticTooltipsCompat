package com.slprime.chromatictooltipscompat.event;

import net.minecraft.item.ItemStack;

import com.slprime.chromatictooltips.api.TooltipModifier;
import com.slprime.chromatictooltips.util.TooltipUtils;
import com.slprime.chromatictooltipscompat.util.TooltipResolverEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import forestry.apiculture.items.ItemBeeGE;

public class ForestryHandler {

    public static void registerHandler() {
        TooltipUtils.registerEventListener(new ForestryHandler());
    }

    @SubscribeEvent
    public void onTooltip(TooltipResolverEvent event) {
        final ItemStack stack = event.getItemStack();
        if (stack.getItem() instanceof ItemBeeGE bee && stack.hasTagCompound()
            && bee.getIndividual(stack) != null
            && bee.getIndividual(stack)
                .isAnalyzed()) {
            event.supports(TooltipModifier.SHIFT);
        }
    }

}
