package world.maryt.game_opening_for_you.utils;

import net.minecraft.entity.player.EntityPlayer;

import java.text.NumberFormat;

import static world.maryt.game_opening_for_you.GameOpeningForYou.hourFixDigits;
import static world.maryt.game_opening_for_you.GameOpeningForYou.minuteFixDigits;
import static world.maryt.game_opening_for_you.GameOpeningForYou.secondFixDigits;
import static world.maryt.game_opening_for_you.GameOpeningForYou.monthFixDigits;
import static world.maryt.game_opening_for_you.GameOpeningForYou.dayFixDigits;

public class TextPreprocessUtil {

    public static String preprocess(String rawLanguageValue, EntityPlayer player) {
        String result;
        // player info
        result = rawLanguageValue.replaceAll("%name%", player.getName());
        // time
        result = result.replaceAll("%hour%", getHour());
        result = result.replaceAll("%min%", getMinute());
        result = result.replaceAll("%sec%", getSecond());
        // date
        result = result.replaceAll("%year%", getYear());
        result = result.replaceAll("%month%", getMonth());
        result = result.replaceAll("%day%", getDay());

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
