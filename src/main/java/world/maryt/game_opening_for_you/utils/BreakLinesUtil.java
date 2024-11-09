package world.maryt.game_opening_for_you.utils;
import static world.maryt.game_opening_for_you.GameOpeningForYou.DEBUG;
import static world.maryt.game_opening_for_you.GameOpeningForYou.LOGGER;

public class BreakLinesUtil {
    public static String[] breakline(String rawString) {
        // Pre-process headlines
        String headlinePreprocessed = rawString.replaceAll("`hr`", "`br``hr``br`");
        headlinePreprocessed = headlinePreprocessed.replaceAll("`headline`", "`br``hr``br`");
        if (DEBUG) LOGGER.info("Headline preprocess result: {}", headlinePreprocessed);
        // Break lines
        String[] result = headlinePreprocessed.split("`br`");
        if (DEBUG) {
            LOGGER.info("Break line result:");
            for (String line : result) {
                LOGGER.info(line);
            }
        }
        return result;
    }
}
