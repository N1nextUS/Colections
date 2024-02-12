package dev.arnaldo.mission.command;

import dev.arnaldo.mission.Main;
import dev.arnaldo.mission.command.annotation.CommandPath;
import dev.arnaldo.mission.command.annotation.HelpPosition;
import dev.arnaldo.mission.command.annotation.Positive;
import dev.arnaldo.mission.inventory.InventoryType;
import dev.arnaldo.mission.manager.UserManager;
import dev.arnaldo.mission.model.Config;
import dev.arnaldo.mission.model.Item;
import dev.arnaldo.mission.model.User;
import dev.arnaldo.mission.util.PlayerUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.help.CommandHelp;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.logging.Level;

@RequiredArgsConstructor
@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class CollectionCommand {

    private final UserManager userManager;

    @DefaultFor("~")
    @HelpPosition(1)
    @CommandPath("collection-command")
    public void collection(BukkitCommandActor actor) {
        Player player = actor.requirePlayer();;
        findUser(actor, player, user -> InventoryType.COLLECT.getInventory().open(player, user));
    }

    @HelpPosition(2)
    @CommandPath("item-give-command")
    public void giveItem(CommandActor actor, Player target, Item item, @Positive(ignoreZero = true) Integer amount) {
        ItemStack itemStack = item.getItem();
        itemStack.setAmount(amount);

        PlayerUtil.give(target, itemStack);
        actor.reply(Config.ITEM_SENT_MESSAGE.asString()
                .replace("{item}", item.getName())
                .replace("{player}", target.getName())
                .replace("{amount}", String.valueOf(amount)));
    }

    @HelpPosition(3)
    @CommandPath("reset-collection-command")
    public void resetCollection(CommandActor actor, OfflinePlayer target) {
        this.userManager.findIfPresent(target.getName()).ifPresent(user -> user.setItems(Collections.emptyList()));
        this.userManager.delete(target.getName());

        actor.reply(Config.ITEMS_REDEFINED_MESSAGE.asString().replace("{player}", target.getName()));
    }

    @HelpPosition(4)
    @CommandPath("reload-command")
    public void reload(BukkitCommandActor actor) {
        Main.getInstance().reload();
        actor.reply(Config.PLUGIN_RELOADED_MESSAGE.asString());
    }

    @HelpPosition(5)
    @CommandPath("help-command")
    public void help(BukkitCommandActor actor, CommandHelp<String> help) {
        actor.reply("");
        actor.reply(String.join("\n", help));
        actor.reply("");
    }

    private void findUser(@NotNull CommandActor actor, @NotNull OfflinePlayer player, @NotNull Consumer<User> consumer) {
        this.userManager.find(player.getName()).whenComplete((user, error) -> {
            if (error != null) {
                Main.getInstance().getLogger().log(Level.SEVERE, "An error occurred while trying to find the user", error);
                return;
            }

            if (user == null) {
                actor.reply(Config.PLAYER_NOT_FOUND_MESSAGE.asString());
                return;
            }

            try {
                consumer.accept(user);
            } catch (Exception exception) {
                Main.getInstance().getLogger().log(Level.SEVERE, "command error", exception);
            }
        });
    }

}