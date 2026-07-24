package org.quiltmc.users.duckteam.DuckTech.gui.transporter_node;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.quiltmc.users.duckteam.DuckTech.blocks.DTBlocks;
import org.quiltmc.users.duckteam.DuckTech.gui.DTMenu;

public class TransporterNodeMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;

    // 构造函数：现在需要 access 参数
    public TransporterNodeMenu(int windowId, Inventory playerInv,
                               IItemHandler nodeInventory,
                               ContainerLevelAccess access) {
        super(DTMenu.TRANSPORTER_NODE_MENU.get(), windowId);
        this.access = access;

        // 节点内部槽位（前 27 格）
        // slot 0 可用，其余锁定
        for (int i = 0; i < 27; i++) {
            int x = 8 + (i % 9) * 18;
            int y = 18 + (i / 9) * 18;
            if (i == 0) {
                this.addSlot(new SlotItemHandler(nodeInventory, i, x, y));
            } else {
                this.addSlot(new LockedSlot(nodeInventory, i, x, y));
            }
        }

        // 玩家背包（27 格） + 快捷栏（9 格）
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9,
                        8 + col * 18, 140 + row * 18));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInv, col, 8 + col * 18, 198));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            itemstack = stackInSlot.copy();

            // 情况1：点击的是容器槽位 (0~8) -> 移到玩家背包
            if (index < 9) {
                if (!this.moveItemStackTo(stackInSlot, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            }
            // 情况2：点击的是玩家背包 (9~44) -> 移到容器
            else if (!this.moveItemStackTo(stackInSlot, 0, 9, false)) {
                return ItemStack.EMPTY;
            }

            // 如果原槽位物品被清空，则清空槽位
            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            // 若转移后数量没有变化（目标已满），返回 EMPTY 表示失败
            if (stackInSlot.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            // 触发槽位变化事件
            slot.onTake(player, stackInSlot);
        }

        return itemstack;
    }

    // 关键修复：实现 stillValid
    @Override
    public boolean stillValid(Player player) {
        return access.evaluate((level, pos) ->
                        level.getBlockState(pos).is(DTBlocks.TRANSPORTER_NODE.get())
                                && player.distanceToSqr(pos.getX() + 0.5,
                                pos.getY() + 0.5,
                                pos.getZ() + 0.5) <= 64.0,
                true); // 若访问失败（例如世界已卸载），返回 true 以防止意外关闭（或按需）
    }

    // 锁定槽位（不可放取）
    private static class LockedSlot extends SlotItemHandler {
        public LockedSlot(IItemHandler handler, int index, int x, int y) {
            super(handler, index, x, y);
        }
        @Override public boolean mayPlace(ItemStack stack) { return false; }
        @Override public boolean mayPickup(Player player) { return false; }
    }

    // 快速移动物品方法（需自行完善），此处略
}