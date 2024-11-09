package world.maryt.game_opening_for_you.handler;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import world.maryt.game_opening_for_you.GameOpeningForYou;
import world.maryt.game_opening_for_you.utils.GameInfoUtil;
import world.maryt.game_opening_for_you.utils.TextPreprocessUtil;
import world.maryt.game_opening_for_you.utils.TimeAndDateUtil;

import java.util.ArrayList;

import static world.maryt.game_opening_for_you.GameOpeningForYou.*;


@SideOnly(Side.CLIENT)
public class GameOpeningHandler {

    @SubscribeEvent
    public void clearMessages(ClientChatReceivedEvent event) {
        if (isWaitingForGameOpeningFinish) {
            ITextComponent message = event.getMessage();

            if (MessageMarkHelper.messageNotMarked(message)) {
                MISSED_MSG_LOGGER.info("Missed Message: {}", message.getFormattedText());
                event.setCanceled(true);
            }
            else {
                event.setMessage(MessageMarkHelper.removeMessageMark(message));
                if(DEBUG) LOGGER.info("Message after removing mark: {}", event.getMessage());
            }

            if (DEBUG) {
                if (event.isCanceled()) {
                    LOGGER.info("Rejected message: {} in the waiting period. {} milliseconds remains.", event.getMessage(), getRemainingWaitingTime());
                } else {
                    LOGGER.info("Received message: {} in the waiting period. {} milliseconds remains.", event.getMessage(), getRemainingWaitingTime());
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void gameOpening(PlayerEvent.PlayerLoggedInEvent event) {
        waitForGameOpeningFinish();
        EntityPlayer player = event.player;
        if (gameOpeningMessageList == null || gameOpeningMessageList.length == 0)
            sendMessageToPlayer(MessageMarkHelper.markMessage(createOpeningMessage("default_opening", player)), player);
        else {
            boolean defaultMessage = true;
            for (String openingEntry : gameOpeningMessageList) {
                if (conditionSatisfied(openingEntry, player)) {
                    sendMessageToPlayer(MessageMarkHelper.markMessage(createOpeningMessage(openingEntry, player)), player);
                    defaultMessage = false;
                }
            }
            if (defaultMessage) sendMessageToPlayer(MessageMarkHelper.markMessage(createOpeningMessage("default_opening", player)), player);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void refreshGameOpening(PlayerEvent.PlayerLoggedOutEvent event) {
        if (isStillWaitingForGameOpeningFinish()) stopWaitingForGameOpeningFinish();
    }

    private ArrayList<ITextComponent> createOpeningMessage(String openingEntry, EntityPlayer player) {
        String rawTranslation = I18n.format(String.format("%s.%s.text", GameOpeningForYou.MOD_ID, openingEntry));
        String[] lines = rawTranslation.split("`br`");
        return TextPreprocessUtil.preprocess(lines, player.getName());
    }

    private void sendMessageToPlayer(ArrayList<ITextComponent> messageList, EntityPlayer player) {
        for (ITextComponent message : messageList) {
            player.sendMessage(message);
        }
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
