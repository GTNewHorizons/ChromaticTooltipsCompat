package com.slprime.chromatictooltipscompat.mixins.late.jecalculation;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import me.towdium.jecalculation.gui.JecaGui;
import me.towdium.jecalculation.gui.widgets.WTooltip;
import me.towdium.jecalculation.utils.Utilities;

@Mixin(WTooltip.class)
public abstract class WTooltipMixin {

    public abstract boolean mouseIn(int xMouse, int yMouse);

    /**
     * @author Slprime
     * @reason Remove the tooltip delay when the mouse is hovering over the tooltip to prevent it from disappearing
     *         while the player is trying to read it.
     */
    @Redirect(
        method = "onTooltip",
        at = @At(value = "INVOKE", target = "Lme/towdium/jecalculation/utils/Utilities$Timer;getTime()J"),
        remap = false)
    private long replaceTooltipDelay(Utilities.Timer timer, JecaGui gui, int xMouse, int yMouse, List<String> tooltip) {
        return mouseIn(xMouse, yMouse) ? 501L : 0L;
    }

}
