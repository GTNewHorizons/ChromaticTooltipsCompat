package com.slprime.chromatictooltipscompat.mixins.late.wawla;

import net.darkhax.wawla.addons.generic.AddonGenericTooltips;
import net.darkhax.wawla.util.Utilities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.slprime.chromatictooltips.api.TooltipLines;
import com.slprime.chromatictooltips.util.TooltipUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Mixin(AddonGenericTooltips.class)
public class AddonGenericTooltipsMixin {

    /**
     * @author SLPrime
     * @reason Disable WAWLA's own enchanted book tooltip branch so it doesn't conflict with the chromatic tooltip,
     *         and remove armor protection tooltip added by WAWLA.
     */
    @Overwrite(remap = false)
    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {

        if (event.entityPlayer != null && event.entityPlayer.worldObj != null) {
            Item item = event.itemStack.getItem();
            Block block = Block.getBlockFromItem(item);

            if (item instanceof ItemEnchantedBook) {
                Enchantment[] enchArr = Utilities.getEnchantmentsFromStack(event.itemStack, true);
                Enchantment ench = enchArr.length > 0 ? enchArr[0] : null;

                if (ench != null) {

                    if (TooltipUtils.isShiftKeyDown()) {
                        final String translation = StatCollector.translateToLocal("description." + ench.getName());

                        if (translation.startsWith("description.")) {
                            event.toolTip.add(StatCollector.translateToLocal("tooltip.wawla.missingEnch"));
                        } else {
                            event.toolTip.add(translation);
                        }
                    }

                    event.toolTip.add(TooltipLines.SHIFT_MODIFIER);
                }
            }

            if (block != null && !(block instanceof BlockAir)) {
                float enchPower = block.getEnchantPowerBonus(event.entityPlayer.worldObj, 0, 0, 0);

                if (enchPower > 0) {
                    event.toolTip.add(StatCollector.translateToLocal("tooltip.wawla.enchPower") + ": " + enchPower);
                }
            }
        }
    }

}
