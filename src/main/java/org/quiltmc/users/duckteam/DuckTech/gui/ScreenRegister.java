package org.quiltmc.users.duckteam.DuckTech.gui;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.quiltmc.users.duckteam.DuckTech.DuckTech;
import org.quiltmc.users.duckteam.DuckTech.gui.advance_shredder.AdvanceShredderScreen;
import org.quiltmc.users.duckteam.DuckTech.gui.essence_conversion_machine.EssenceConversionMachineScreen;
import org.quiltmc.users.duckteam.DuckTech.gui.essence_furnace.EssenceFurnaceScreen;
import org.quiltmc.users.duckteam.DuckTech.gui.fe2thermal_essence_machine.FE2ThermalEssenceMachineScreen;
import org.quiltmc.users.duckteam.DuckTech.gui.levitation.LevitationMachineScreen;
import org.quiltmc.users.duckteam.DuckTech.gui.injection_machine.InjectionMachineScreen;

@Mod.EventBusSubscriber(modid = DuckTech.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ScreenRegister {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(DTMenu.ADVANCE_SHREDDER_MENU.get(), AdvanceShredderScreen::new);
            MenuScreens.register(DTMenu.LEVITATION_MACHINE_MENU.get(), LevitationMachineScreen::new);
            MenuScreens.register(DTMenu.ESSENCE_FURNACE_MENU.get(), EssenceFurnaceScreen::new);
            MenuScreens.register(DTMenu.ESSENCE_CONVERSION_MACHINE_MENU.get(), EssenceConversionMachineScreen::new);
            MenuScreens.register(DTMenu.INJECTION_MACHINE_MENU.get(), InjectionMachineScreen::new);
            MenuScreens.register(DTMenu.FE2THERMAL_ESSENCE_MACHINE_MENU.get(), FE2ThermalEssenceMachineScreen::new);
        });
    }
}
