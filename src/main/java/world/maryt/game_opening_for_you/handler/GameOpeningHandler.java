package world.maryt.game_opening_for_you.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import world.maryt.game_opening_for_you.GameOpeningForYou;
import world.maryt.game_opening_for_you.utils.GameInfoUtil;
import world.maryt.game_opening_for_you.utils.TextPreprocessUtil;
import world.maryt.game_opening_for_you.utils.TimeAndDateUtil;

import static world.maryt.game_opening_for_you.GameOpeningForYou.*;

public class GameOpeningHandler {

    private final String OPENING_FINISH_SIGNAL = GameOpeningForYou.MOD_ID + ".opening_finish";
    private boolean gameOpeningFinished;

    public GameOpeningHandler() {
        this.gameOpeningFinished = false;
    }

    @SubscribeEvent
    public void clearMessages(ClientChatReceivedEvent event) {
        if (!this.gameOpeningFinished) {
            ITextComponent message = event.getMessage();

            // Opening Finish Signal
            if (event.getMessage().getFormattedText().equals(OPENING_FINISH_SIGNAL)) {
                gameOpeningFinished = true;
                event.setCanceled(true);
                return;
            }

            if (MessageMarkHelper.messageNotMarked(message)) {
                GameOpeningForYou.MISSED_MSG_LOGGER.info("Missed Message: {}", message.getFormattedText());
                event.setCanceled(true);
            }
            else {
                event.setMessage(MessageMarkHelper.removeMessageMark(message));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void gameOpening(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        boolean defaultMessage = true;
        for (String openingEntry : GameOpeningForYou.gameOpeningMessageList) {
            if (conditionSatisfied(openingEntry, player)) {
                player.sendMessage(MessageMarkHelper.markMessage(createOpeningMessage(openingEntry, player)));
                defaultMessage = false;
            }
        }
        if (defaultMessage) {player.sendMessage(MessageMarkHelper.markMessage(createOpeningMessage("default_opening", player)));}
        player.sendMessage(createOpeningFinishSignal());
    }

    // TODO: Player info
    private ITextComponent createOpeningMessage(String openingEntry, EntityPlayer player) {
        return new TextComponentTranslation(String.format("%s.%s.text", GameOpeningForYou.MOD_ID, openingEntry));
    }

    private ITextComponent createOpeningFinishSignal() {
        return new TextComponentString(OPENING_FINISH_SIGNAL);
    }

    private boolean conditionSatisfied(String openingEntry, EntityPlayer player) {
        // Default message is only sent when other conditions are all unsatisfied.
        // It is checked separately.
        // Game info checks
        if (openingEntry.contains("hardcore")) {
            if (DEBUG) GameOpeningForYou.LOGGER.info("{}: {}", openingEntry, GameInfoUtil.isHardcore(player));
            return GameInfoUtil.isHardcore(player);
        }
        // Time related checks
        if (openingEntry.contains("night")) {
            if (DEBUG) {
                GameOpeningForYou.LOGGER.info("{}: {}", openingEntry, TimeAndDateUtil.isNight());
                TimeAndDateUtil.displayTime("night");
            }
            return TimeAndDateUtil.isNight();
        }
        if (openingEntry.contains("late")) {
            if (DEBUG) {
                GameOpeningForYou.LOGGER.info("{}: {}", openingEntry, TimeAndDateUtil.isLateNight());
                TimeAndDateUtil.displayTime("late");
            }
            return TimeAndDateUtil.isLateNight();
        }
        // Date related checks
        if (openingEntry.contains("april_fool")) {
            if (DEBUG) {
                GameOpeningForYou.LOGGER.info("{}: {}", openingEntry, TimeAndDateUtil.isAprilFool());
                TimeAndDateUtil.displayDate();
            }
            return TimeAndDateUtil.isAprilFool();
        }
        if (openingEntry.contains("halloween")) {
            if (DEBUG) {
                GameOpeningForYou.LOGGER.info("{}: {}", openingEntry, TimeAndDateUtil.isHalloween());
                TimeAndDateUtil.displayDate();
            }
            return TimeAndDateUtil.isHalloween();
        }
        if (openingEntry.contains("spring_festival")) {
            if (DEBUG) {
                GameOpeningForYou.LOGGER.info("{}: {}", openingEntry, TimeAndDateUtil.isSpringFestival());
                TimeAndDateUtil.displayDate();
            }
            return TimeAndDateUtil.isSpringFestival();
        }
        // Default
        return false;
    }


}
