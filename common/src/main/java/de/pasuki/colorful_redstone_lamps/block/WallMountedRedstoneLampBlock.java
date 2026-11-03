package de.pasuki.colorful_redstone_lamps.block;

import org.jetbrains.annotations.Nullable;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/** A two-pixel-deep redstone lamp panel that can be placed on every solid block face, optionally inverted. */
@MethodsReturnNonnullByDefault
public class WallMountedRedstoneLampBlock extends RedstoneLampBlock {
    public static final EnumProperty<Direction> FACING = EnumProperty.create("facing", Direction.class);

    private final boolean inverted;

    public WallMountedRedstoneLampBlock(Properties properties, boolean inverted) {
        super(properties);
        this.inverted = inverted;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(LIT, inverted));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getClickedFace();
        BlockState state = this.defaultBlockState().setValue(FACING, facing).setValue(LIT, LampPower.shouldBeLit(context.getLevel(), context.getClickedPos(), inverted));
        return state.canSurvive(context.getLevel(), context.getClickedPos()) ? state : null;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos supportPos = pos.relative(state.getValue(FACING).getOpposite());
        return level.getBlockState(supportPos).isFaceSturdy(level, supportPos, state.getValue(FACING));
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
                                     Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (direction == state.getValue(FACING).getOpposite() && !state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, level, ticks, pos, direction, neighborPos, neighborState, random);
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

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        // Panels are 2 pixels deep and sit against the block opposite to their facing.
        return switch (state.getValue(FACING)) {
            case UP -> Block.box(0, 0, 0, 16, 2, 16);
            case DOWN -> Block.box(0, 14, 0, 16, 16, 16);
            case NORTH -> Block.box(0, 0, 14, 16, 16, 16);
            case SOUTH -> Block.box(0, 0, 0, 16, 16, 2);
            case EAST -> Block.box(0, 0, 0, 2, 16, 16);
            case WEST -> Block.box(14, 0, 0, 16, 16, 16);
        };
    }
}
