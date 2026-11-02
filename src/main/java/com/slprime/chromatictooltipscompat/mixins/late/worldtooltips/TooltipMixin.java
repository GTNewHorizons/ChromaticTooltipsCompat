package com.slprime.chromatictooltipscompat.mixins.late.worldtooltips;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.slprime.chromatictooltips.TooltipHandler;
import com.slprime.chromatictooltips.api.TooltipContext;
import com.slprime.chromatictooltips.api.TooltipLines;
import com.slprime.chromatictooltips.api.TooltipRequest;
import com.slprime.chromatictooltips.api.TooltipTarget;
import com.slprime.chromatictooltips.util.TooltipUtils;

import ninja.genuine.tooltips.client.Tooltip;

@Mixin(value = Tooltip.class, remap = false)
public class TooltipMixin {

    @Unique
    private TooltipContext chromatic$context;

    @Unique
    private TooltipRequest chromatic$request;

    @Unique
    private ItemStack chromatic$stackSnapshot;

    @Unique
    private int chromatic$modifierHash = Integer.MIN_VALUE;

    @Redirect(
        method = "renderTooltip3D",
        at = @At(value = "INVOKE", target = "Lninja/genuine/tooltips/client/RenderHelper;renderTooltipTile(IIIIIIII)V"))
    private void drawChromaticBackground(int x, int y, int width, int height, int backgroundStart, int backgroundEnd,
        int borderStart, int borderEnd) {
        drawChromaticTooltip(x, y);
    }

    @Redirect(
        method = "renderTooltip3D",
        at = @At(
            value = "INVOKE",
            target = "Lninja/genuine/tooltips/client/compat/BetterTooltipsCompat;DrawTooltip(IIII)V"))
    private void drawChromaticBetterTooltip(int x, int y, int width, int height) {
        drawChromaticTooltip(x, y);
    }

    @Redirect(
        method = "renderTooltip3D",
        at = @At(
            value = "INVOKE",
            target = "Lninja/genuine/tooltips/client/RenderHelper;renderTooltipText(Lninja/genuine/tooltips/client/Tooltip;III)V"))
    private void skipWorldTooltipText(Tooltip tooltip, int x, int y, int alpha) {}

    @Unique
    private void drawChromaticTooltip(int x, int y) {
        final Tooltip tooltip = (Tooltip) (Object) this;
        final ItemStack stack = tooltip.getEntity()
            .getEntityItem();
        final int modifierHash = TooltipUtils.getMetaHash();

        if (this.chromatic$request == null || !ItemStack.areItemStacksEqual(this.chromatic$stackSnapshot, stack)) {
            final List<String> lines = new ArrayList<>(tooltip.size());

            for (int i = 0; i < tooltip.size(); i++) {
                lines.add(tooltip.getLine(i));
            }

            this.chromatic$request = new TooltipRequest(
                null,
                TooltipTarget.ofItem(stack),
                new TooltipLines(lines),
                null);
            this.chromatic$stackSnapshot = stack.copy();
            this.chromatic$context = null;
        }

        if (this.chromatic$context == null || this.chromatic$modifierHash != modifierHash) {
            this.chromatic$context = TooltipHandler
                .createTooltipContext(this.chromatic$request, this.chromatic$context);
            this.chromatic$modifierHash = modifierHash;
        }

        this.chromatic$context.getRenderer()
            .draw(this.chromatic$context, x, y);
    }
}
