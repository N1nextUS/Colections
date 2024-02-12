package dev.arnaldo.mission.listener;

import dev.arnaldo.mission.Main;
import dev.arnaldo.mission.manager.UserManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

@RequiredArgsConstructor
public class PlayerJoinListener implements Listener {

    private final UserManager userManager;

    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent event) {
        this.userManager.find(event.getPlayer()).whenComplete((user, error) -> {
            if (error != null) {
                Main.getInstance().getLogger().log(Level.SEVERE, "An error occurred while trying to find the user", error);
            }

            if (user == null) {
                event.getPlayer().kickPlayer("§cAn error occurred while trying to find your user, please try again later.");
            }
        });
    }

}