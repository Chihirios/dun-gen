package chihirios.dungen;

import chihirios.dungen.generator.DunGenData;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static net.minecraft.commands.Commands.literal;

public class DunGenCommand {
    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                literal("dun-gen")
                        .then(literal("generate")
                                .executes(ctx -> {
                                    // generate new dungeon and take player there
                                    DunGen.LOGGER.info("" + DunGenData.get(ctx.getSource().getServer()).increment());
                                    return 0;
                                }))
        );
    }
}
