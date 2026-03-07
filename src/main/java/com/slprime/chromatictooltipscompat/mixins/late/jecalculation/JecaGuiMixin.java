package com.slprime.chromatictooltipscompat.mixins.late.jecalculation;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.slprime.chromatictooltips.TooltipHandler;

import me.towdium.jecalculation.gui.JecaGui;

@Mixin(JecaGui.class)
public class JecaGuiMixin {

    /**
     * @author Slprime
     * @reason Replace the tooltip rendering with our own to support chromatic tooltips in JEC.
     */
    @Overwrite(remap = false)
    public void drawHoveringText(List<String> lines, int x, int y, FontRenderer font) {
        TooltipHandler.drawHoveringText(lines);
    }

}
