package org.quiltmc.users.duckteam.DuckTech.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.quiltmc.users.duckteam.DuckTech.blocks.blockentity.EssenceEarthFurnaceBlockEntity;

import javax.annotation.Nullable;

public class EssenceEarthFurnace extends AbstractFurnaceBlock {
    public EssenceEarthFurnace(Properties properties) {
        super(properties);
    }

    @Override
    protected void openContainer(Level level, BlockPos pos, Player player) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof EssenceEarthFurnaceBlockEntity) {
            player.openMenu((MenuProvider) be);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EssenceEarthFurnaceBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof MenuProvider provider) {
                player.openMenu(provider);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}