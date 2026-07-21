package org.quiltmc.users.duckteam.DuckTech.gui;

import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.quiltmc.users.duckteam.DuckTech.DuckTech;
import org.quiltmc.users.duckteam.DuckTech.blocks.blockentity.FE2ThermalEssenceMachineBlockEntity;
import org.quiltmc.users.duckteam.DuckTech.gui.advance_shredder.AdvanceShredderMenu;
import org.quiltmc.users.duckteam.DuckTech.gui.essence_conversion_machine.EssenceConversionMachineMenu;
import org.quiltmc.users.duckteam.DuckTech.gui.essence_furnace.EssenceFurnaceMenu;
import org.quiltmc.users.duckteam.DuckTech.gui.fe2thermal_essence_machine.FE2ThermalEssenceMachineMenu;
import org.quiltmc.users.duckteam.DuckTech.gui.injection_machine.InjectionMachineMenu;
import org.quiltmc.users.duckteam.DuckTech.gui.levitation.LevitationMachineMenu;

import static net.minecraftforge.registries.ForgeRegistries.MENU_TYPES;


public class DTMenu {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(MENU_TYPES, DuckTech.MODID);

    public static final RegistryObject<MenuType<AdvanceShredderMenu>> ADVANCE_SHREDDER_MENU =
            MENUS.register("advance_shredder_menu", () -> IForgeMenuType.create(AdvanceShredderMenu::new
        ));

    public static final RegistryObject<MenuType<LevitationMachineMenu>> LEVITATION_MACHINE_MENU =
            MENUS.register("levitation_machine_menu", () -> IForgeMenuType.create(LevitationMachineMenu::new));

    public static final RegistryObject<MenuType<EssenceFurnaceMenu>> ESSENCE_FURNACE_MENU =
            MENUS.register("essence_furnace_menu", () -> IForgeMenuType.create(EssenceFurnaceMenu::new));

    public static final RegistryObject<MenuType<EssenceConversionMachineMenu>> ESSENCE_CONVERSION_MACHINE_MENU =
            MENUS.register("essence_conversion_machine_menu", () -> IForgeMenuType.create(EssenceConversionMachineMenu::new));

    public static final RegistryObject<MenuType<InjectionMachineMenu>> INJECTION_MACHINE_MENU =
            MENUS.register("injection_machine_menu", () -> IForgeMenuType.create(InjectionMachineMenu::new));

    public static final RegistryObject<MenuType<FE2ThermalEssenceMachineMenu>> FE2THERMAL_ESSENCE_MACHINE_MENU =
            MENUS.register("fe2thermal_essence_machine_menu",
                    () -> IForgeMenuType.create((windowId, inv, data) -> {
                        BlockPos pos = data.readBlockPos();
                        BlockEntity be = inv.player.level().getBlockEntity(pos);
                        if (be instanceof FE2ThermalEssenceMachineBlockEntity generator) {
                            return new FE2ThermalEssenceMachineMenu(windowId, inv, generator);
                        }
                        return null;
                    }));
}
