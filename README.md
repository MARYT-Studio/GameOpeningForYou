## Game Opening For You

![](src/main/resources/logo.png)
_________________
## Overview

Game-Opening, only for your modpack.

Manage your own messages in one mod, and remove any other messages from mods.

## Configuration

Configuration file is fully documented.

Configure it is quick and easy:

```
# Configuration file

general {
    # Display debug logs. For debug purposes.
    B:DEBUG=true

    # The time of day you believe daylight begins during the day, expressed in 24-hour format. The default value is 8:00.
    I:daytimeBeginningHour=8

    # The time of day you believe daylight begins during the day, expressed in 24-hour format. The default value is 8:00.
    I:daytimeBeginningMinute=0

    # The time of day you believe daylight ends during the day, expressed in 24-hour format. The default value is 18:30.
    I:daytimeEndHour=18

    # The time of day you believe daylight ends during the day, expressed in 24-hour format. The default value is 18:30.
    I:daytimeEndMinute=30

    # Each row corresponds to an opening greeting.
    # You can delete the lines you don't need, or comment them out (by adding a # sign in front of them),
    # so that the open screen greeting corresponding to that line will not be sent.
    S:gameOpeningMessageList <
        hardcore_opening
        night_opening
        late_opening
        april_fools_day_opening
        halloween_opening
        spring_festival_opening
     >

    # The duration of the game's opening screen greeting.
    # Chat messages other than game opening greetings during this time will be rejected and saved in the log.
    # You can adjust this value at your discretion,
    # depending on the number of messages you want to block,
    # and the player's experience when sending messages in the game.
    I:gameOpeningMilliseconds=1000

    # Whether to fix day to 2 digits.
    B:hourFixDigits=false

    # The time of day you think late night starts, expressed in 24-hour format. The default value is 23:30.
    I:lateNightBeginningHour=23

    # The time of day you think late night starts, expressed in 24-hour format. The default value is 23:30.
    I:lateNightBeginningMinute=30

    # Whether to fix minute to 2 digits.
    B:minuteFixDigits=true

    # Whether to fix month to 2 digits.
    B:monthFixDigits=false

    # Whether to fix hour to 2 digits.
    B:secondFixDigits=true
}
```

## License

This project's license will be kept the same as its dependency [**dromara/hutool**](https://github.com/dromara/hutool) 's license.

_________________

## Credits

Text art used in project's logo is created with:

**Font Generator** powered by https://www.textstudio.com/

_Dedicated to my sleep-aiding wizard, the beloved ASMR artist_ [**Youtube@LatteASMR**](https://www.youtube.com/watch?v=ZD67afkURFQ&t=104s).
