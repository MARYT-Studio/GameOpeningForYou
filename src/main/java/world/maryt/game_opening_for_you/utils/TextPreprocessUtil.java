package world.maryt.game_opening_for_you.utils;

import com.google.gson.JsonSyntaxException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.text.NumberFormat;
import java.util.ArrayList;

import static world.maryt.game_opening_for_you.GameOpeningForYou.hourFixDigits;
import static world.maryt.game_opening_for_you.GameOpeningForYou.minuteFixDigits;
import static world.maryt.game_opening_for_you.GameOpeningForYou.secondFixDigits;
import static world.maryt.game_opening_for_you.GameOpeningForYou.monthFixDigits;
import static world.maryt.game_opening_for_you.GameOpeningForYou.dayFixDigits;

public class TextPreprocessUtil {

    public static ArrayList<ITextComponent> preprocess(String[] lines, String playerName) {

        ArrayList<ITextComponent> result = new ArrayList<>();

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

            try {
                result.add(ITextComponent.Serializer.jsonToComponent(resultLine));
            } catch (JsonSyntaxException e) {
                result.add(new TextComponentString(resultLine));
            }
        }


        return result;
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
