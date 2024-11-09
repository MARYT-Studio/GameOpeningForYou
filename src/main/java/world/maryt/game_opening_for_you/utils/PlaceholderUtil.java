package world.maryt.game_opening_for_you.utils;

import com.google.gson.JsonSyntaxException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;

import java.text.NumberFormat;
import java.util.ArrayList;

import static world.maryt.game_opening_for_you.GameOpeningForYou.*;

@SuppressWarnings("deprecation")
public class PlaceholderUtil {

    private static final String pattern = "{\"text\":\"%s\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"%s\"}%s}";
    private static final String hoverTextPattern = ",\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"%s\"}}";

    public static ArrayList<ITextComponent> parsePlaceholders(String[] lines, String playerName) {

        ArrayList<ITextComponent> result = new ArrayList<>();

        for (String line : lines) {
            String resultLine = line;
            // Detect line type, Null if not a link line, or will be its link type.
            String lineType = detectLinkType(line);
            // If this is not a link line
            if (lineType == null) {
                // player info
                resultLine = resultLine.replaceAll("`player`", playerName);
                // time
                resultLine = hideTimeColon(resultLine);
                // date
                resultLine = resultLine.replaceAll("`year`", getYear());
                resultLine = resultLine.replaceAll("`month`", getMonth());
                resultLine = resultLine.replaceAll("`day`", getDay());
            } else if (!lineType.equals("headline")) {
                resultLine = createLinkLine(lineType);
            } else {
                resultLine = createHeadline();
            }

            if (resultLine == null) {
                LOGGER.warn("Text line: {} failed to create a corresponding message component.", line);
                continue;
            }

            try {
                // If this throws exception, we know that every colon in this line is used by time, not a JSON colon.
                // So this line's return value is not used.
                ITextComponent.Serializer.jsonToComponent(resultLine);
                // Then we do the real time placeholders' replacement
                result.add(ITextComponent.Serializer.jsonToComponent(revealTimeColon(resultLine)));
            } catch (JsonSyntaxException e) {
                result.add(new TextComponentString(revealTimeColon(resultLine)));
            }
        }

        return result;
    }

    // Hide colons to prevent unexpected JSON parsing.
    private static String hideTimeColon(String line) {
        String result = line;
        result = result.replaceAll("`hour`:`min`:`sec`", "`hms`");
        result = result.replaceAll("`min`:`sec`", "`ms`");
        result = result.replaceAll("`hour`:`min`", "`hm`");
        return result;
    }

    // Reveal the hidden colons and do the time placeholders' replacement.
    private static String revealTimeColon(String line) {
        String result = line;
        result = result.replaceAll("`hms`", "`hour`:`min`:`sec`");
        result = result.replaceAll("`ms`", "`min`:`sec`");
        result = result.replaceAll("`hm`", "`hour`:`min`");
        // time
        result = result.replaceAll("`hour`", getHour());
        result = result.replaceAll("`min`", getMinute());
        result = result.replaceAll("`sec`", getSecond());
        return result;
    }

    private static String detectLinkType(String line) {
        if (line.contains("`discord`")) return "discord";
        if (line.contains("`qqgroup`")) return "qqgroup";
        if (line.contains("`curseforge`")) return "curseforge";
        if (line.contains("`modrinth`")) return "modrinth";

        // Headline
        if (line.contains("`hr`") || line.contains("`headline`")) return "headline";
        return null;
    }

    private static String createHeadline() {
        return I18n.translateToLocal("game_opening_for_you.headline");
    }

    private static String createLinkLine(String linkType) {
        String hoverText = createHoverText(linkType);
        if (hoverText == null) {
            LOGGER.error("failed to generate link line for link type: {}, due to hover text generation failed.", linkType);
            return null;
        }
        switch (linkType) {
            case "discord":
                if (discordLink.isEmpty()) {
                    if (DEBUG) LOGGER.warn("Link type discord has an empty link.");
                    return null;
                }
                if (discordDisplayText.isEmpty()) return String.format(pattern, defaultDiscordDisplayText, discordLink, hoverText);
                return String.format(pattern, I18n.translateToLocal(discordDisplayText), discordLink, hoverText);
            case "qqgroup":
                if (qqGroupLink.isEmpty()) {
                    if (DEBUG) LOGGER.warn("Link type qqgroup has an empty link.");
                    return null;
                }
                if (qqGroupDisplayText.isEmpty()) return String.format(pattern, defaultQQGroupDisplayText, qqGroupLink, hoverText);
                return String.format(pattern, I18n.translateToLocal(qqGroupDisplayText), qqGroupLink, hoverText);
            case "curseforge":
                if (curseforgeLink.isEmpty()) {
                    if (DEBUG) LOGGER.warn("Link type curseforge has an empty link.");
                    return null;
                }
                if (curseforgeDisplayText.isEmpty()) return String.format(pattern, defaultCurseforgeDisplayText, curseforgeLink, hoverText);
                return String.format(pattern, I18n.translateToLocal(curseforgeDisplayText), curseforgeLink, hoverText);
            case "modrinth":
                if (modrinthLink.isEmpty()) {
                    if (DEBUG) LOGGER.warn("Link type modrinth has an empty link.");
                    return null;
                }
                if (modrinthDisplayText.isEmpty()) return String.format(pattern, defaultModrinthDisplayText, modrinthLink, hoverText);
                return String.format(pattern, I18n.translateToLocal(modrinthDisplayText), modrinthLink, hoverText);
            default:
                LOGGER.error("failed to create link line for link type: {}", linkType);
                return null;
        }
    }

    private static String createHoverText(String linkType) {
        switch (linkType) {
            case "discord":
                if (discordHoverText.isEmpty()) return "";
                return String.format(hoverTextPattern, I18n.translateToLocal(discordHoverText));
            case "qqgroup":
                if (qqGroupHoverText.isEmpty()) return "";
                return String.format(hoverTextPattern, I18n.translateToLocal(qqGroupHoverText));
            case "curseforge":
                if (curseforgeHoverText.isEmpty()) return "";
                return String.format(hoverTextPattern, I18n.translateToLocal(curseforgeHoverText));
            case "modrinth":
                if (modrinthHoverText.isEmpty()) return "";
                return String.format(hoverTextPattern, I18n.translateToLocal(modrinthHoverText));
            default:
                LOGGER.error("failed to generate hover text for link type: {}", linkType);
                return null;
        }
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
