package world.maryt.game_opening_for_you.utils;

import cn.hutool.core.date.ChineseDate;
import cn.hutool.core.date.DateUtil;
import world.maryt.game_opening_for_you.GameOpeningForYou;

import java.text.NumberFormat;
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
        if (hour > GameOpeningForYou.daytimeEndHour || (hour == GameOpeningForYou.daytimeEndHour) && (minute >= GameOpeningForYou.daytimeEndMinute)) return true;
        return (hour < GameOpeningForYou.daytimeBeginningHour) || (hour == GameOpeningForYou.daytimeBeginningHour && minute <= GameOpeningForYou.daytimeBeginningMinute);
    }

    public static boolean isLateNight() {
        if (hour > GameOpeningForYou.lateNightBeginningHour || (hour == GameOpeningForYou.lateNightBeginningHour) && (minute >= GameOpeningForYou.lateNightBeginningMinute)) return true;
        return (hour < GameOpeningForYou.daytimeBeginningHour) || (hour == GameOpeningForYou.daytimeBeginningHour && minute <= GameOpeningForYou.daytimeBeginningMinute);
    }

    public static void displayTime(String time) {
        // Time formatting
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setGroupingUsed(false);
        numberFormat.setMinimumIntegerDigits(2);
        numberFormat.setMaximumIntegerDigits(2);

        if (time.equals("night")) {
            GameOpeningForYou.LOGGER.info("In config, Daytime beginning time is defined as {}:{}," +
                            "and daytime ending time is defined as {}:{}," +
                            "while it's {}:{} now.",
                    numberFormat.format(GameOpeningForYou.daytimeBeginningHour),
                    numberFormat.format(GameOpeningForYou.daytimeBeginningMinute),
                    numberFormat.format(GameOpeningForYou.daytimeEndHour),
                    numberFormat.format(GameOpeningForYou.daytimeEndMinute),
                    numberFormat.format(TimeAndDateUtil.hour),
                    numberFormat.format(TimeAndDateUtil.minute));
            return;
        }
        if (time.equals("late")) {
            GameOpeningForYou.LOGGER.info("In config, late night beginning time is defined as {}:{}," +
                            "and daytime beginning time is defined as {}:{}," +
                            "while it's {}:{} now.",
                    numberFormat.format(GameOpeningForYou.lateNightBeginningHour),
                    numberFormat.format(GameOpeningForYou.lateNightBeginningMinute),
                    numberFormat.format(GameOpeningForYou.daytimeBeginningHour),
                    numberFormat.format(GameOpeningForYou.daytimeBeginningMinute),
                    numberFormat.format(TimeAndDateUtil.hour),
                    numberFormat.format(TimeAndDateUtil.minute));
            return;
        }
        GameOpeningForYou.LOGGER.info("It's {}:{} now.",
                numberFormat.format(TimeAndDateUtil.hour),
                numberFormat.format(TimeAndDateUtil.minute));
    }

    public static void displayDate() {
        // Date formatting
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setGroupingUsed(false);
        numberFormat.setMinimumIntegerDigits(2);
        numberFormat.setMaximumIntegerDigits(2);
        GameOpeningForYou.LOGGER.info("Mod detected date is {}-{}.",
                numberFormat.format(TimeAndDateUtil.month),
                numberFormat.format(TimeAndDateUtil.day));
    }
}
