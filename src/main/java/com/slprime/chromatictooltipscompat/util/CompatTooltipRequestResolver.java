package com.slprime.chromatictooltipscompat.util;

import net.minecraft.item.ItemStack;

import com.slprime.chromatictooltips.api.ITooltipRequestResolver;
import com.slprime.chromatictooltips.api.TooltipRequest;
import com.slprime.chromatictooltips.util.TooltipUtils;

public class CompatTooltipRequestResolver implements ITooltipRequestResolver {

    @Override
    public boolean resolve(TooltipRequest request) {
        final ItemStack stack = request.target.getItem();

        if (stack != null) {
            TooltipUtils.postEvent(new TooltipResolverEvent(request));
        }

        return false;
    }

}
