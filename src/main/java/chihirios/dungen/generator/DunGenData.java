package chihirios.dungen.generator;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class DunGenData extends SavedData {
    private static final String KEY = "DungeonIndex";
    private int index = 0;

    @Override
    public @NotNull CompoundTag save(CompoundTag tag) {
        tag.putInt(KEY, index);
        return tag;
    }

    public static DunGenData load(CompoundTag tag) {
        var data = new DunGenData();
        data.index = tag.getInt(KEY);
        return data;
    }

    public static DunGenData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(DunGenData::load, DunGenData::new, "dun-gen");
    }

    public int increment() {
        this.setDirty();
        return index++;
    }
}
