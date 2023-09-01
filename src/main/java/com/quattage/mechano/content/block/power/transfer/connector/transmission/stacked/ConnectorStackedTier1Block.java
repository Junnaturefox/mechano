package com.quattage.mechano.content.block.power.transfer.connector.transmission.stacked;

import java.util.ArrayList;

import com.quattage.mechano.MechanoBlockEntities;
import com.quattage.mechano.MechanoBlocks;
import com.quattage.mechano.core.CreativeTabExcludable;
import com.quattage.mechano.core.block.CombinedOrientedBlock;
import com.quattage.mechano.core.block.UpgradableBlock;
import com.quattage.mechano.core.util.ShapeBuilder;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.VoxelShaper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ConnectorStackedTier1Block extends UpgradableBlock implements IBE<ConnectorStackedTier1BlockEntity>, CreativeTabExcludable {

    public static final VoxelShaper SHAPE = ShapeBuilder
            .newShape(2, 0, 3, 14, 6, 13)
            .add(5, 6, 5, 11, 20, 11)
            .defaultUp();

    public ConnectorStackedTier1Block(Properties properties) {
        super(properties, MechanoBlocks.CONNECTOR_STACKED_ZERO.get(), 1);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE.get(state.getValue(ORIENTATION).getLocalUp());
    }

    @Override
    public Class<ConnectorStackedTier1BlockEntity> getBlockEntityClass() {
        return ConnectorStackedTier1BlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ConnectorStackedTier1BlockEntity> getBlockEntityType() {
        return MechanoBlockEntities.STACKED_CONNECTOR_ONE.get();
    }

    @Override
    protected ArrayList<UpgradableBlock> setUpgradeTiers(ArrayList<UpgradableBlock> upgrades) {
        upgrades.add(MechanoBlocks.CONNECTOR_STACKED_ZERO.get());
        upgrades.add(MechanoBlocks.CONNECTOR_STACKED_ONE.get());
        upgrades.add(MechanoBlocks.CONNECTOR_STACKED_TWO.get());
        upgrades.add(MechanoBlocks.CONNECTOR_STACKED_THREE.get());
        return upgrades;
    }

    @Override
    protected Item setUpgradeItem() {
        return MechanoBlocks.CONNECTOR_TRANSMISSION.asItem();
    }

    @Override
    public BlockState updateAfterWrenched(BlockState newState, UseOnContext context) {
        // TODO Auto-generated method stub
        return super.updateAfterWrenched(newState, context);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos,
            boolean isMoving) {

        Direction under = state.getValue(CombinedOrientedBlock.ORIENTATION).getLocalUp().getOpposite();
        if(world.getBlockState(pos.relative(under)).getBlock() != MechanoBlocks.TRANSMISSION_NODE.get())
            world.destroyBlock(pos, false);
        super.neighborChanged(state, world, pos, block, fromPos, isMoving);
    }

}
