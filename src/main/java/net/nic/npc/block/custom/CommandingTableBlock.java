package net.nic.npc.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import net.nic.npc.data.KingdomSaveData;
import net.nic.npc.kingdom.KingdomInfo;

import net.nic.npc.kingdom.KingdomManager;
import net.nic.npc.menu.menus.CommandingTableMenu;
import org.jetbrains.annotations.Nullable;


public class CommandingTableBlock extends Block {

    public KingdomInfo kingdom;

    public CommandingTableBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);

        // Only proceed if we are on the server side and pLevel is a ServerLevel
        if (!pLevel.isClientSide && pLevel instanceof ServerLevel serverLevel && pPlacer instanceof Player player) {
            if (!KingdomManager.hasKingdom(player.getUUID(), serverLevel)) {
                this.kingdom = new KingdomInfo("kingdomNameRandom", serverLevel, player);
                KingdomManager.registerKingdom(player.getUUID(), this.kingdom, serverLevel);
            } else {
                this.kingdom = KingdomManager.getKingdom(player.getUUID(), serverLevel);
            }
        }



        /*
        if (pPlacer instanceof Player) {
            ServerPlayer serverPlayer = (ServerPlayer) pPlacer;
            MenuProvider provider = new SimpleMenuProvider((containerId, playerInventory, _player) -> new CommandingTableMenu(containerId, playerInventory), Component.translatable("npc.gui.commanding_table"));
            serverPlayer.openMenu(provider);
        }

         */
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        //  we're on the server side bro???
        if (!pLevel.isClientSide) {

            // WE ARE SO MAKE IT A SERVERLEVELE
            if (pLevel instanceof ServerLevel serverLevel) {
                ItemStack pStack = pPlayer.getMainHandItem();

                // IF eat then good
                if (isEdible(pStack)) {
                    pStack.shrink(1);
                    pLevel.playSound(pPlayer, pPos, SoundEvents.AMETHYST_BLOCK_STEP, SoundSource.BLOCKS, 1f, 1f);
                    kingdom.addFoodValue((int) (getFoodNutrition(pStack) * 30));
                } else {
                    // if can't eat then no good
                    if (pPlayer instanceof ServerPlayer serverPlayer) {
                        CommandingTableMenu.setPlayer(pPlayer);
                        MenuProvider provider = new SimpleMenuProvider(
                                (containerId, playerInventory, _player) ->
                                        new CommandingTableMenu(containerId, playerInventory, pPlayer),
                                Component.translatable("npc.gui.commanding_table")
                        );

                        serverPlayer.openMenu(provider);
                    }
                    kingdom.updateNeededFood();
                }
            } else {


                return InteractionResult.FAIL;
            }
        }

        KingdomSaveData saveData = KingdomSaveData.get((ServerLevel) pPlayer.level());
        saveData.setDirty();
        return InteractionResult.SUCCESS;
    }


    @Override
    protected boolean isRandomlyTicking(BlockState pState) {
        return true;
    }
    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        super.randomTick(state, world, pos, random);

        // 33%
        if (random.nextInt(3) == 0) {
            if (kingdom.getFoodValue() > 0 && !(kingdom.getFoodValue() < kingdom.getNeededFood()/10)) {
                kingdom.addFoodValue(-(kingdom.getNeededFood()/10));
            } else if (kingdom.getFoodValue() < kingdom.getNeededFood()/10) {
                kingdom.setFoodValue(0);
            }
       }
    }

    public boolean isEdible(ItemStack stack)    {
        FoodProperties foodProperties = (FoodProperties) stack.get(DataComponents.FOOD);
        if (foodProperties != null) {
            return true;
        }
        else return false;
    }

    public float getFoodNutrition(ItemStack stack) {
        ItemStack itemstack = stack;
        FoodProperties foodproperties = (FoodProperties) itemstack.get(DataComponents.FOOD);
        if (foodproperties != null) {
            return foodproperties.nutrition();
        } else
            return 0;
    }
}
