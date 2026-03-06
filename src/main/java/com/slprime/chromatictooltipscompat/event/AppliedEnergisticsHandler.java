package com.slprime.chromatictooltipscompat.event;

import java.awt.Point;
import java.lang.reflect.Method;

import net.minecraft.client.gui.inventory.GuiContainer;

import com.slprime.chromatictooltips.event.StackSizeEnricherEvent;
import com.slprime.chromatictooltips.util.TooltipUtils;

import appeng.api.storage.data.IAEStack;
import appeng.client.gui.AEBaseGui;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class AppliedEnergisticsHandler {

    protected static Method getVirtualMESlotUnderMouse = null;
    protected static Method getSlot = null;

    protected static Class<?> SlotMEClazz = null;
    protected static Method getSlotMEStack = null;

    protected static Class<?> VirtualMESlotClazz = null;
    protected static Method getVirtualMESlotStack = null;

    public static void registerHandler() {
        TooltipUtils.registerEventListener(new AppliedEnergisticsHandler());

        try {
            getVirtualMESlotUnderMouse = AEBaseGui.class.getDeclaredMethod("getVirtualMESlotUnderMouse");

            VirtualMESlotClazz = Class.forName("appeng.client.gui.slots.VirtualMESlot");
            getVirtualMESlotStack = VirtualMESlotClazz.getDeclaredMethod("getAEStack");
            getVirtualMESlotStack.setAccessible(true);
        } catch (Exception ignored) {
            /* Do nothing */
        }

        try {
            getSlot = AEBaseGui.class.getDeclaredMethod("getSlot", int.class, int.class);
            getSlot.setAccessible(true);

            SlotMEClazz = Class.forName("appeng.client.me.SlotME");
            getSlotMEStack = SlotMEClazz.getDeclaredMethod("getAEStack");
            getSlotMEStack.setAccessible(true);

        } catch (Exception ignored) {
            /* Do nothing */
        }
    }

    @SubscribeEvent
    public void onStackSizeEnricherEvent(StackSizeEnricherEvent event) {
        final GuiContainer gui = TooltipUtils.getGuiContainer();

        if (gui instanceof AEBaseGui basesGui) {

            try {
                if (getVirtualMESlotUnderMouse != null) {
                    final Object virtualMESlot = getVirtualMESlotUnderMouse.invoke(basesGui);
                    if (virtualMESlot != null && VirtualMESlotClazz.isInstance(virtualMESlot)) {
                        final IAEStack<?> aes = (IAEStack<?>) getVirtualMESlotStack.invoke(virtualMESlot);
                        if (aes != null) {
                            event.stackAmount = aes.getStackSize();
                        }
                    }
                } else {
                    final Point mousePos = TooltipUtils.getMousePosition();
                    final Object slot = getSlot.invoke(basesGui, mousePos.x, mousePos.y);

                    if (slot != null && SlotMEClazz.isInstance(slot)) {
                        final IAEStack<?> aes = (IAEStack<?>) getSlotMEStack.invoke(slot);
                        if (aes != null) {
                            event.stackAmount = aes.getStackSize();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
