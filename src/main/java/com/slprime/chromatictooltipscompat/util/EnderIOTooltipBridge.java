package com.slprime.chromatictooltipscompat.util;

import java.awt.Point;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;

import com.enderio.core.client.gui.widget.GuiToolTip;
import com.slprime.chromatictooltips.util.TooltipUtils;
import com.slprime.chromatictooltipscompat.mixins.late.enderio.GuiMachineBaseInvoker;

import crazypants.enderio.machine.gui.GuiMachineBase;

public final class EnderIOTooltipBridge {

    public static void handle(List<String> tooltip) {
        final GuiContainer gui = TooltipUtils.getGuiContainer();

        if (gui instanceof GuiMachineBase<?>base) {
            final List<GuiToolTip> progressTooltips = ((GuiMachineBaseInvoker) base).getProgressTooltips();
            final Point mouse = TooltipUtils.getMousePosition();

            if (progressTooltips == null) {
                return;
            }

            for (GuiToolTip tt : progressTooltips) {
                if (tt.getBounds()
                    .contains(mouse.x - gui.guiLeft, mouse.y - gui.guiTop)) {
                    tooltip.addAll(tt.getToolTipText());
                    break;
                }
            }

            for (GuiToolTip tt : progressTooltips) {
                tt.setVisible(false);
            }
        }
    }
}
