package org.quiltmc.users.duckteam.DuckTech.blocks.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.users.duckteam.DuckTech.blocks.DTBlockEntity;
import org.quiltmc.users.duckteam.DuckTech.gui.levitation.LevitationMachineMenu;
import org.quiltmc.users.duckteam.DuckTech.items.DTItems;
import org.quiltmc.users.duckteam.DuckTech.sounds.DTSounds;

import java.util.List;

public class LevitationMachineBlockEntity extends BlockEntity implements MenuProvider {
    public final ItemStackHandler itemStackHandler = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
            super.onContentsChanged(slot);
        }
    };

        private int levitationTime = 0;
        private static final int MAX_LEVITATION_TIME = 600; // 30秒
        private boolean isLevitating = false;

        public void tick(Level level, BlockPos pos, BlockState state) {
            if (level.isClientSide) return;

            // 检查是否有AIR_ESSENCE物品，如果有则启动效果
            if (!isLevitating && itemStackHandler.getStackInSlot(0).getItem().equals(DTItems.AIR_ESSENCE.get())) {
                if (!level.isClientSide()) {
                    level.playSound(null, pos,
                            DTSounds.ZAOYIN.get(),
                            SoundSource.BLOCKS,
                            1.0F,
                            1.0F);
                }
                // 消耗一个物品并启动效果
                itemStackHandler.getStackInSlot(0).shrink(1);
                startLevitationEffect();
            }

            // 如果正在应用漂浮效果
            if (isLevitating) {
                levitationTime++;
                data.set(0 ,this.levitationTime);
                setChanged();

                // 获取周围15格范围内的所有生物
                List<LivingEntity> entities = level.getEntitiesOfClass(
                        LivingEntity.class,
                        getBoundingBox(pos, 15),
                        entity -> !(entity instanceof Player) || !entity.isSpectator()
                );

                // 对每个生物应用漂浮效果
                for (LivingEntity entity : entities) {
                    // 给予漂浮效果 (Levitation I, 持续2秒，但会持续刷新)
                    entity.addEffect(new MobEffectInstance(
                            MobEffects.LEVITATION,  // 漂浮效果
                            40,                     // 持续时间 (2秒，会在下次tick时刷新)
                            1,                      // 效果等级 (Levitation I)
                            true,                   // 显示粒子效果
                            true                    // 显示图标
                    ));
                }

                // 检查是否达到30秒
                if (levitationTime >= MAX_LEVITATION_TIME) {
                    // 停止效果
                    isLevitating = false;
                    levitationTime = 0;
                }
            }
        }

        public void drops() {
            SimpleContainer inv = new SimpleContainer(itemStackHandler.getSlots());
            for (int i = 0; i < itemStackHandler.getSlots(); i++) {
                inv.setItem(i, itemStackHandler.getStackInSlot(i));
            }
            Containers.dropContents(this.level, this.worldPosition, inv);
        }

        @Override
        public CompoundTag getUpdateTag() {
            return saveWithoutMetadata();
        }
        @Override
        public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
            return ClientboundBlockEntityDataPacket.create(this);
        }

        private AABB getBoundingBox(BlockPos pos, int radius) {
            return new AABB(
                    pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius,
                    pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius
            );
        }

        private void startLevitationEffect() {
            isLevitating = true;
            levitationTime = 0;
        }

        // 添加漏斗适配功能
        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            if (cap == ForgeCapabilities.ITEM_HANDLER) {

                // 创建输入处理器（只允许插入）
                LazyOptional<IItemHandler> inputHandler = LazyOptional.of(() -> new IItemHandler() {
                    @Override
                    public int getSlots() {
                        return itemStackHandler.getSlots();
                    }

                    @Override
                    public ItemStack getStackInSlot(int slot) {
                        // 不允许通过漏斗查看内部物品
                        return ItemStack.EMPTY;
                    }

                    @Override
                    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                        // 只允许向槽位0插入
                        if (slot == 0 && stack.getItem().equals(DTItems.AIR_ESSENCE.get())) {
                            return itemStackHandler.insertItem(slot, stack, simulate);
                        }
                        return stack;
                    }

                    @Override
                    public ItemStack extractItem(int slot, int amount, boolean simulate) {
                        // 不允许通过漏斗提取物品
                        return ItemStack.EMPTY;
                    }

                    @Override
                    public int getSlotLimit(int slot) {
                        return itemStackHandler.getSlotLimit(slot);
                    }

                    @Override
                    public boolean isItemValid(int slot, ItemStack stack) {
                        // 只有槽位0可以接受AIR_ESSENCE物品
                        return slot == 0 && stack.getItem().equals(DTItems.AIR_ESSENCE.get());
                    }
                });
                return inputHandler.cast();
            }
            return super.getCapability(cap, side);
        }

        protected final ContainerData data;
        public LevitationMachineBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
            super(DTBlockEntity.LEVITATION_MACHINE_BLOCK_ENTITY.get(), p_155229_, p_155230_);
            data = new SimpleContainerData(1);

        }

        @Override
        public Component getDisplayName() {
            return Component.translatable("block.ducktech.levitation_machine");
        }

        @Override
        public @Nullable AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
            return new LevitationMachineMenu(p_39954_, p_39955_, this  ,data );
        }
    }