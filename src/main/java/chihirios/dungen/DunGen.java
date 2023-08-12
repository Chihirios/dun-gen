package chihirios.dungen;

import chihirios.dungen.datagen.DunGenDataGen;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DunGen.MODID)
public class DunGen {
    public static final String MODID = "dun_gen";

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MODID, path);
    }
    public static final Logger LOGGER = LogUtils.getLogger();

    public DunGen() {
        MinecraftForge.EVENT_BUS.register(DunGenCommand.class);
        FMLJavaModLoadingContext.get().getModEventBus().register(DunGenDataGen.class);
    }
}
