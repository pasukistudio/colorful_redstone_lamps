package de.pasuki.colorful_redstone_lamps.block;

import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jspecify.annotations.NullMarked;

/** A redstone lamp slab, optionally inverted. */
@NullMarked
public class LampSlabBlock extends SlabBlock {
    private final boolean inverted;

    public LampSlabBlock(Properties properties, boolean inverted) {
        super(properties);
        this.inverted = inverted;
        this.registerDefaultState(this.defaultBlockState().setValue(RedstoneLampBlock.LIT, inverted));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(RedstoneLampBlock.LIT);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state == null ? null
                : state.setValue(RedstoneLampBlock.LIT, LampPower.shouldBeLit(context.getLevel(), context.getClickedPos(), inverted));
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock,
                                   @Nullable Orientation orientation, boolean movedByPiston) {
        LampPower.neighborChanged(state, level, pos, this, inverted);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        LampPower.tick(state, level, pos, inverted);
    }
}
