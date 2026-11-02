package com.slprime.chromatictooltipscompat.mixins.late.gtnllib;

import java.util.List;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.gtnllib.client.tooltip.AnimatedTooltipHandler;
import com.slprime.chromatictooltips.TooltipHandler;
import com.slprime.chromatictooltips.component.DyncamicTextComponent;

@Mixin(value = AnimatedTooltipHandler.class, remap = false)
public class AnimatedTooltipHandlerMixin {

    /**
     * @author SLPrime
     * @reason Redirect tooltip text retrieval to Chromatic Tooltips so it can be wrapped in a dynamic text component.
     * @see https://github.com/ABKQPO/GT-Not-Leisure/blob/7a7e339eaf29b1793cf3970fb8dbd7fcf6f2bc2b/src/main/java/com/science/gtnl/mixins/early/Stick/MixinAnimatedTooltipHandler.java
     */
    @Redirect(
        method = "appendTooltipText",
        at = @At(value = "INVOKE", target = "Ljava/util/function/Supplier;get()Ljava/lang/Object;"),
        remap = false)
    private static Object renderTooltip(Supplier<String> tooltip, List<String> tooltips,
        List<Supplier<String>> tooltipSuppliers) {
        if (tooltip.get() != null) {
            tooltips.add(TooltipHandler.getComponentId(new DyncamicTextComponent(tooltip)));
        }

        return null;
    }

}
