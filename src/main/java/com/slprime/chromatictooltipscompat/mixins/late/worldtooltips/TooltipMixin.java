package com.slprime.chromatictooltipscompat.mixins.late.worldtooltips;

import java.nio.FloatBuffer;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.slprime.chromatictooltips.TooltipHandler;
import com.slprime.chromatictooltips.api.TooltipContext;
import com.slprime.chromatictooltips.api.TooltipRequest;
import com.slprime.chromatictooltips.api.TooltipTarget;
import com.slprime.chromatictooltips.component.SectionComponent;
import com.slprime.chromatictooltips.util.TooltipSpacing;
import com.slprime.chromatictooltips.util.TooltipUtils;

import ninja.genuine.tooltips.client.Tooltip;

@Mixin(value = Tooltip.class, remap = false)
public class TooltipMixin {

    @Unique
    private static final FloatBuffer chromatic$modelview = BufferUtils.createFloatBuffer(16);

    @Unique
    private static final FloatBuffer chromatic$projection = BufferUtils.createFloatBuffer(16);

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
        final ScaledResolution sr = TooltipUtils.getScaledResolution();

        if (this.chromatic$request == null || !ItemStack.areItemStacksEqual(this.chromatic$stackSnapshot, stack)) {
            this.chromatic$request = new TooltipRequest("worldtooltips", TooltipTarget.ofItem(stack), null, null);
            this.chromatic$stackSnapshot = stack.copy();
            this.chromatic$context = null;
        }

        if (this.chromatic$context == null || this.chromatic$modifierHash != modifierHash) {
            this.chromatic$context = TooltipHandler
                .createTooltipContext(this.chromatic$request, this.chromatic$context);
            this.chromatic$context.setScaleFactor(sr.getScaleFactor());
            this.chromatic$modifierHash = modifierHash;
        }

        final SectionComponent page = this.chromatic$context.getActivePageComponent();
        if (page == null || page.isEmpty()) {
            return;
        }

        final float anchorX = x + tooltip.getWidth() / 2f;
        final float anchorY = y + tooltip.getHeight() + 4;

        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, chromatic$modelview);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, chromatic$projection);

        final double screenWidth = sr.getScaledWidth_double();
        final double screenHeight = sr.getScaledHeight_double();
        final double[] anchor = chromatic$project(anchorX, anchorY, screenWidth, screenHeight);
        final double[] probe = chromatic$project(anchorX + 100f, anchorY, screenWidth, screenHeight);

        if (anchor == null || probe == null) {
            return;
        }

        final double unit = Math.hypot(probe[0] - anchor[0], probe[1] - anchor[1]) / 100d;
        if (!(unit > 0d) || Double.isInfinite(unit)) {
            return;
        }

        final TooltipSpacing margin = page.getMargin();
        final int innerWidth = page.getWidth() - margin.getInline();
        final int innerHeight = page.getHeight() - margin.getBlock();
        final int drawX = -innerWidth / 2 - margin.getLeft();
        final int drawY = -innerHeight - margin.getTop();

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        // unit also scales z, so the depth range must grow with it or item icons get clipped by the near plane
        GL11.glOrtho(0d, screenWidth, screenHeight, 0d, 1000d * unit, 3000d * unit);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glTranslated(0d, 0d, -2000d * unit);
        GL11.glTranslated(anchor[0], anchor[1], 0d);
        GL11.glScaled(unit, unit, unit);

        this.chromatic$context.drawAt(drawX, drawY);

        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadMatrix(chromatic$projection);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    @Unique
    private static double[] chromatic$project(float x, float y, double screenWidth, double screenHeight) {
        final FloatBuffer mv = chromatic$modelview;
        final FloatBuffer p = chromatic$projection;
        final double[] eye = new double[4];

        for (int i = 0; i < 4; i++) {
            eye[i] = mv.get(i) * x + mv.get(4 + i) * y + mv.get(12 + i);
        }

        final double[] clip = new double[4];

        for (int i = 0; i < 4; i++) {
            clip[i] = p.get(i) * eye[0] + p.get(4 + i) * eye[1] + p.get(8 + i) * eye[2] + p.get(12 + i) * eye[3];
        }

        if (clip[3] <= 1.0E-6d) {
            return null;
        }

        final double ndcX = clip[0] / clip[3];
        final double ndcY = clip[1] / clip[3];

        return new double[] { (ndcX + 1d) * 0.5d * screenWidth, (1d - ndcY) * 0.5d * screenHeight };
    }
}
