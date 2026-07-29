package org.quiltmc.users.duckteam.DuckTech.gui.essence_earth_furnace;

import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class EssenceEarthFurnaceScreen extends AbstractFurnaceScreen<EssenceEarthFurnaceMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/furnace.png");

    public EssenceEarthFurnaceScreen(EssenceEarthFurnaceMenu menu, Inventory playerInventory, Component title) {
        super(menu, new AbstractFurnaceRecipeBookComponent() {
            @Override
            protected Set<Item> getFuelItems() {
                return null;
            }
        }, playerInventory, title, TEXTURE);
    }
}