package com.slprime.chromatictooltipscompat.util;

import net.minecraft.item.ItemStack;

import com.slprime.chromatictooltips.api.TooltipModifier;
import com.slprime.chromatictooltips.api.TooltipRequest;

import cpw.mods.fml.common.eventhandler.Event;

public class TooltipResolverEvent extends Event {

    private final TooltipRequest request;

    public TooltipResolverEvent(TooltipRequest request) {
        this.request = request;
    }

    public ItemStack getItemStack() {
        return request.target.getItem();
    }

    public void supports(TooltipModifier... modifiers) {
        request.tooltip.supports(modifiers);
    }

}
