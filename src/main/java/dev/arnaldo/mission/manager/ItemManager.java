package dev.arnaldo.mission.manager;

import dev.arnaldo.mission.model.Config;
import dev.arnaldo.mission.model.Item;
import dev.arnaldo.mission.model.User;
import dev.arnaldo.mission.model.UserItem;
import dev.arnaldo.mission.util.StringUtil;
import dev.arnaldo.mission.util.item.ItemUtil;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.time.Instant;
import java.util.*;

public class ItemManager {

    private final Map<String, Item> items = new HashMap<>();

    public void validate(@NonNull Item item) {
        this.items.put(item.getId(), item);
    }

    public void invalidate(@NonNull String id) {
        this.items.remove(id);
    }

    @Unmodifiable
    public Collection<Item> findAll() {
        return Collections.unmodifiableCollection(this.items.values());
    }

    public Optional<Item> find(@NonNull String id) {
        return Optional.ofNullable(this.items.get(id));
    }

    public Optional<Item> find(@Nullable ItemStack item) {
        if (item == null || item.getType() == Material.AIR)
            return Optional.empty();

        String id = ItemUtil.getInfo(item, Item.NBT_KEY);
        return id == null ? Optional.empty() : this.find(id);
    }

    public boolean collect(@NonNull Player player, @NonNull User user, @NonNull Item item) {
        if (user.hasItem(item.getId())) {
            player.sendMessage(Config.ITEM_ALREADY_COLLECTED_MESSAGE.asString());
            return false;
        }

        user.addItem(new UserItem(item.getId(), Instant.now(), true));
        if (user.getItems().size() >= Config.QUEST_AMOUNT_SETTING.asInt()) {
            Bukkit.broadcastMessage(Config.QUEST_COMPLETED_MESSAGE.asString().replace("{player}", player.getName()));
        }

        player.sendMessage(StringUtil.replaceItem(item, Config.ITEM_COLLECTED_SETTING.asString()));
        return true;
    }

}