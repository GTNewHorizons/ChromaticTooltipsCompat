package com.slprime.chromatictooltipscompat.mixins.late.applecore;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import squeek.applecore.client.TooltipOverlayHandler;

@Mixin(TooltipOverlayHandler.class)
public class TooltipOverlayHandlerMixin {

    /**
     * @author slprime
     * @reason Disable the render tick handler to prevent conflicts with the tooltip rendering.
     */
    @Overwrite(remap = false)
    public void onRenderTick(RenderTickEvent event) {}

}
