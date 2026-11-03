package de.pasuki.colorful_redstone_lamps.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.state.BlockState;

/** Redstone behavior shared by all lamp shapes: normal lamps light up when powered, inverted ones when unpowered. */
final class LampPower {
    private static final int SHUTDOWN_DELAY_TICKS = 4;

    private LampPower() {
    }

    static boolean shouldBeLit(Level level, BlockPos pos, boolean inverted) {
        return level.hasNeighborSignal(pos) != inverted;
    }

    static void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, boolean inverted) {
        if (level.isClientSide()) {
            return;
        }

        boolean lit = state.getValue(RedstoneLampBlock.LIT);
        if (lit == shouldBeLit(level, pos, inverted)) {
            return;
        }

        if (lit) {
            // Switching off is delayed so short pulses do not flicker.
            level.scheduleTick(pos, block, SHUTDOWN_DELAY_TICKS);
        } else {
            level.setBlock(pos, state.setValue(RedstoneLampBlock.LIT, true), Block.UPDATE_CLIENTS);
        }
    }

    static void tick(BlockState state, ServerLevel level, BlockPos pos, boolean inverted) {
        if (state.getValue(RedstoneLampBlock.LIT) && !shouldBeLit(level, pos, inverted)) {
            level.setBlock(pos, state.setValue(RedstoneLampBlock.LIT, false), Block.UPDATE_CLIENTS);
        }
    }
}
