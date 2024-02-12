package dev.arnaldo.mission.listener;

import dev.arnaldo.mission.Main;
import dev.arnaldo.mission.manager.ItemManager;
import dev.arnaldo.mission.manager.UserManager;
import dev.arnaldo.mission.model.Item;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

@RequiredArgsConstructor
public class PlayerInteractListener implements Listener {

    private final UserManager userManager;
    private final ItemManager itemManager;

    @EventHandler
    public void onInteract(@NotNull PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_BLOCK &&
                action != Action.RIGHT_CLICK_AIR)
            return;

        Item item = this.itemManager.find(event.getItem()).orElse(null);
        if (item == null) return;

        Player player = event.getPlayer();
        this.userManager.find(player).whenComplete((user, error) -> {
            if (error != null) {
                Main.getInstance().getLogger().log(Level.SEVERE, "interact error", error);
                return;
            }

            ItemStack hand = player.getItemInHand();
            if (!item.getItem().isSimilar(hand) ||
                    !this.itemManager.collect(player, user, item))
                return;

            int amount = hand.getAmount();
            if (amount > 1) {
                hand.setAmount(amount - 1);
                return;
            }

            player.setItemInHand(null);
        });

        event.setCancelled(true);
    }

}
