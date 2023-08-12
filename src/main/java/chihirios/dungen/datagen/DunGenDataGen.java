package chihirios.dungen.datagen;

import chihirios.dungen.DunGen;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Set;
import java.util.function.Function;

public class DunGenDataGen {
    private static ResourceKey<StructureTemplatePool> pool(String path) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, DunGen.id(path));
    }

    private static Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer> template(String path, int weight) {
        return Pair.of(StructurePoolElement.single(DunGen.id(path).toString()), weight);
    }

    @SubscribeEvent
    public static void doDataGen(GatherDataEvent event) {
        event.getGenerator().addProvider(event.includeServer(), DunGenDataGen.createProvider(event));
    }

    public static DataProvider.Factory<DatapackBuiltinEntriesProvider> createProvider(GatherDataEvent event) {
        return output -> new DatapackBuiltinEntriesProvider(output,
                event.getLookupProvider(),
                new RegistrySetBuilder().add(Registries.TEMPLATE_POOL, DunGenDataGen::makePools),
                Set.of(DunGen.MODID)
        );
    }

    private static void makePools(BootstapContext<StructureTemplatePool> bootstrap) {
        HolderGetter<StructureTemplatePool> templatePools = bootstrap.lookup(Registries.TEMPLATE_POOL);
        Holder<StructureTemplatePool> emptyPool = templatePools.getOrThrow(Pools.EMPTY);

        bootstrap.register(pool("dungeon/red"), new StructureTemplatePool(
                emptyPool,
                ImmutableList.of(
                        template("redwalk", 2)
                ),
                StructureTemplatePool.Projection.RIGID
        ));

        bootstrap.register(pool("dungeon/start"), new StructureTemplatePool(
                emptyPool,
                ImmutableList.of(
                        template("start", 1)
                ),
                StructureTemplatePool.Projection.RIGID
        ));
    }
}
