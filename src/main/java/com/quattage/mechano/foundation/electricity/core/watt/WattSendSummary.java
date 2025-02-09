package com.quattage.mechano.foundation.electricity.core.watt;

import com.quattage.mechano.foundation.electricity.WattBatteryHandler;
import com.quattage.mechano.foundation.electricity.grid.landmarks.GridPath;

import net.minecraft.core.BlockPos;

public class WattSendSummary {
    final WattBatteryHandler<?> destination;
    final BlockPos sourcePos;
    final BlockPos destinationPos;
    final GridPath addressedPath;

    public WattSendSummary(WattBatteryHandler<?> destination, BlockPos sourcePos, BlockPos destinationPos, GridPath addressedPath) {
        this.destination = destination;
        this.sourcePos = sourcePos;
        this.destinationPos = destinationPos;
        this.addressedPath = addressedPath;
    }

    public WattBatteryHandler<?> getDestination() {
        return destination;
    }

    public BlockPos getDestinationPos() {
        return destinationPos;
    }

    public BlockPos getSourcePos() {
        return sourcePos;
    }

    public String toString() {
        return destinationPos.toShortString();
    }

    public boolean equals(Object o) {
        if(!(o instanceof WattSendSummary that)) return false;
        return this.destinationPos.equals(that.destinationPos);
    }

    public GridPath getAddressedPath() {
        return addressedPath;
    }
}
