package net.nic.npc.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.nic.npc.menu.CommandingTableMenu;


public class CommandingTableBlock extends Block {

    public CommandingTableBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        pLevel.playSound(pPlayer, pPos, SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.BLOCKS, 1f,1f );

        if (!pLevel.isClientSide) {
                ServerPlayer serverPlayer = (ServerPlayer) pPlayer;
                MenuProvider provider = new SimpleMenuProvider(
                        (containerId, playerInventory, _player) ->
                                new CommandingTableMenu(containerId, playerInventory),
                        Component.translatable("npc.gui.commanding_table")
                );
                serverPlayer.openMenu(provider);
            }


            return InteractionResult.SUCCESS;
        }
}
