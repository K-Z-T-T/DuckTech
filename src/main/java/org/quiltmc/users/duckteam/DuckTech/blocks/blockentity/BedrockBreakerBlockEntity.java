package org.quiltmc.users.duckteam.DuckTech.blocks.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.quiltmc.users.duckteam.DuckTech.blocks.DTBlockEntity;

import java.util.List;

public class BedrockBreakerBlockEntity extends BlockEntity {

    public BedrockBreakerBlockEntity(BlockPos pos, BlockState state) {
        super(DTBlockEntity.BEDROCK_BREAKER_BE.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, BedrockBreakerBlockEntity be) {
        be.tick();
    }

    private void tick() {
        if (level == null || level.isClientSide) return;

        // 检测方块上方一格的物品实体（扩大检测范围避免遗漏）
        AABB area = new AABB(worldPosition).move(0, 1, 0).expandTowards(0, 0.5, 0);
        List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, area);

        for (ItemEntity itemEntity : items) {
            // 必须为 64 个钻石
            if (itemEntity.getItem().getCount() == 64 &&
                    itemEntity.getItem().is(Items.DIAMOND)) {

                // 消耗钻石
                itemEntity.discard();

                // 破坏下方方块（仅基岩）
                BlockPos below = worldPosition.below();
                if (level.getBlockState(below).is(Blocks.BEDROCK)) {
                    // 直接替换为空气，绝不掉落物品
                    level.setBlock(below, Blocks.AIR.defaultBlockState(), 3);
                }
                // 只处理一次（防止多个物品堆叠同时触发）
                break;
            }
        }
    }
}