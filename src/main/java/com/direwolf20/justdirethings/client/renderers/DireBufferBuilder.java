package com.direwolf20.justdirethings.client.renderers;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.ints.IntConsumer;

public class DireBufferBuilder extends BufferBuilder {
    //This class exists because sorting in vanilla minecraft is the opposite of how we want to do it
    //So override the sort method (Which needs lots of ATs) and add a reversal line
    public DireBufferBuilder(int pCapacity) {
        super(pCapacity);
    }

    @Override
    public void putSortedQuadIndices(VertexFormat.IndexType pIndexType) {
        if (this.sortingPoints != null && this.sorting != null) {
            int[] aint = this.sorting.sort(this.sortingPoints);
            IntConsumer intconsumer = this.intConsumer(this.nextElementByte, pIndexType);
            // Reverse the order of the sorted indices. The whole reason this class exists is this one line!
            //ArrayUtils.reverse(aint);
            for (int i : aint) {
                intconsumer.accept(i * this.mode.primitiveStride + 0);
                intconsumer.accept(i * this.mode.primitiveStride + 1);
                intconsumer.accept(i * this.mode.primitiveStride + 2);
                intconsumer.accept(i * this.mode.primitiveStride + 2);
                intconsumer.accept(i * this.mode.primitiveStride + 3);
                intconsumer.accept(i * this.mode.primitiveStride + 0);
            }

        } else {
            throw new IllegalStateException("Sorting state uninitialized");
        }
    }
}
