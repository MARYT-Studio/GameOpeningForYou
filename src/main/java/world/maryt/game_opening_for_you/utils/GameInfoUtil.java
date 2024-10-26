package world.maryt.game_opening_for_you.utils;

import net.minecraft.entity.player.EntityPlayer;

public class GameInfoUtil {
    public static boolean isHardcore(EntityPlayer player) {
        return player.world.getWorldInfo().isHardcoreModeEnabled();
    }
}
