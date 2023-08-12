package chihirios.dungen;

import chihirios.dungen.generator.DunGenData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static net.minecraft.commands.Commands.literal;

public class DunGenCommand {
    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                literal("dungen")
                        .then(literal("generate")
                                .executes(ctx -> {
                                    // generate new dungeon and take player there
                                    var index = DunGenData.get(ctx.getSource().getServer()).increment();
                                    var dungeonDim = ctx.getSource().getServer().getLevel(ResourceKey.create(Registries.DIMENSION, DunGen.id("dungeon")));
                                    var structure = ctx.getSource().getServer().getStructureManager().get(DunGen.id("start")).orElse(null);
                                    if (dungeonDim == null || structure == null) return 0;
                                    var pos = new BlockPos(index * 1000, 0, 0);
                                    // var bounds = structure.getBoundingBox(pos, Rotation.NONE, pos, Mirror.NONE);
                                    structure.placeInWorld(dungeonDim, pos, pos, new StructurePlaceSettings(), ctx.getSource().getLevel().random, Block.UPDATE_ALL);
                                    ctx.getSource().getPlayerOrException().teleportTo(dungeonDim, pos.getX() + 3, pos.getY() + 1, pos.getZ() + 3, 0f, 0f);
                                    return 0;
                                }))
        );
    }
}
