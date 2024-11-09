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
import java.util.Arrays;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION, clientSideOnly = true)
public class GameOpeningForYou {
    public static final String MOD_ID = Tags.MOD_ID;
    public static final String MOD_NAME = Tags.MOD_NAME;
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
    // Only for world.maryt.game_opening_for_you.handler.GameOpeningHandler.clearMessages
    public static final Logger MISSED_MSG_LOGGER = LogManager.getLogger(MOD_NAME + " - Missed Message Collector");

    // Config variables
    public static boolean DEBUG = false;

    public static boolean hourFixDigits = false;
    public static boolean minuteFixDigits = true;
    public static boolean secondFixDigits = true;
    public static boolean monthFixDigits = false;
    public static boolean dayFixDigits = true;

    public static int daytimeBeginningHour = 8;
    public static int daytimeEndHour = 18;
    public static int lateNightBeginningHour = 23;

    public static int daytimeBeginningMinute = 0;
    public static int daytimeEndMinute = 30;
    public static int lateNightBeginningMinute = 30;

    public static int gameOpeningMilliseconds = 5000;

    public static String[] gameOpeningMessageList = new String[]{
            "hardcore_opening",
            "night_opening",
            "late_opening",
            "april_fools_day_opening",
            "halloween_opening",
            "spring_festival_opening"
    };

    // Link line feature
    // Links
    public static String discordLink = "";
    // QQ is an IM software mainly used in China
    public static String qqGroupLink = "";
    public static String curseforgeLink = "";
    public static String modrinthLink = "";

    // Placeholder will be replaced with corresponding display text, and be directed to its link
    // Default texts, to cover the illegal config texts
    public static final String defaultDiscordDisplayText = "game_opening_for_you.discord.display.text";
    public static final String defaultQQGroupDisplayText = "game_opening_for_you.qq_group.display.text";
    public static final String defaultCurseforgeDisplayText = "game_opening_for_you.curseforge.display.text";
    public static final String defaultModrinthDisplayText = "game_opening_for_you.modrinth.display.text";

