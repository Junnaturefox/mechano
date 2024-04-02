package com.quattage.mechano.content.block.power.alternator.collector;

import com.quattage.mechano.Mechano;
import com.quattage.mechano.MechanoBlockEntities;
import com.quattage.mechano.MechanoBlocks;
import com.quattage.mechano.foundation.block.hitbox.Hitbox;
import com.quattage.mechano.foundation.block.hitbox.HitboxNameable;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Locale;

public class CollectorBlock extends DirectionalKineticBlock implements IBE<CollectorBlockEntity> {

    private static Hitbox<Direction> hitbox;

    public static final EnumProperty<CollectorBlockModelType> MODEL_TYPE = EnumProperty.create("model", CollectorBlockModelType.class);

    public CollectorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(defaultBlockState().setValue(MODEL_TYPE, CollectorBlockModelType.BASE));
    }

    public enum CollectorBlockModelType implements StringRepresentable, HitboxNameable {
        BASE, ROTORED;

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }

        @Override
        public String toString() {
            return getSerializedName();
        }
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
        return 1f;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if(hitbox == null) hitbox = Mechano.HITBOXES.get(FACING, state.getValue(MODEL_TYPE), this);
        return hitbox.getRotated(state.getValue(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction preferred = getPreferredFacing(context);
		if (preferred == null || (context.getPlayer() != null && context.getPlayer()
			.isShiftKeyDown())) {
			Direction nearestLookingDirection = context.getNearestLookingDirection();
			return defaultBlockState().setValue(FACING, context.getPlayer() != null && context.getPlayer()
				.isShiftKeyDown() ? nearestLookingDirection.getOpposite() : nearestLookingDirection);
		}
		return defaultBlockState().setValue(FACING, preferred);
    }


    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        withBlockEntityDo(level, pos, CollectorBlockEntity::updateRotorAndStatorCount);
        super.onNeighborChange(state, level, pos, neighbor);
    }

    @Override
	public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction dir) {
		return dir.getAxis() == state.getValue(FACING).getAxis();
	}

    @Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);
        doRotorCheck(world, pos, state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        super.neighborChanged(state, world, pos, pBlock, pFromPos, pIsMoving);
        if(state.getValue(MODEL_TYPE) == CollectorBlockModelType.ROTORED) {
            Direction ax = state.getValue(FACING);
            if(world.getBlockState(pos.relative(ax)).getBlock() != MechanoBlocks.SMALL_ROTOR.get())
                world.destroyBlock(pos, true);
        }
    }

    private boolean doRotorCheck(Level world, BlockPos pos, BlockState state) {
        Direction ax = state.getValue(FACING);
        if(state.getValue(MODEL_TYPE) == CollectorBlockModelType.ROTORED)
            return true;
        if(world.getBlockState(pos.relative(ax)).getBlock() == MechanoBlocks.SMALL_ROTOR.get()) {
            world.setBlock(pos, state.setValue(MODEL_TYPE, CollectorBlockModelType.ROTORED), Block.UPDATE_ALL);
            return true;
        }
        return false;
    }

    
    @Override
    @SuppressWarnings("deprecation")
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction ax = state.getValue(FACING);
        if(ax.getAxis() == Axis.Y)
            return false;
        if(world.getBlockState(pos.relative(ax)).getBlock() == MechanoBlocks.COLLECTOR.get())
            return false;
        if(world.getBlockState(pos.relative(ax.getOpposite())).getBlock() == MechanoBlocks.COLLECTOR.get())
            return false;
        return super.canSurvive(state, world, pos);
    }

    @Override
    public Class<CollectorBlockEntity> getBlockEntityClass() {
        return CollectorBlockEntity.class;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MODEL_TYPE);
    }

    @Override
    public BlockEntityType<? extends CollectorBlockEntity> getBlockEntityType() {
        return MechanoBlockEntities.COLLECTOR.get();
    }

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    }    
}
