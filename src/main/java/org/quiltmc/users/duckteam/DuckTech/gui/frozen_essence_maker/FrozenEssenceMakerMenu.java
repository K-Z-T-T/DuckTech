package org.quiltmc.users.duckteam.DuckTech.gui.frozen_essence_maker;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import org.quiltmc.users.duckteam.DuckTech.blocks.DTBlocks;
import org.quiltmc.users.duckteam.DuckTech.blocks.blockentity.FrozenEssenceMakerBlockEntity;
import org.quiltmc.users.duckteam.DuckTech.gui.DTMenu;

import javax.annotation.Nullable;

public class FrozenEssenceMakerMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;
    private final ContainerData data;

    // 客户端构造（通过工厂传递位置）
    public FrozenEssenceMakerMenu(int containerId, Inventory playerInv, BlockPos pos) {
        this(containerId, playerInv, ContainerLevelAccess.create(playerInv.player.level(), pos), null);
    }

    // 服务端构造（带实体）
    public FrozenEssenceMakerMenu(int containerId, Inventory playerInv, FrozenEssenceMakerBlockEntity entity) {
        this(containerId, playerInv,
                entity != null ? ContainerLevelAccess.create(entity.getLevel(), entity.getBlockPos()) : ContainerLevelAccess.NULL,
                entity);
    }
    public int getScaleArrowProgress(){
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int arrowPixelSize = 24;

        return maxProgress != 0 &&  progress != 0 ? progress * arrowPixelSize / maxProgress : 0;
    }
    // 私有构造，统一处理
    private FrozenEssenceMakerMenu(int containerId, Inventory playerInv, ContainerLevelAccess access,
                                   @Nullable FrozenEssenceMakerBlockEntity entity) {
        super(DTMenu.FROZEN_ESSENCE_MAKER_MENU.get(), containerId);
        this.access = access;

        // 添加方块实体槽位（仅服务端可用，客户端entity为null但不会调用此槽位）
        if (entity != null) {
            addSlot(new SlotItemHandler(entity.getItemHandler(), 0, 56, 35));
            addSlot(new SlotItemHandler(entity.getItemHandler(), 1, 116, 35));
        }

        // 同步进度数据（客户端通过ContainerData读取）
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                if (entity != null) {
                    return switch (index) {
                        case 0 -> entity.getProgress();
                        case 1 -> entity.getMaxProgress();
                        default -> 0;
                    };
                }
                return 0; // 客户端会通过数据包覆盖
            }
            @Override public void set(int index, int value) {}
            @Override public int getCount() { return 2; }
        };
        addDataSlots(data);

        // 玩家物品栏
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
        for (int k = 0; k < 9; ++k)
            addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
    }

    public int getProgress() { return data.get(0); }
    public int getMaxProgress() { return data.get(1); }

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(access, player, DTBlocks.FROZEN_ESSENCE_MAKER.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // 保持原有逻辑
        ItemStack result = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            if (index < 2) {
                if (!moveItemStackTo(stack, 2, 38, true)) return ItemStack.EMPTY;
            } else {
                if (!moveItemStackTo(stack, 0, 1, false)) return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();
        }
        return result;
    }
}