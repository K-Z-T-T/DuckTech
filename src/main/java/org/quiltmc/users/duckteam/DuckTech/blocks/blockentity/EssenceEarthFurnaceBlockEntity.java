package org.quiltmc.users.duckteam.DuckTech.blocks.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.quiltmc.users.duckteam.DuckTech.blocks.DTBlockEntity;
import org.quiltmc.users.duckteam.DuckTech.gui.essence_earth_furnace.EssenceEarthFurnaceMenu;

public class EssenceEarthFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    // 目标燃料物品
    private static final Item THERMAL_ESSENCE = ForgeRegistries.ITEMS.getValue(
            new ResourceLocation("ducktech", "thermal_essence"));
    // 目标排除标签
    private static final TagKey<Item> ORE_TAG =
            TagKey.create(Registries.ITEM, new ResourceLocation("ducktech", "ore"));

    public EssenceEarthFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(DTBlockEntity.ESSENCE_EARTH_FURNACE_BLOCK_ENTITY.get(), pos, state, RecipeType.SMELTING);
    }

    // 限制槽位物品放置：燃料槽只能放thermal_essence，输入槽不能有ore标签
    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot == 1) { // 燃料槽
            return stack.is(THERMAL_ESSENCE);
        } else if (slot == 0) { // 输入槽
            return !stack.is(ORE_TAG);
        }
        return super.canPlaceItem(slot, stack);
    }

    // 定义燃料燃烧时间（tick），这里固定为400
    @Override
    protected int getBurnDuration(ItemStack stack) {
        if (stack.is(THERMAL_ESSENCE)) {
            return 400;
        }
        return 0;
    }

    // 创建菜单
    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.essence_earth_furnace");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new EssenceEarthFurnaceMenu(id, inv, this, this.dataAccess);
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inv) {
        return new EssenceEarthFurnaceMenu(id, inv, this, this.dataAccess);
    }

    public ContainerData getContainerData() {
        return this.dataAccess;
    }
}