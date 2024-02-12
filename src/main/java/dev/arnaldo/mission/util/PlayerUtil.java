package dev.arnaldo.mission.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class PlayerUtil {

    public void give(@NonNull Player player, @NonNull ItemStack itemStack) {
        for (ItemStack drop : player.getInventory().addItem(itemStack).values()) {
            player.getWorld().dropItem(player.getLocation(), drop);
        }
    }

}