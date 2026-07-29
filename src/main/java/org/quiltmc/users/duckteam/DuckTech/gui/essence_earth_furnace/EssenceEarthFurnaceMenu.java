package org.quiltmc.users.duckteam.DuckTech.gui.essence_earth_furnace;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.RecipeType;
import org.quiltmc.users.duckteam.DuckTech.blocks.blockentity.EssenceEarthFurnaceBlockEntity;
import org.quiltmc.users.duckteam.DuckTech.gui.DTMenu;

public class EssenceEarthFurnaceMenu extends AbstractFurnaceMenu {
    public EssenceEarthFurnaceMenu(int containerId, Inventory playerInventory) {
        super(DTMenu.ESSENCE_EARTH_FURNACE.get(), RecipeType.SMELTING,
                RecipeBookType.FURNACE, containerId, playerInventory);
    }

    public EssenceEarthFurnaceMenu(int id, Inventory player, EssenceEarthFurnaceBlockEntity furnace,
                                   ContainerData data) {
        super(DTMenu.ESSENCE_EARTH_FURNACE.get(), RecipeType.SMELTING,
                RecipeBookType.FURNACE, id, player, furnace, data);
    }
}