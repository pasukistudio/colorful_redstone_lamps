package de.pasuki.colorful_redstone_lamps.block;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

/** Redstone lamp stairs, optionally inverted. */
@MethodsReturnNonnullByDefault
public class LampStairBlock extends StairBlock {
    private final boolean inverted;

    public LampStairBlock(Properties properties, boolean inverted) {
        // The base state only feeds vanilla stair callbacks; a lamp here would turn the stairs into a lamp when powered.
        super(Blocks.GLASS.defaultBlockState(), properties);
        this.inverted = inverted;
        this.registerDefaultState(this.defaultBlockState().setValue(RedstoneLampBlock.LIT, inverted));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(RedstoneLampBlock.LIT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state.setValue(RedstoneLampBlock.LIT, LampPower.shouldBeLit(context.getLevel(), context.getClickedPos(), inverted));
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos,
                                Block block, BlockPos fromPos, boolean isMoving) {
        LampPower.neighborChanged(state, level, pos, this, inverted);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        LampPower.tick(state, level, pos, inverted);
    }
}
