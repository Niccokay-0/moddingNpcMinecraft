package net.nic.npc.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.pathfinder.Path;
import net.nic.npc.entity.EntityNpcCitizen;

import java.util.EnumSet;

public class GoToSpecialBlockGoal extends Goal {

    private final EntityNpcCitizen mob;
    private final Block targetBlock;
    private final double speedModifier;
    private final int searchRadius;

    private BlockPos destinationBlock = null;
    private Path path = null;

    public GoToSpecialBlockGoal(EntityNpcCitizen mob, Block targetBlock, double speedModifier, int searchRadius) {
        this.mob = mob;
        this.targetBlock = targetBlock;
        this.speedModifier = speedModifier;
        this.searchRadius = searchRadius;
        this.setFlags(EnumSet.of(Flag.MOVE));

    }

    @Override
    public boolean canUse() {
        if (mob.getRandom().nextInt(3) != 0) {
            return false;
        }

        BlockPos mobPos = mob.blockPosition();
        Level level = mob.level();

        for (BlockPos pos : BlockPos.betweenClosed(mobPos.offset(-searchRadius, -2, -searchRadius), mobPos.offset(searchRadius, 2, searchRadius))) {
            if (level.getBlockState(pos).is(targetBlock)) {
                destinationBlock = pos.immutable();
                path = mob.getNavigation().createPath(destinationBlock, 4);
                return path != null;
            }
        }
        mob.setRecruitable();
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return path != null && !mob.getNavigation().isDone();
    }

    @Override
    public void start() {
        if (path != null) {
            mob.getNavigation().moveTo(path, speedModifier);
        }
    }

    @Override
    public void stop() {
        destinationBlock = null;
        path = null;
    }

    public boolean isNearTargetBlock() {

        return destinationBlock != null && mob.blockPosition().closerThan(destinationBlock, 6.0);
    }

    public BlockPos getTargetBlock() {
        return destinationBlock;
    }
}
