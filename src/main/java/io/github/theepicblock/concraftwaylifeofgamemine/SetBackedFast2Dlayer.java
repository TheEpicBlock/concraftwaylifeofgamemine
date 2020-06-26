package io.github.theepicblock.concraftwaylifeofgamemine;

import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntCollection;

public class SetBackedFast2Dlayer extends Fast2Dlayer{
    @Override
    protected IntCollection getNewBacking() {
        return new IntArraySet();
    }
}
