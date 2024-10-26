package world.maryt.game_opening_for_you.utils;

import cn.hutool.core.date.ChineseDate;
import cn.hutool.core.date.DateUtil;
import world.maryt.game_opening_for_you.GameOpeningForYou;

import java.util.Calendar;

public class TimeAndDateUtil {
    private static final Calendar calendar = Calendar.getInstance();
    public static int year = calendar.get(Calendar.YEAR);
    public static int month = calendar.get(Calendar.MONTH) + 1;
    public static int day = calendar.get(Calendar.DAY_OF_MONTH);
    public static int hour = calendar.get(Calendar.HOUR_OF_DAY);
    public static int minute = calendar.get(Calendar.MINUTE);
    public static int second = calendar.get(Calendar.SECOND);

    // Several dates frequently used
    public static boolean isAprilFool() {
        return month == Calendar.APRIL && day == 1;
    }

    public static boolean isHalloween() {
        return month == Calendar.OCTOBER && day == 31;
    }

    // Chinese New Year
    public static boolean isSpringFestival() {
        ChineseDate chineseDate = new ChineseDate(DateUtil.parseDate(String.format("%d-%d-%d", year, month, day)));
        return chineseDate.getFestivals().contains("春节");
    }

    // Time related
    public static boolean isNight() {
        if (hour >= GameOpeningForYou.daytimeEndHour && minute >= GameOpeningForYou.daytimeEndMinute) return true;
        return hour <= GameOpeningForYou.daytimeBeginningHour && minute <= GameOpeningForYou.daytimeBeginningMinute;
    }
}
