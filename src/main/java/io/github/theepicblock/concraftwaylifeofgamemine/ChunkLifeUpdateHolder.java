package io.github.theepicblock.concraftwaylifeofgamemine;

import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import nerdhub.cardinal.components.api.component.extension.CopyableComponent;
import nerdhub.cardinal.components.api.event.ChunkComponentCallback;
import nerdhub.cardinal.components.api.util.ChunkComponent;
import net.minecraft.nbt.CompoundTag;

public class ChunkLifeUpdateHolder implements CopyableComponent {
    @Override
    public void fromTag(CompoundTag compoundTag) {

    }

    @Override
    public CompoundTag toTag(CompoundTag compoundTag) {
        return null;
    }

    @Override
    public ComponentType<?> getComponentType() {
        return null;
    }
}
