package com.slprime.chromatictooltipscompat.event;

import java.util.Arrays;

import com.enderio.core.api.common.enchant.IAdvancedEnchant;
import com.slprime.chromatictooltips.api.EnchantmentData;
import com.slprime.chromatictooltips.event.EnchantmentEnricherEvent;
import com.slprime.chromatictooltips.util.TooltipUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EnderCoreHandler {

    public static void registerHandler() {
        TooltipUtils.registerEventListener(new EnderCoreHandler());
    }

    @SubscribeEvent
    public void onEnchantmentEvent(EnchantmentEnricherEvent event) {
        for (EnchantmentData ehchData : event.enchantments) {
            if (ehchData.hint.isEmpty() && ehchData.enchantment instanceof IAdvancedEnchant advancedEnchant) {
                ehchData.hint.addAll(Arrays.asList(advancedEnchant.getTooltipDetails(event.target.getItem())));
            }
        }
    }

}
