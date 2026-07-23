package org.quiltmc.users.duckteam.DuckTech.blocks.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.ItemStackHandler;
import org.quiltmc.users.duckteam.DuckTech.blocks.DTBlockEntity;
import org.quiltmc.users.duckteam.DuckTech.gui.thermal_essence_maker.ThermalEssenceMakerMenu;
import org.quiltmc.users.duckteam.DuckTech.items.DTItems;

import javax.annotation.Nullable;

public class ThermalEssenceMakerBlockEntity extends BlockEntity implements MenuProvider, Container {   // ✅ 实现 Container

    public static final int SLOT_INPUT = 0;
    public static final int SLOT_OUTPUT = 1;
    public static final int INVENTORY_SIZE = 2;
    public static final int MAX_PROGRESS = 100; // 5 秒（100 ticks）

    private final ItemStackHandler itemHandler = new ItemStackHandler(INVENTORY_SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return slot == SLOT_INPUT && getBurnTime(stack) >= 20;
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (slot == SLOT_INPUT && getBurnTime(stack) < 20) return stack;
            return super.insertItem(slot, stack, simulate);
        }
    };

    private int progress = 0;
    private int maxProgress = MAX_PROGRESS;
    private int outputCount = 0;   // 当前物品完成后要产出的精华数量

    // 客户端同步字段（目前未使用，但保留无妨）
    private int clientProgress = 0;
    private int clientMaxProgress = MAX_PROGRESS;

    public ThermalEssenceMakerBlockEntity(BlockPos pos, BlockState state) {
        super(DTBlockEntity.THERMAL_ESSENCE_MAKER.get(), pos, state);
    }

    // 获取燃烧时间，使用 ForgeHooks 兼容所有模组
    private static int getBurnTime(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack, null);
    }

    // 尝试开始处理输入物品
    private void tryStartProcessing() {
        if (outputCount > 0) return; // 已有任务在进行

        ItemStack input = itemHandler.getStackInSlot(SLOT_INPUT);
        if (input.isEmpty()) return;

        int burn = getBurnTime(input);
        if (burn < 20) return; // 燃烧时间不足，无法产出

        int count = burn / 20;
        ItemStack output = itemHandler.getStackInSlot(SLOT_OUTPUT);
        ItemStack essence = new ItemStack(DTItems.THERMAL_ESSENCE.get());
        int maxStack = essence.getMaxStackSize();

        // 检查输出槽是否能接纳至少 1 个精华
        if (!output.isEmpty() &&
                (!output.is(essence.getItem()) || output.getCount() + count > maxStack)) {
            return;
        }

        outputCount = count;
        progress = 0;
        maxProgress = MAX_PROGRESS;
        setChanged();
    }

    public void tickInternal() {
        if (level == null || level.isClientSide) return;

        // 没有活跃任务时尝试启动
        if (outputCount <= 0) {
            tryStartProcessing();
            if (outputCount <= 0) {
                // 若依然没有，置零进度并返回
                if (progress != 0) {
                    progress = 0;
                    setChanged();
                }
                return;
            }
        }

        // 检查输入物品是否仍存在且有效
        ItemStack input = itemHandler.getStackInSlot(SLOT_INPUT);
        if (input.isEmpty() || getBurnTime(input) < 20) {
            // 原料无效，取消加工
            outputCount = 0;
            progress = 0;
            setChanged();
            return;
        }

        // 增加进度
        progress++;
        if (progress >= maxProgress) {
            // 消耗输入物品
            input.shrink(1);

            // 产出精华
            ItemStack output = itemHandler.getStackInSlot(SLOT_OUTPUT);
            ItemStack essence = new ItemStack(DTItems.THERMAL_ESSENCE.get());
            if (output.isEmpty()) {
                itemHandler.setStackInSlot(SLOT_OUTPUT, essence.copy());
                essence.setCount(outputCount);
            } else {
                output.grow(outputCount);
            }

            outputCount = 0;
            progress = 0;
            setChanged();

            // 自动尝试开始下一个物品
            tryStartProcessing();
        }
        setChanged(); // 每 tick 标记同步
    }

    // 以下用于同步数据和菜单
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("progress", progress);
        tag.putInt("maxProgress", maxProgress);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("progress");
        maxProgress = tag.getInt("maxProgress");
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        handleUpdateTag(pkt.getTag());
    }

    // 菜单提供
    @Override
    public Component getDisplayName() {
        return Component.translatable("block.ducktech.thermal_essence_maker");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player) {
        return new ThermalEssenceMakerMenu(containerId, playerInv, this);
    }

    public Container getInventory() {
        return this;
    }

    public int getProgress() {
        return progress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    // ========== Container 接口实现（委托给 itemHandler） ==========
    @Override
    public int getContainerSize() {
        return itemHandler.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = itemHandler.extractItem(slot, amount, false);
        setChanged();
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = itemHandler.getStackInSlot(slot);
        itemHandler.setStackInSlot(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        itemHandler.setStackInSlot(slot, stack);
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        if (level == null || level.getBlockEntity(worldPosition) != this) return false;
        return player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5) <= 64.0;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            itemHandler.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ThermalEssenceMakerBlockEntity entity) {
        if (level.isClientSide) return;
        entity.tickInternal();
    }
}