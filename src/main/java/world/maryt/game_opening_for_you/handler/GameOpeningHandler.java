package world.maryt.game_opening_for_you.handler;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GameOpeningHandler {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOW)
    public void clearMessages(PlayerEvent.PlayerLoggedInEvent event) {

        if (event.player.world.isRemote) {
            // Clear messages before sending yours
            // Minecraft.getMinecraft().ingameGUI.getChatGUI().refreshChat();
            EntityPlayerSP player = (EntityPlayerSP) event.player;
            for (int i = 0; i < 10; i++) {
                player.sendChatMessage("");
            }
            player.sendChatMessage("1");
        }
    }
}