    public static final String defaultDiscordHoverText = "game_opening_for_you.discord.hover.text";
    public static final String defaultQQGroupHoverText = "game_opening_for_you.qq_group.hover.text";
    public static final String defaultCurseforgeHoverText = "game_opening_for_you.curseforge.hover.text";
    public static final String defaultModrinthHoverText = "game_opening_for_you.modrinth.hover.text";
    // texts will read from config
    public static String discordDisplayText = defaultDiscordDisplayText;
    public static String qqGroupDisplayText = defaultQQGroupDisplayText;
    public static String curseforgeDisplayText = defaultCurseforgeDisplayText;
    public static String modrinthDisplayText = defaultModrinthDisplayText;
    // When player hover mouse pointer on the link display text, it will show a Hover Text
    public static String discordHoverText = "";
    public static String qqGroupHoverText = "";
    public static String curseforgeHoverText = "";
    public static String modrinthHoverText = "";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent preEvent) {
        Configuration config = new Configuration(new File(Loader.instance().getConfigDir(), MOD_ID + ".cfg"));
        try {
            config.load();
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "hourFixDigits", false);
                property.setComment("Whether to fix hour to 2 digits.");
                GameOpeningForYou.hourFixDigits = property.getBoolean();
                property.setShowInGui(true);
            }
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "minuteFixDigits", true);
                property.setComment("Whether to fix minute to 2 digits.");
                GameOpeningForYou.minuteFixDigits = property.getBoolean();
                property.setShowInGui(true);
            }
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "secondFixDigits", true);
                property.setComment("Whether to fix hour to 2 digits.");
                GameOpeningForYou.secondFixDigits = property.getBoolean();
                property.setShowInGui(true);
            }
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "monthFixDigits", false);
                property.setComment("Whether to fix month to 2 digits.");
                GameOpeningForYou.monthFixDigits = property.getBoolean();
                property.setShowInGui(true);
            }
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "hourFixDigits", true);
                property.setComment("Whether to fix day to 2 digits.");
                GameOpeningForYou.dayFixDigits = property.getBoolean();
                property.setShowInGui(true);
            }
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
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "DEBUG", false);
                property.setComment("Display debug logs. For debug purposes.");
                GameOpeningForYou.DEBUG = property.getBoolean();
                property.setShowInGui(true);
            }
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "gameOpeningMessageList",
                        new String[]{
                                "hardcore_opening",
                                "night_opening",
                                "late_opening",
                                "april_fools_day_opening",
                                "halloween_opening",
                                "spring_festival_opening"
                        });
                property.setComment("Each row corresponds to an opening greeting.\nYou can delete the lines you don't need, or comment them out (by adding a # sign in front of them),\nso that the open screen greeting corresponding to that line will not be sent.");
                // Filter commented lines
                gameOpeningMessageList = Arrays.stream(property.getStringList()).filter(condition -> !condition.startsWith("#")).toArray(String[]::new);
                property.setShowInGui(true);
            }
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "gameOpeningMilliseconds", 1000);
                property.setComment("The duration of the game's opening screen greeting.\nChat messages other than game opening greetings during this time will be rejected and saved in the log.\nYou can adjust this value at your discretion,\ndepending on the number of messages you want to block,\nand the player's experience when sending messages in the game.");
                GameOpeningForYou.gameOpeningMilliseconds = property.getInt();
                property.setShowInGui(true);
            }
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "discordLink", "");
                property.setComment("Put a Discord invitation link here, and you can use `discord` placeholder for a MESSAGE LINE of this link.\nCaution: Any other text in this line will be ignored.");
                GameOpeningForYou.discordLink = property.getString();
                property.setShowInGui(true);
            }
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "qqGroupLink", "");
                property.setComment("Put a QQ Group invitation link here, and you can use `qqgroup` placeholder for a MESSAGE LINE of this link.\nCaution: Any other text in this line will be ignored.");
                GameOpeningForYou.qqGroupLink = property.getString();
                property.setShowInGui(true);
            }
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "curseforgeLink", "");
                property.setComment("Put CurseForge official link of your modpack here, and you can use `curseforge` placeholder for a MESSAGE LINE of this link.\nCaution: Any other text in this line will be ignored.");
                GameOpeningForYou.curseforgeLink = property.getString();
                property.setShowInGui(true);
            }
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "modrinthLink", "");
                property.setComment("Put Modrinth official link of your modpack here, and you can use `modrinth` placeholder for a MESSAGE LINE of this link.\nCaution: Any other text in this line will be ignored.");
                GameOpeningForYou.modrinthLink = property.getString();
                property.setShowInGui(true);
            }
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "discordDisplayText", defaultDiscordDisplayText);
                property.setComment("When you use `discord` placeholder, the placeholder will be replaced with this text and point to your Discord link.");
                GameOpeningForYou.discordDisplayText = property.getString();
                property.setShowInGui(true);
            }
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "qqGroupDisplayText", defaultQQGroupDisplayText);
                property.setComment("When you use `qqgroup` placeholder, the placeholder will be replaced with this text and point to your QQ Group invitation link.");
                GameOpeningForYou.qqGroupDisplayText = property.getString();
                property.setShowInGui(true);
            }
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "curseforgeDisplayText", defaultCurseforgeDisplayText);
                property.setComment("When you use `curseforge` placeholder, the placeholder will be replaced with this text and point to your CurseForge link.");
                GameOpeningForYou.curseforgeDisplayText = property.getString();
                property.setShowInGui(true);
            }
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "modrinthDisplayText", defaultModrinthDisplayText);
                property.setComment("When you use `modrinth` placeholder, the placeholder will be replaced with this text and point to your Modrinth link.");
                GameOpeningForYou.modrinthDisplayText = property.getString();
                property.setShowInGui(true);
            }
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "discordHoverText", defaultDiscordHoverText);
                property.setComment("Hover text displayed when player hover mouse pointer on link.\nDelete the line from config file if you does not need hover texts.");
                if (property.getString().startsWith("#")) GameOpeningForYou.discordHoverText = "";
                else GameOpeningForYou.discordHoverText = property.getString();
                property.setShowInGui(true);
            }
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "qqGroupHoverText", defaultQQGroupHoverText);
                property.setComment("Hover text displayed when player hover mouse pointer on link.\nDelete the line from config file if you does not need hover texts.");
                if (property.getString().startsWith("#")) GameOpeningForYou.qqGroupHoverText = "";
                else GameOpeningForYou.qqGroupHoverText = property.getString();
                property.setShowInGui(true);
            }
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "curseforgeHoverText", defaultCurseforgeHoverText);
                property.setComment("Hover text displayed when player hover mouse pointer on link.\nDelete the line from config file if you does not need hover texts.");
                if (property.getString().startsWith("#")) GameOpeningForYou.curseforgeHoverText = "";
                else GameOpeningForYou.curseforgeHoverText = property.getString();
                property.setShowInGui(true);
            }
            {
                Property property = config.get(Configuration.CATEGORY_GENERAL, "modrinthHoverText", defaultModrinthHoverText);
                property.setComment("Hover text displayed when player hover mouse pointer on link.\nDelete the line from config file if you does not need hover texts.");
                if (property.getString().startsWith("#")) GameOpeningForYou.modrinthHoverText = "";
                else GameOpeningForYou.modrinthHoverText = property.getString();
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

    // Game Opening status
    public static boolean gameOpeningFinished = false;
    public static boolean isWaitingForGameOpeningFinish = false;
    public static WaitingHandler waitingHandlerInstance = null;
    // Control function
    public static void waitForGameOpeningFinish() {
        waitingHandlerInstance = new WaitingHandler();
        waitingHandlerInstance.waitingThread.start();
    }
    public static void stopWaitingForGameOpeningFinish() {
        waitingHandlerInstance.waitingThread.interrupt();
        gameOpeningFinished = false;
        isWaitingForGameOpeningFinish = false;
    }
    public static boolean isStillWaitingForGameOpeningFinish() {
        return waitingHandlerInstance.waitingThread.isInterrupted();
    }
    public static long getRemainingWaitingTime() {
        return waitingHandlerInstance.remainingTime;
    }

    public static class WaitingHandler{
        private long remainingTime = 0;
        private final Thread waitingThread = new Thread(() -> {
            isWaitingForGameOpeningFinish = true;

            // Get current time
            long startTime = System.currentTimeMillis();

            try {
                Thread.sleep(gameOpeningMilliseconds);
            } catch (InterruptedException e) {
                GameOpeningForYou.LOGGER.error("Waiting thread interrupted");
                GameOpeningForYou.LOGGER.error(e.getStackTrace());
                GameOpeningForYou.LOGGER.error("Due to an exception, the game's opening greeting is not working properly. Chat messages are now allowed to be received normally.");
            } finally {
                isWaitingForGameOpeningFinish = false;
                gameOpeningFinished = true;
            }

            long elapsedTime = System.currentTimeMillis() - startTime;
            remainingTime = gameOpeningMilliseconds - elapsedTime;
        });

    }

}