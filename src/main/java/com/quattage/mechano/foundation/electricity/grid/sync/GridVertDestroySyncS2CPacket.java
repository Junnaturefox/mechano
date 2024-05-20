package com.quattage.mechano.foundation.electricity.grid.sync;

import java.util.function.Supplier;

import com.quattage.mechano.foundation.electricity.grid.GridClientCache;
import com.quattage.mechano.foundation.network.Packetable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class GridVertDestroySyncS2CPacket implements Packetable {
    
    private final GridSyncPacketType type;
    private final BlockPos pos;

    public GridVertDestroySyncS2CPacket(GridSyncPacketType type, BlockPos pos) {
        this.type = type;
        this.pos = pos;
    }

    public GridVertDestroySyncS2CPacket(FriendlyByteBuf buf) {
        this.type = GridSyncPacketType.get(buf.readInt());
        this.pos = buf.readBlockPos();
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(type.ordinal());
        buf.writeBlockPos(pos);
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            switch(type) {
                case REMOVE:
                    GridClientCache.ofInstance().clearAllOccurancesOf(pos);
                    break;
                default:
                    break;
            }
        });
        return true;
    }
}