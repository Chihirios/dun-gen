package chihirios.dungen;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DunGen.MODID)
public class DunGen
{
    public static final String MODID = "dun_gen";
    public static final Logger LOGGER = LogUtils.getLogger();

    public DunGen() {
        MinecraftForge.EVENT_BUS.register(DunGenCommand.class);
    }
}
