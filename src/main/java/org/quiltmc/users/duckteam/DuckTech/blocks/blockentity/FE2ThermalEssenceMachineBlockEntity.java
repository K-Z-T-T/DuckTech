package org.quiltmc.users.duckteam.DuckTech.blocks.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.users.duckteam.DuckTech.blocks.DTBlockEntity;
import org.quiltmc.users.duckteam.DuckTech.gui.fe2thermal_essence_machine.FE2ThermalEssenceMachineMenu;
import org.quiltmc.users.duckteam.DuckTech.items.DTItems;

public class FE2ThermalEssenceMachineBlockEntity extends BlockEntity implements MenuProvider {

    // 能量存储
    private final EnergyStorage energyStorage = new EnergyStorage(10000, 1000, 0, 0);
    private final LazyOptional<EnergyStorage> energyLazy = LazyOptional.of(() -> energyStorage);

    // 物品存储：只有一个输出槽
    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged(); // 标记数据已变更，存档/同步
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            // 这个槽不允许玩家放入任何物品，只能由机器输出
            return false;
        }
    };
    private final LazyOptional<ItemStackHandler> itemLazy = LazyOptional.of(() -> itemHandler);

    private int tickCounter = 0;
    private static final int TICKS_PER_OPERATION = 20;
    private static final int ENERGY_PER_OPERATION = 100;

    public FE2ThermalEssenceMachineBlockEntity(BlockPos pos, BlockState state) {
        super(DTBlockEntity.FE2THERMAL_ESSENCE_MACHINE_BLOCK_ENTITY.get(), pos, state);
    }

    public void serverTick() {
        if (level == null || level.isClientSide()) return;

        tickCounter++;
        if (tickCounter >= TICKS_PER_OPERATION) {
            tickCounter = 0;

            // 能量不足则跳过
            if (energyStorage.getEnergyStored() < ENERGY_PER_OPERATION) {
                return;
            }

            // 输出槽逻辑：如果为空，直接放入橡木原木
            // 如果已有橡木原木且数量未满，则增加1
            ItemStack output = itemHandler.getStackInSlot(0);
            if (output.isEmpty()) {
                energyStorage.extractEnergy(ENERGY_PER_OPERATION, false);
                itemHandler.setStackInSlot(0, new ItemStack(DTItems.THERMAL_ESSENCE.get(), 1));
            } else if (output.getItem() == Items.OAK_LOG && output.getCount() < output.getMaxStackSize()) {
                energyStorage.extractEnergy(ENERGY_PER_OPERATION, false);
                output.grow(1);
                // setStackInSlot 会调用 onContentsChanged
                itemHandler.setStackInSlot(0, output);
            }
            // 否则槽满或不是橡木，什么都不做
        }
    }

    // ---------- GUI 提供 ----------
    @Override
    public Component getDisplayName() {
        return Component.translatable("block.oakgenerator.oak_generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new FE2ThermalEssenceMachineMenu(containerId, playerInventory, this);
    }

    // ---------- 能力暴露 ----------
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return energyLazy.cast();
        }
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return itemLazy.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        energyLazy.invalidate();
        itemLazy.invalidate();
    }

    // ---------- 数据持久化 ----------
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        energyStorage.deserializeNBT(tag.get("Energy"));
        itemHandler.deserializeNBT(tag.getCompound("Inventory"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Energy", energyStorage.serializeNBT());
        tag.put("Inventory", itemHandler.serializeNBT());
    }

    // 供菜单使用的能量存取器（避免直接暴露 EnergyStorage）
    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }
}