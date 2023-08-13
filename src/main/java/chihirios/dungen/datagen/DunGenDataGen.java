package chihirios.dungen.datagen;

import chihirios.dungen.DunGen;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.PillagerOutpostPools;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.data.worldgen.SnowyVillagePools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class DunGenDataGen {
    private static ResourceKey<StructureTemplatePool> pool(String path) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, DunGen.id(path));
    }
    private static ResourceKey<Structure> structurePool(String path) {
        return ResourceKey.create(Registries.STRUCTURE, DunGen.id(path));
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
                new RegistrySetBuilder().add(Registries.TEMPLATE_POOL, DunGenDataGen::makePools).add(Registries.STRUCTURE, DunGenDataGen::makeStructure),
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

    private static Structure.StructureSettings structure(HolderSet<Biome> pBiomes, Map<MobCategory, StructureSpawnOverride> pSpawnOverrides, GenerationStep.Decoration pStep, TerrainAdjustment pTerrainAdaptation) {
        return new Structure.StructureSettings(pBiomes, pSpawnOverrides, pStep, pTerrainAdaptation);
    }

    private static Structure.StructureSettings structure(HolderSet<Biome> pBiomes, GenerationStep.Decoration pStep, TerrainAdjustment pTerrainAdaptation) {
        return structure(pBiomes, Map.of(), pStep, pTerrainAdaptation);
    }

    private static Structure.StructureSettings structure(HolderSet<Biome> pBiomes, TerrainAdjustment pTerrainAdaptation) {
        return structure(pBiomes, Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, pTerrainAdaptation);
    }
    private static void makeStructure(BootstapContext<Structure> pContext) {

        HolderGetter<Biome> holdergetter = pContext.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> holdergetter1 = pContext.lookup(Registries.TEMPLATE_POOL); // register new pools here for lookup

        pContext.register(structurePool("dungeon"), new JigsawStructure(
                structure(holdergetter.getOrThrow(BiomeTags.HAS_VILLAGE_SNOWY), TerrainAdjustment.NONE),
                holdergetter1.getOrThrow(SnowyVillagePools.START),
                6,
                ConstantHeight.of(VerticalAnchor.absolute(60)),
                false
        ));

    }

}
