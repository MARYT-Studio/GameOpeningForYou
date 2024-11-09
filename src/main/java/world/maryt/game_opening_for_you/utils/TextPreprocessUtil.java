package world.maryt.game_opening_for_you.utils;

import com.google.gson.stream.MalformedJsonException;
import net.minecraft.util.text.*;

import java.text.NumberFormat;
import java.util.ArrayList;

import static world.maryt.game_opening_for_you.GameOpeningForYou.*;

public class TextPreprocessUtil {

    public static ArrayList<ITextComponent> preprocess(String[] lines, String playerName) {

        ArrayList<String> singleReplaceResult = new ArrayList<>();

        for (String line : lines) {
            String resultLine = line;
            // player info
            resultLine = resultLine.replaceAll("`player`", playerName);
            // time
            resultLine = resultLine.replaceAll("`hour`", getHour());
            resultLine = resultLine.replaceAll("`min`", getMinute());
            resultLine = resultLine.replaceAll("`sec`", getSecond());
            // date
            resultLine = resultLine.replaceAll("`year`", getYear());
            resultLine = resultLine.replaceAll("`month`", getMonth());
            resultLine = resultLine.replaceAll("`day`", getDay());

            singleReplaceResult.add(resultLine);
        }

        ArrayList<ITextComponent> processedMessageList = new ArrayList<>();
        for (String resultLine: singleReplaceResult) {
            resultLine = replaceLinks(resultLine, "`discord`");
            resultLine = replaceLinks(resultLine, "`curseforge`");
            resultLine = replaceLinks(resultLine, "`modrinth`");

            LOGGER.info("Line: {}", resultLine);
            processedMessageList.add(ITextComponent.Serializer.jsonToComponent(resultLine));
        }

        return processedMessageList;
    }

    private static String replaceLinks(String message, String placeholder) {

        if (DEBUG) LOGGER.info("Link replacer {} working", placeholder);

        String linkSibling = createLinkSibling(placeholder);
        if (linkSibling == null) {
            if (DEBUG) LOGGER.warn("Failed to create link sibling for {}. If you intend to leave it blank, ignore this warning.", placeholder);
            return message;
        }

        String[] splitMessage = message.split(placeholder);
        StringBuilder replacedMessage = new StringBuilder().append("[");
        for (int i = 0; i < splitMessage.length; i++) {
            replacedMessage.append(String.format("{" +
                    "\"text\": \"%s\"" +
                    "}", splitMessage[i]));
            if (i < splitMessage.length - 1) replacedMessage.append(String.format(", %s,", linkSibling));
        }
        return replacedMessage.append("]").toString();
    }

    private static String createLinkSibling(String placeholder) {
        if (placeholder == null) return null;

        String link = "";
        String displayText = "";

        if (placeholder.equalsIgnoreCase("`curseforge`")) {
            link = curseforgeLink;
            displayText = curseforgeDisplayText;
        } else if (placeholder.equalsIgnoreCase("`modrinth`")) {
            link = modrinthLink;
            displayText = modrinthDisplayText;
        } else if (placeholder.equalsIgnoreCase("`discord`")) {
            link = discordLink;
            displayText = discordDisplayText;
        }

        if (link.isEmpty() || displayText.isEmpty()) return null;

        // Check format with Serializer
        if (ITextComponent.Serializer.jsonToComponent(
                String.format("{" +
                        "\"text\": \"%s\"," +
                        "\"clickEvent\": {" +
                        "\"action\": \"open_url\"," +
                        "\"value\": \"%s\"" +
                        "}" +
                        "}", displayText, link)
        ) == null) return null;

        // Return the JSON
        return String.format("{" +
                "\"text\": \"%s\"," +
                "\"clickEvent\": {" +
                "\"action\": \"open_url\"," +
                "\"value\": \"%s\"" +
                "}" +
                "}", displayText, link);
    }

    private static String getYear() {
        return "" + TimeAndDateUtil.year;
    }

    private static String getMonth() {
        return monthFixDigits ? fix(TimeAndDateUtil.month) : "" + TimeAndDateUtil.month;
    }

    private static String getDay() {
        return dayFixDigits ? fix(TimeAndDateUtil.day) : "" + TimeAndDateUtil.day;
    }

    private static String getHour() {
        return hourFixDigits ? fix(TimeAndDateUtil.hour) : "" + TimeAndDateUtil.hour;
    }

    private static String getMinute() {
        return minuteFixDigits ? fix(TimeAndDateUtil.minute) : "" + TimeAndDateUtil.minute;
    }

    private static String getSecond() {
        return secondFixDigits ? fix(TimeAndDateUtil.second) : "" + TimeAndDateUtil.second;
    }

    private static String fix(int raw) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setGroupingUsed(false);
        numberFormat.setMinimumIntegerDigits(2);
        numberFormat.setMaximumIntegerDigits(2);

        return numberFormat.format(raw);
    }
}
