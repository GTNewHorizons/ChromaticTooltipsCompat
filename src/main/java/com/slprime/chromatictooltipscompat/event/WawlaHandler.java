package com.slprime.chromatictooltipscompat.event;

import net.darkhax.wawla.util.Utilities;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;

import com.slprime.chromatictooltips.api.TooltipModifier;
import com.slprime.chromatictooltips.util.TooltipUtils;
import com.slprime.chromatictooltipscompat.util.TooltipResolverEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class WawlaHandler {

    public static void registerHandler() {
        TooltipUtils.registerEventListener(new WawlaHandler());
    }

    @SubscribeEvent
    public void onTooltip(TooltipResolverEvent event) {
        final ItemStack stack = event.getItemStack();
        if (stack.getItem() instanceof ItemEnchantedBook
            && Utilities.getEnchantmentsFromStack(stack, true).length > 0) {
            event.supports(TooltipModifier.SHIFT);
        }
    }

}
