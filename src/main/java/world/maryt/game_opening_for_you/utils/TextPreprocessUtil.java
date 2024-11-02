package world.maryt.game_opening_for_you.utils;

import com.google.gson.JsonSyntaxException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.text.NumberFormat;

import static world.maryt.game_opening_for_you.GameOpeningForYou.hourFixDigits;
import static world.maryt.game_opening_for_you.GameOpeningForYou.minuteFixDigits;
import static world.maryt.game_opening_for_you.GameOpeningForYou.secondFixDigits;
import static world.maryt.game_opening_for_you.GameOpeningForYou.monthFixDigits;
import static world.maryt.game_opening_for_you.GameOpeningForYou.dayFixDigits;

public class TextPreprocessUtil {

    public static ITextComponent preprocess(String rawString, String playerName) {
        
        // player info
        rawString = rawString.replaceAll("%player%", playerName);
        // time
        rawString = rawString.replaceAll("%hour%", getHour());
        rawString = rawString.replaceAll("%min%", getMinute());
        rawString = rawString.replaceAll("%sec%", getSecond());
        // date
        rawString = rawString.replaceAll("%year%", getYear());
        rawString = rawString.replaceAll("%month%", getMonth());
        rawString = rawString.replaceAll("%day%", getDay());

        try {
            return ITextComponent.Serializer.jsonToComponent(rawString);
        } catch (JsonSyntaxException e) {
            return new TextComponentString(rawString);
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
