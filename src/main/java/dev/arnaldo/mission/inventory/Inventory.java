package dev.arnaldo.mission.inventory;

import dev.arnaldo.mission.Main;
import dev.arnaldo.mission.model.User;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Inventory {

    void open(@NotNull Player player, @NotNull User user);

    void load(@NotNull Main main);

}
