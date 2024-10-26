package world.maryt.game_opening_for_you;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import world.maryt.game_opening_for_you.handler.GameOpeningHandler;

import java.io.File;


@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION, clientSideOnly = true)
public class GameOpeningForYou {
    public static String MOD_ID = Tags.MOD_ID;
    public static String MOD_NAME = Tags.MOD_NAME;
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static int daytimeBeginningHour = 8;
    public static int daytimeEndHour = 18;
    public static int lateNightBeginningHour = 23;

    public static int daytimeBeginningMinute = 0;
    public static int daytimeEndMinute = 30;
    public static int lateNightBeginningMinute = 30;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent preEvent) {
        Configuration config = new Configuration(new File(Loader.instance().getConfigDir(), Tags.MOD_ID + ".cfg"));
        try {
            config.load();
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "daytimeBeginningHour", 8);
                property.setComment("The time of day you believe daylight begins during the day, expressed in 24-hour format. The default value is 8:00.");
                GameOpeningForYou.daytimeBeginningHour = property.getInt();
                property.setShowInGui(true);
            }
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "daytimeEndHour", 18);
                property.setComment("The time of day you believe daylight ends during the day, expressed in 24-hour format. The default value is 18:30.");
                GameOpeningForYou.daytimeEndHour = property.getInt();
                property.setShowInGui(true);
            }
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "lateNightBeginningHour", 23);
                property.setComment("The time of day you think late night starts, expressed in 24-hour format. The default value is 23:30.");
                GameOpeningForYou.lateNightBeginningHour = property.getInt();
                property.setShowInGui(true);
            }
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "daytimeBeginningMinute", 0);
                property.setComment("The time of day you believe daylight begins during the day, expressed in 24-hour format. The default value is 8:00.");
                GameOpeningForYou.daytimeBeginningMinute = property.getInt();
                property.setShowInGui(true);
            }
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "daytimeEndMinute", 30);
                property.setComment("The time of day you believe daylight ends during the day, expressed in 24-hour format. The default value is 18:30.");
                GameOpeningForYou.daytimeEndMinute = property.getInt();
                property.setShowInGui(true);
            }
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "lateNightBeginningMinute", 30);
                property.setComment("The time of day you think late night starts, expressed in 24-hour format. The default value is 23:30.");
                GameOpeningForYou.lateNightBeginningMinute = property.getInt();
                property.setShowInGui(true);
            }

            LOGGER.info("Configuration loaded. Preparing Game Opening For You.");
        } finally {
            config.save();
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new GameOpeningHandler());
    }
}
